package com.ymm.ebatis.core.request;

import com.ymm.ebatis.core.annotation.Agg;
import com.ymm.ebatis.core.annotation.Metric;
import com.ymm.ebatis.core.common.DslUtils;
import com.ymm.ebatis.core.domain.AggConditionProvider;
import com.ymm.ebatis.core.domain.MissingProvider;
import com.ymm.ebatis.core.domain.ScriptProvider;
import com.ymm.ebatis.core.meta.MethodMeta;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.support.ValuesSourceAggregationBuilder;

import java.util.Optional;

/**
 * @author 章多亮
 * @since 2020/1/2 19:16
 */
public class MetricAggRequestFactory extends AbstractAggRequestFactory {
    public static final MetricAggRequestFactory INSTANCE = new MetricAggRequestFactory();

    private MetricAggRequestFactory() {
    }

    @Override
    protected void setOptionalMeta(SearchRequest request, Agg agg) {
        request.routing(DslUtils.getRouting(agg.routing()));
        request.source().fetchSource(agg.fetchSource()).size(agg.size());
    }

    @Override
    protected SearchRequest doCreate(MethodMeta meta, Object[] args) {
        Object condition = DslUtils.getFirstElement(args).orElse(null);

        SearchRequest request = createSearchRequest(meta, condition);

        Agg agg = meta.getAnnotationRequired(Agg.class);
        Metric metric = DslUtils.getFirstElementRequired(agg.metric());

        // 聚合
        AggregationBuilder aggregation = createAggregation(meta, metric, condition);
        request.source().aggregation(aggregation);

        return request;
    }

    private SearchRequest createSearchRequest(MethodMeta meta, Object condition) {
        SearchRequest request;
        if (condition instanceof AggConditionProvider) {
            request = SearchRequestFactory.INSTANCE.create(meta, ((AggConditionProvider) condition).getCondition());
        } else if (condition != null) {
            request = SearchRequestFactory.INSTANCE.create(meta, condition);
        } else {
            request = new SearchRequest(ArrayUtils.EMPTY_STRING_ARRAY);
        }
        return request;
    }

    private AggregationBuilder createAggregation(MethodMeta meta, Metric metric, Object condition) {
        AggregationBuilder aggregation = metric.type().aggregate(meta, metric);

        if (aggregation instanceof ValuesSourceAggregationBuilder) {
            ValuesSourceAggregationBuilder<?, ?> sourceAggregation = (ValuesSourceAggregationBuilder<?, ?>) aggregation;
            // 如果是空的，
            Optional.ofNullable(condition)
                    .map(BeanDescriptor::of)
                    .flatMap(BeanDescriptor::getMissing)
                    .ifPresent(m -> sourceAggregation.missing(m.getValue(condition)));

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
                sourceAggregation.field(getFieldName(metric));
            }
        }
        return aggregation;
    }

    private String getFieldName(Metric metric) {
        String name = metric.fieldName();
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("metric字段聚合运算，字段名称不能为空");
        }
        return name;
    }

}
