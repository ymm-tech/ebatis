package com.ymm.ebatis.request;

import com.ymm.ebatis.annotation.Agg;
import com.ymm.ebatis.meta.MethodMeta;
import org.elasticsearch.action.search.SearchRequest;

/**
 * 聚合请求工厂，不同的聚合请求会被交给具体的聚合请求工厂去创建
 *
 * @author 章多亮
 * @since 2020/1/3 11:31
 */
class AggRequestFactory extends AbstractAggRequestFactory {
    public static final AggRequestFactory INSTANCE = new AggRequestFactory();

    private AggRequestFactory() {
    }

    @Override
    protected SearchRequest doCreate(MethodMeta meta, Object[] args) {
        Agg agg = meta.getRequestAnnotation();
        return agg.type().getRequestFactory().create(meta, args);
    }
}
