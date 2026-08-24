package io.manbang.ebatis.core.request;

import io.manbang.ebatis.core.annotation.Agg;
import io.manbang.ebatis.core.annotation.DeleteByQuery;
import io.manbang.ebatis.core.annotation.Count;
import io.manbang.ebatis.core.annotation.MultiSearch;
import io.manbang.ebatis.core.annotation.QueryType;
import io.manbang.ebatis.core.annotation.Search;
import io.manbang.ebatis.core.annotation.SearchScroll;
import io.manbang.ebatis.core.annotation.UpdateByQuery;
import io.manbang.ebatis.core.builder.QueryBuilderFactory;
import io.manbang.ebatis.core.common.AnnotationUtils;
import io.manbang.ebatis.core.domain.ContextHolder;
import io.manbang.ebatis.core.domain.Pageable;
import io.manbang.ebatis.core.domain.ScriptField;
import io.manbang.ebatis.core.domain.Sort;
import io.manbang.ebatis.core.exception.ConditionNotSupportException;
import io.manbang.ebatis.core.meta.MetaUtils;
import io.manbang.ebatis.core.meta.MethodMeta;
import io.manbang.ebatis.core.meta.ParameterMeta;
import io.manbang.ebatis.core.provider.CollapseProvider;
import io.manbang.ebatis.core.provider.HighlighterProvider;
import io.manbang.ebatis.core.provider.PointTimeProvider;
import io.manbang.ebatis.core.provider.QueryBuilderProvider;
import io.manbang.ebatis.core.provider.RoutingProvider;
import io.manbang.ebatis.core.provider.ScriptFieldProvider;
import io.manbang.ebatis.core.provider.SearchAfterProvider;
import io.manbang.ebatis.core.provider.SearchSourceBuilderProvider;
import io.manbang.ebatis.core.provider.SortProvider;
import io.manbang.ebatis.core.provider.SourceProvider;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.Requests;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.builder.PointInTimeBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static io.manbang.ebatis.core.domain.HighlighterBuilderUtils.toHighlighterBuilder;

/**
 * @author 章多亮
 * @since 2019/12/17 15:32
 */
@Slf4j
class SearchRequestFactory extends AbstractRequestFactory<Search, SearchRequest> {
    static final SearchRequestFactory INSTANCE = new SearchRequestFactory();
    private static final Map<MethodMeta, QueryBuilderFactory> QUERY_BUILDER_FACTORIES = new ConcurrentHashMap<>();
    private static final List<Class<? extends Annotation>> SEARCH_ANNOTATION_CLASSES;

    static {
        // search request annotation list
        List<Class<? extends Annotation>> annotationClasses = new LinkedList<>();
        annotationClasses.add(Search.class);
        annotationClasses.add(MultiSearch.class);
        annotationClasses.add(SearchScroll.class);
        annotationClasses.add(UpdateByQuery.class);
        annotationClasses.add(DeleteByQuery.class);
        annotationClasses.add(Agg.class);
        annotationClasses.add(Count.class);

        SEARCH_ANNOTATION_CLASSES = Collections.unmodifiableList(annotationClasses);
    }

    private SearchRequestFactory() {
    }

    @Override
    protected void setAnnotationMeta(SearchRequest request, Search search) {
        if (request.source().pointInTimeBuilder() == null) {
            request.preference(StringUtils.trimToNull(search.preference()));
        }
        request.searchType(search.searchType());

        val allowPartialSearchResults = StringUtils.trimToNull(search.allowPartialSearchResults());
        if (allowPartialSearchResults != null) {
            request.allowPartialSearchResults(Boolean.parseBoolean(allowPartialSearchResults));
        }

        val requestCache = StringUtils.trimToNull(search.requestCache());
        if (requestCache != null) {
            request.requestCache(Boolean.parseBoolean(requestCache));
        }

        String keepAlive = StringUtils.trimToNull(search.scrollKeepAlive());
        if (keepAlive != null) {
            request.scroll(keepAlive);
        }

        // source 配置
        val source = request.source();
        source.timeout(TimeValue.parseTimeValue(StringUtils.trimToNull(search.timeout()), "search timeout"));
        if (search.countOnly()) {
            source.fetchSource(false).size(0);
        }
        if (search.trackTotalHits()) {
            source.trackTotalHits(true);
        }
        if (search.minScore() != -Float.MAX_VALUE) {
            source.minScore(search.minScore());
        }
        source.trackScores(search.trackScores()).explain(search.explain()).profile(search.profile());
        for (String storedField : search.storedFields()) {
            source.storedField(storedField);
        }
        for (String docValueField : search.docValueFields()) {
            source.docValueField(docValueField);
        }
    }

    @Override
    protected SearchRequest doCreate(MethodMeta meta, Object[] args) {
        // 目前支持一个入参作为条件查询，所以可以通过多参数变成一个实体类
        // 传过来的只有一个入参条件
        Optional<ParameterMeta> conditionMeta = meta.findConditionParameter();
        Object condition = conditionMeta.map(p -> p.getValue(args)).orElse(null);

        // 1. 如果是一个入参
        SearchRequest request = condition instanceof PointTimeProvider
                ? new SearchRequest()
                : Requests.searchRequest(meta.getIndices(meta, args));

        // 获取语句构建器，不能的查询语句是不一样的
        QueryBuilder queryBuilder;
        if (condition instanceof QueryBuilderProvider) {
            queryBuilder = ((QueryBuilderProvider) condition).queryBuilder();
        } else {
            QueryBuilderFactory factory = getQueryBuilderFactory(meta);
            queryBuilder = factory.create(conditionMeta.orElse(null), condition);
        }

        SearchSourceBuilder searchSource = new SearchSourceBuilder();
        searchSource.query(queryBuilder);

        // 设置分页参数
        meta.getPageableParameter()
                .map(p -> p.<Pageable>getNarrowValue(args))
                .ifPresent(setFromSize(searchSource));

        setSearchSourceProviderMeta(searchSource, meta, condition);

        request.source(searchSource);
        if (condition instanceof RoutingProvider && !(condition instanceof PointTimeProvider)) {
            request.routing(((RoutingProvider) condition).routing());
        }

        return request;
    }

    private Consumer<Pageable> setFromSize(SearchSourceBuilder searchSource) {
        return p -> {
            // 设个上下文是为了创建Page
            ContextHolder.setPageable(p);
            searchSource.from(p.getFrom()).size(p.getSize());
        };
    }

    private QueryBuilderFactory getQueryBuilderFactory(MethodMeta meta) {
        return QUERY_BUILDER_FACTORIES.computeIfAbsent(meta, m -> SEARCH_ANNOTATION_CLASSES.stream()
                .map(meta::findAnnotation)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(this::fromAnnotation)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new));
    }

    private QueryBuilderFactory fromAnnotation(Annotation searchAnnotation) {
        Optional<QueryType> queryType = AnnotationUtils.findAttribute(searchAnnotation, "queryType");
        QueryType type = queryType.orElse(QueryType.AUTO);
        if (!type.isSupported()) {
            throw new ConditionNotSupportException("QueryType " + type + " is not implemented");
        }
        return type.getQueryBuilderFactory();
    }

    private static void setSearchSourceProviderMeta(SearchSourceBuilder searchSource, MethodMeta meta, Object condition) {
        if (condition instanceof ScriptFieldProvider) {
            val fields = ((ScriptFieldProvider) condition).getScriptFields();
            for (ScriptField field : fields) {
                searchSource.scriptField(field.getName(), field.getScript().toEsScript());
            }
        }

        if (condition instanceof SortProvider) {
            val sorts = ((SortProvider) condition).getSorts();
            for (Sort sort : sorts) {
                searchSource.sort(sort.toSortBuilder());
            }
        }

        setFetchSource(searchSource, meta, condition);

        if (condition instanceof CollapseProvider) {
            val collapse = ((CollapseProvider) condition).getCollapse();
            searchSource.collapse(collapse.toCollapseBuilder());
        }
        if (condition instanceof SearchAfterProvider) {
            searchSource.searchAfter(((SearchAfterProvider) condition).sortValues());
        }

        if (condition instanceof PointTimeProvider) {
            PointTimeProvider provider = (PointTimeProvider) condition;
            val builder = new PointInTimeBuilder(provider.pitId());
            String keepAlive = StringUtils.trimToNull(provider.keepAlive());
            if (keepAlive != null) {
                builder.setKeepAlive(keepAlive);
            }
            searchSource.pointInTimeBuilder(builder);
        }

        if (condition instanceof SearchSourceBuilderProvider) {
            ((SearchSourceBuilderProvider) condition).customize(searchSource);
        }

        if (condition instanceof HighlighterProvider) {
            searchSource.highlighter(toHighlighterBuilder(((HighlighterProvider) condition).highlighterBuilder()));
        }
    }

    private static void setFetchSource(SearchSourceBuilder searchSource, MethodMeta meta, Object condition) {
        if (meta.unwrappedReturnType().map(MetaUtils::isBasic).orElse(false)) {
            searchSource.fetchSource(false);
        } else {
            if (condition instanceof SourceProvider) {
                val sourceProvider = (SourceProvider) condition;
                val includeFields = sourceProvider.getIncludeFields();
                if (includeFields == null || includeFields.length == 0) {
                    searchSource.fetchSource(false);
                } else {
                    searchSource.fetchSource(includeFields, sourceProvider.getExcludeFields());
                }
            } else {
                val includeFields = meta.getIncludeFields();
                if (includeFields == null || includeFields.length == 0) {
                    searchSource.fetchSource(false);
                } else {
                    searchSource.fetchSource(includeFields, ArrayUtils.EMPTY_STRING_ARRAY);
                }
            }
        }
    }
}
