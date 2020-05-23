package com.ymm.ebatis.core.request;

import com.ymm.ebatis.core.annotation.Agg;
import com.ymm.ebatis.core.annotation.Bucket;
import com.ymm.ebatis.core.annotation.BucketOrder;
import com.ymm.ebatis.core.common.DslUtils;
import com.ymm.ebatis.core.domain.AggConditionProvider;
import com.ymm.ebatis.core.meta.MethodMeta;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author 章多亮
 * @since 2020/1/2 20:20
 */
public class BucketAggRequestFactory extends AbstractAggRequestFactory {
    public static final BucketAggRequestFactory INSTANCE = new BucketAggRequestFactory();

    private BucketAggRequestFactory() {
    }

    @Override
    protected SearchRequest doCreate(MethodMeta meta, Object[] args) {
        Object condition = DslUtils.getFirstElement(args).orElse(null);

        SearchRequest request = createSearchRequest(meta, condition);

        Agg agg = meta.getAnnotationRequired(Agg.class);
        Bucket bucket = DslUtils.getFirstElementRequired(agg.bucket());

        // 聚合
        AggregationBuilder aggregation = createAggregation(meta, bucket, condition);
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

    private AggregationBuilder createAggregation(MethodMeta meta, Bucket bucket, Object condition) {
        AggregationBuilder aggregation = bucket.type().aggregate(meta, bucket);
        if (aggregation instanceof TermsAggregationBuilder) {
            if (ArrayUtils.isNotEmpty(bucket.BucketOrders())) {
                ((TermsAggregationBuilder) aggregation).order(Stream.of(bucket.BucketOrders()).map(BucketOrder::bucketOrder).collect(Collectors.toList()));
                ((TermsAggregationBuilder) aggregation).field(getFieldName(bucket));
            }
        }

        return aggregation;
    }

    private String getFieldName(Bucket bucket) {
        String name = bucket.fieldName();
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("bucket字段运算，字段名称不能为空");
        }
        return name;
    }

}
