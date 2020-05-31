package com.ymm.ebatis.request;

import com.ymm.ebatis.annotation.Agg;
import com.ymm.ebatis.annotation.Metric;
import com.ymm.ebatis.common.DslUtils;
import com.ymm.ebatis.meta.MetaUtils;
import com.ymm.ebatis.meta.MethodMeta;
import com.ymm.ebatis.provider.AggConditionProvider;
import com.ymm.ebatis.provider.MissingProvider;
import com.ymm.ebatis.provider.ScriptProvider;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.support.ValuesSourceAggregationBuilder;

/**
 * @author 章多亮
 * @since 2020/1/2 19:16
 */
class MetricAggRequestFactory extends AbstractAggRequestFactory {
    static final MetricAggRequestFactory INSTANCE = new MetricAggRequestFactory();

    private MetricAggRequestFactory() {
    }

    @Override
    protected void setAnnotationMeta(SearchRequest request, Agg agg) {
        request.routing(DslUtils.getRouting(agg.routing()));
        request.source().fetchSource(agg.fetchSource()).size(agg.size());
    }

    @Override
    protected SearchRequest doCreate(MethodMeta meta, Object[] args) {
        Object condition = meta.getConditionParameter().getValue(args);

        SearchRequest request = createSearchRequest(meta, condition);

        Agg agg = meta.getAnnotation(Agg.class);
        Metric metric = MetaUtils.getFirstElement(agg.metric());

        // 聚合
        AggregationBuilder aggregation = createAggregation(meta, metric, condition);
        request.source().aggregation(aggregation);

        return request;
    }

    private SearchRequest createSearchRequest(MethodMeta meta, Object condition) {
        SearchRequest request;
        if (condition instanceof AggConditionProvider) {
            request = RequestFactory.search().create(meta, ((AggConditionProvider) condition).getCondition());
        } else {
            request = RequestFactory.search().create(meta, condition);
        }
        return request;
    }

    private AggregationBuilder createAggregation(MethodMeta meta, Metric metric, Object condition) {
        AggregationBuilder aggregation = metric.type().aggregate(meta, metric);

        if (aggregation instanceof ValuesSourceAggregationBuilder) {
            ValuesSourceAggregationBuilder<?, ?> sourceAggregation = (ValuesSourceAggregationBuilder<?, ?>) aggregation;
            // 缺失字段处理
            if (condition instanceof MissingProvider) {
                sourceAggregation.missing(((MissingProvider) condition).getMissing());
            }

            if (StringUtils.isNotBlank(metric.format())) {
                sourceAggregation.format(metric.format());
            }

            if (condition instanceof ScriptProvider) {
                sourceAggregation.script(((ScriptProvider) condition).getScript().toEsScript());
            } else {
                // 如果条件是空的，则只能通过字段统计
                sourceAggregation.field(metric.fieldName());
            }
        }
        return aggregation;
    }
}
