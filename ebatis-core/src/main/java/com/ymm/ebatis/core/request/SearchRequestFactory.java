package com.ymm.ebatis.core.request;

import com.ymm.ebatis.core.annotation.MultiSearch;
import com.ymm.ebatis.core.annotation.QueryType;
import com.ymm.ebatis.core.annotation.Search;
import com.ymm.ebatis.core.builder.QueryBuilderFactory;
import com.ymm.ebatis.core.common.DslUtils;
import com.ymm.ebatis.core.domain.Collapse;
import com.ymm.ebatis.core.domain.ContextHolder;
import com.ymm.ebatis.core.domain.Pageable;
import com.ymm.ebatis.core.domain.ScriptField;
import com.ymm.ebatis.core.domain.Sort;
import com.ymm.ebatis.core.meta.MethodMeta;
import com.ymm.ebatis.core.meta.ParameterMeta;
import com.ymm.ebatis.core.provider.CollapseProvider;
import com.ymm.ebatis.core.provider.ScriptFieldProvider;
import com.ymm.ebatis.core.provider.SortProvider;
import com.ymm.ebatis.core.provider.SourceProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.Requests;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 章多亮
 * @since 2019/12/17 15:32
 */
@Slf4j
class SearchRequestFactory extends AbstractRequestFactory<Search, SearchRequest> {
    static final SearchRequestFactory INSTANCE = new SearchRequestFactory();
    private static final Map<MethodMeta, QueryBuilderFactory> QUERY_BUILDER_FACTORIES = new ConcurrentHashMap<>();

    private SearchRequestFactory() {
    }

    @Override
    protected void setAnnotationMeta(SearchRequest request, Search search) {
        request.routing(DslUtils.getRouting(search.routing()))
                .preference(StringUtils.trimToNull(search.preference()))
                .searchType(search.searchType());
    }

    @Override
    protected SearchRequest doCreate(MethodMeta meta, Object[] args) {
        // 目前支持一个入参作为条件查询，所以可以通过多参数变成一个实体类
        // 传过来的只有一个入参条件
        Optional<ParameterMeta> conditionMeta = meta.findConditionParameter();
        Object condition = conditionMeta.map(p -> p.getValue(args)).orElse(null);

        // 1. 如果是一个入参
        SearchRequest request = Requests.searchRequest(meta.getIndices());
        setTypesIfNecessary(meta, request::types);

        // 获取语句构建器，不能的查询语句是不一样的
        QueryBuilderFactory factory = getQueryBuilderFactory(meta);

        // 创建查询语句
        QueryBuilder queryBuilder = factory.create(conditionMeta.orElse(null), condition);

        SearchSourceBuilder searchSource = new SearchSourceBuilder();
        searchSource.query(queryBuilder);

        // 设置分页参数
        meta.getPageableParameter()
                .map(p -> p.getValue(args))
                .map(Pageable.class::cast)
                .ifPresent(p -> {
                    // 设个上下文是为了创建Page
                    ContextHolder.setPageable(p);
                    searchSource.from(p.getFrom()).size(p.getSize());
                });

        setProviderMeta(condition, searchSource);

        request.source(searchSource);

        return request;
    }

    private QueryBuilderFactory getQueryBuilderFactory(MethodMeta meta) {
        return QUERY_BUILDER_FACTORIES.computeIfAbsent(meta, m -> m.findAnnotation(Search.class).map(Search::queryType)
                .orElseGet(() -> m.findAnnotation(MultiSearch.class).map(MultiSearch::queryType).orElse(QueryType.AUTO))
                .getQueryBuilderFactory());
    }

    private void setProviderMeta(Object condition, SearchSourceBuilder searchSource) {
        if (condition instanceof ScriptFieldProvider) {
            ScriptField[] fields = ((ScriptFieldProvider) condition).getScriptFields();
            for (ScriptField field : fields) {
                searchSource.scriptField(field.getName(), field.getScript().toEsScript());
            }
        }

        if (condition instanceof SortProvider) {
            Sort[] sorts = ((SortProvider) condition).getSorts();
            for (Sort sort : sorts) {
                searchSource.sort(sort.toSortBuilder());
            }
        }

        if (condition instanceof SourceProvider) {
            SourceProvider sourceProvider = (SourceProvider) condition;
            searchSource.fetchSource(sourceProvider.getIncludeFields(), sourceProvider.getExcludeFields());
        }

        if (condition instanceof CollapseProvider) {
            Collapse collapse = ((CollapseProvider) condition).getCollapse();
            searchSource.collapse(collapse.toCollapseBuilder());
        }
    }
}
