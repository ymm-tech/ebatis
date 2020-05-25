package com.ymm.ebatis.request;

import com.ymm.ebatis.annotation.Agg;
import com.ymm.ebatis.common.DslUtils;
import com.ymm.ebatis.meta.MethodMeta;
import org.elasticsearch.action.search.SearchRequest;

/**
 * @author 章多亮
 * @since 2020/1/2 19:14
 */
public abstract class AbstractAggRequestFactory extends AbstractRequestFactory<Agg, SearchRequest> {

    @Override
    protected void setOptionalMeta(SearchRequest request, Agg agg) {
        request.routing(DslUtils.getRouting(agg.routing()));
    }

    @Override
    protected SearchRequest doCreate(MethodMeta meta, Object[] args) {
        return new SearchRequest();
    }
}
