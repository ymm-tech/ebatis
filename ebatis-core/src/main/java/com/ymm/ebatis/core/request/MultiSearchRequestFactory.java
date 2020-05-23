package com.ymm.ebatis.core.request;

import com.ymm.ebatis.core.annotation.MultiSearch;
import com.ymm.ebatis.core.common.DslUtils;
import com.ymm.ebatis.core.domain.ContextHolder;
import com.ymm.ebatis.core.domain.Pageable;
import com.ymm.ebatis.core.meta.MethodMeta;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.lang.reflect.Parameter;
import java.util.Collection;

/**
 * @author 章多亮
 * @since 2020/1/14 15:58
 */
public class MultiSearchRequestFactory extends AbstractRequestFactory<MultiSearch, MultiSearchRequest> {
    public static final MultiSearchRequestFactory INSTANCE = new MultiSearchRequestFactory();

    private MultiSearchRequestFactory() {
    }

    @Override
    protected void setOptionalMeta(MultiSearchRequest request, MultiSearch multiSearch) {
        int maxConcurrentSearchRequests = multiSearch.maxConcurrentSearchRequests();
        if (maxConcurrentSearchRequests >= 1) {
            request.maxConcurrentSearchRequests(maxConcurrentSearchRequests);
        }

        for (SearchRequest searchRequest : request.requests()) {
            SearchSourceBuilder source = searchRequest.source();
            if (multiSearch.countOnly()) {
                source.fetchSource(false).size(0);
            }

            searchRequest.routing(DslUtils.getRouting(multiSearch.routing()))
                    .searchType(multiSearch.searchType())
                    .preference(StringUtils.trimToNull(multiSearch.preference()));
            searchRequest.setPreFilterShardSize(multiSearch.preFilterShardSize());
            searchRequest.setBatchedReduceSize(multiSearch.batchedReduceSize());
        }
    }

    @Override
    protected MultiSearchRequest doCreate(MethodMeta meta, Object... args) {
        Parameter parameter = meta.getFirstParameter();
        Object arg = args[0];

        Object[] conditions;
        if (Collection.class.isAssignableFrom(parameter.getType())) {
            Collection<?> collection = (Collection<?>) arg;
            conditions = collection.toArray();
        } else if (parameter.getType().isArray()) {
            conditions = (Object[]) arg;
        } else {
            conditions = new Object[]{arg};
        }
        Pageable[] pageables = new Pageable[conditions.length];
        for (int i = 0; i < conditions.length; i++) {
            if(conditions[i] instanceof Pageable){
                pageables[i]= (Pageable) conditions[i];
            }
        }
        ContextHolder.setPageables(pageables);

        MultiSearchRequest request = new MultiSearchRequest();
        for (int i = 0; i < conditions.length; i++) {
            ContextHolder.setPageable(pageables[i]);
            request.add(SearchRequestFactory.INSTANCE.create(meta, conditions[i]));
        }

        return request;
    }
}
