package com.ymm.ebatis.request;

import com.ymm.ebatis.annotation.Agg;
import com.ymm.ebatis.annotation.Bucket;
import com.ymm.ebatis.annotation.BucketOrder;
import com.ymm.ebatis.common.DslUtils;
import com.ymm.ebatis.meta.MethodMeta;
import com.ymm.ebatis.provider.AggConditionProvider;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.Requests;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author 章多亮
 * @since 2020/1/2 20:20
 */
class BucketAggRequestFactory extends AbstractAggRequestFactory {
    static final BucketAggRequestFactory INSTANCE = new BucketAggRequestFactory();

    private BucketAggRequestFactory() {
    }

    @Override
    protected SearchRequest doCreate(MethodMeta meta, Object[] args) {
        Object condition = DslUtils.getFirstElement(args).orElse(null);

        SearchRequest request = createSearchRequest(meta, condition);

        Agg agg = meta.getAnnotation(Agg.class);
        Bucket bucket = DslUtils.getFirstElementRequired(agg.bucket());

        // 聚合
        AggregationBuilder aggregation = createAggregation(meta, bucket);
        request.source().aggregation(aggregation);

        return request;
    }

    private SearchRequest createSearchRequest(MethodMeta meta, Object condition) {
        SearchRequest request;
        if (condition instanceof AggConditionProvider) {
            request = RequestFactory.search().create(meta, ((AggConditionProvider) condition).getCondition());
        } else if (condition != null) {
            request = RequestFactory.search().create(meta, condition);
        } else {
            request = Requests.searchRequest(meta.getIndices());
        }
        return request;
    }

    private AggregationBuilder createAggregation(MethodMeta meta, Bucket bucket) {
        AggregationBuilder aggregation = bucket.type().aggregate(meta, bucket);
        if (aggregation instanceof TermsAggregationBuilder) {
            ((TermsAggregationBuilder) aggregation).order(Stream.of(bucket.bucketOrders()).map(BucketOrder::order).collect(Collectors.toList()));
            ((TermsAggregationBuilder) aggregation).field(bucket.fieldName());
        }

        return aggregation;
    }
}
