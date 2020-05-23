package com.ymm.ebatis.core.request;

import com.ymm.ebatis.core.annotation.MultiSearch;
import com.ymm.ebatis.core.annotation.QueryType;
import com.ymm.ebatis.core.annotation.Search;
import com.ymm.ebatis.core.builder.QueryBuilderFactory;
import com.ymm.ebatis.core.common.DslUtils;
import com.ymm.ebatis.core.domain.CollapseProvider;
import com.ymm.ebatis.core.domain.ContextHolder;
import com.ymm.ebatis.core.domain.ScriptField;
import com.ymm.ebatis.core.domain.ScriptFieldProvider;
import com.ymm.ebatis.core.domain.Sort;
import com.ymm.ebatis.core.domain.SortProvider;
import com.ymm.ebatis.core.domain.SourceProvider;
import com.ymm.ebatis.core.meta.MethodMeta;
import com.ymm.ebatis.core.meta.ParameterConditionMeta;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.Objects;

/**
 * @author 章多亮
 * @since 2019/12/17 15:32
 */
@Slf4j
public class SearchRequestFactory extends AbstractRequestFactory<Search, SearchRequest> {
    public static final RequestFactory INSTANCE = new SearchRequestFactory();

    private SearchRequestFactory() {
    }

    @Override
    protected void setOptionalMeta(SearchRequest request, Search search) {
        request.routing(DslUtils.getRouting(search.routing()))
                .preference(StringUtils.trimToNull(search.preference()))
                .searchType(search.searchType());
    }

    @Override
    protected SearchRequest doCreate(MethodMeta meta, Object[] args) {
        // 目前支持一个入参作为条件查询，所以可以通过多参数变成一个实体类
        // 传过来的只有一个入参条件
        Object condition = args[0];

        // 1. 如果是一个入参
        SearchRequest request = new SearchRequest();

        // 获取语句构建器，不能的查询了语句是不一样的
        QueryBuilderFactory factory = meta.getAnnotation(Search.class).map(Search::queryType)
                .orElseGet(() -> meta.getAnnotation(MultiSearch.class).map(MultiSearch::queryType).orElse(QueryType.AUTO))
                .getQueryBuilderFactory();


        // 创建查询语句
        ParameterConditionMeta conditionMeta = meta.getFirstParameterCondition();
        QueryBuilder queryBuilder = factory.create(conditionMeta, condition);

        SearchSourceBuilder searchSource = new SearchSourceBuilder();
        searchSource.query(queryBuilder);

        ContextHolder.getContext().getPageable().ifPresent(p -> searchSource.from(p.getFrom()).size(p.getSize()));

        additionalProvider(condition, searchSource);

        request.source(searchSource);

        return request;
    }

    private void additionalProvider(Object condition, SearchSourceBuilder searchSource) {
        if (condition instanceof ScriptFieldProvider && ArrayUtils.isNotEmpty(((ScriptFieldProvider) condition).getScriptFields())) {
            ScriptField[] fields = ((ScriptFieldProvider) condition).getScriptFields();
            for (ScriptField field : fields) {
                searchSource.scriptField(field.getName(), field.getScript().toEsScript());
            }
        }

        if (condition instanceof SortProvider && ArrayUtils.isNotEmpty(((SortProvider) condition).getSorts())) {
            Sort[] sorts = ((SortProvider) condition).getSorts();
            for (Sort sort : sorts) {
                searchSource.sort(sort.toSortBuilder());
            }
        }

        if (condition instanceof SourceProvider) {
            SourceProvider sourceProvider = (SourceProvider) condition;
            searchSource.fetchSource(sourceProvider.getIncludeFields(), sourceProvider.getExcludeFields());
        }

        if (condition instanceof CollapseProvider && Objects.nonNull(((CollapseProvider) condition).getCollapse())) {
            CollapseProvider collapseProvider = (CollapseProvider) condition;
            searchSource.collapse(collapseProvider.getCollapse().toCollapseBuilder());
        }
    }
}
