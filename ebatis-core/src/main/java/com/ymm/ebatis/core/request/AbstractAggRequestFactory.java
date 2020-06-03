package com.ymm.ebatis.core.request;

import com.ymm.ebatis.core.annotation.Agg;
import com.ymm.ebatis.core.meta.MethodMeta;
import org.elasticsearch.action.search.SearchRequest;

/**
 * @author 章多亮
 * @since 2020/1/2 19:14
 */
public abstract class AbstractAggRequestFactory extends AbstractRequestFactory<Agg, SearchRequest> {
    @Override
    protected void setAnnotationMeta(SearchRequest request, Agg agg) {
        // do nothing
    }

    @Override
    protected SearchRequest doCreate(MethodMeta meta, Object[] args) {
        return new SearchRequest(meta.getIndices());
    }
}
