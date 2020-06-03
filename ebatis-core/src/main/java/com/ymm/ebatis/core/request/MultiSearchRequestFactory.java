package com.ymm.ebatis.core.request;

import com.ymm.ebatis.core.annotation.MultiSearch;
import com.ymm.ebatis.core.domain.ContextHolder;
import com.ymm.ebatis.core.domain.Pageable;
import com.ymm.ebatis.core.meta.MethodMeta;
import com.ymm.ebatis.core.meta.ParameterMeta;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.Collection;

/**
 * @author 章多亮
 * @since 2020/1/14 15:58
 */
class MultiSearchRequestFactory extends AbstractRequestFactory<MultiSearch, MultiSearchRequest> {
    static final MultiSearchRequestFactory INSTANCE = new MultiSearchRequestFactory();

    private MultiSearchRequestFactory() {
    }

    @Override
    protected void setAnnotationMeta(MultiSearchRequest request, MultiSearch multiSearch) {
        int maxConcurrentSearchRequests = multiSearch.maxConcurrentSearchRequests();
        if (maxConcurrentSearchRequests >= 1) {
            request.maxConcurrentSearchRequests(maxConcurrentSearchRequests);
        }

        for (SearchRequest searchRequest : request.requests()) {
            SearchSourceBuilder source = searchRequest.source();
            if (multiSearch.countOnly()) {
                source.fetchSource(false).size(0);
            }

            searchRequest
                    .searchType(multiSearch.searchType())
                    .preference(StringUtils.trimToNull(multiSearch.preference()));

            searchRequest.setPreFilterShardSize(multiSearch.preFilterShardSize());
            searchRequest.setBatchedReduceSize(multiSearch.batchedReduceSize());
        }
    }

    @Override
    protected MultiSearchRequest doCreate(MethodMeta meta, Object[] args) {
        ParameterMeta parameterMeta = meta.getConditionParameter();
        Object arg = parameterMeta.getValue(args);


        Object[] conditions;
        if (parameterMeta.isCollection()) {
            Collection<?> collection = (Collection<?>) arg;
            conditions = collection.toArray();
        } else if (parameterMeta.isArray()) {
            conditions = (Object[]) arg;
        } else {
            conditions = new Object[]{arg};
        }

        Pageable[] pageable = meta.getPageableParameter()
                .map(p -> p.getValue(args))
                .map(Pageable[].class::cast)
                .orElse(new Pageable[conditions.length]);

        ContextHolder.setPageables(pageable);

        MultiSearchRequest request = new MultiSearchRequest();
        for (int i = 0; i < conditions.length; i++) {
            ContextHolder.setPageable(pageable[i]);
            request.add(SearchRequestFactory.INSTANCE.create(meta, conditions[i]));
        }

        return request;
    }
}
