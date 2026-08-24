package io.manbang.ebatis.core.domain;

import io.manbang.ebatis.core.provider.BuildProvider;
import org.elasticsearch.search.aggregations.AggregationBuilder;

/** An Ebatis aggregation backed by a native Elasticsearch builder. */
public final class NativeAggregation implements Aggregation, BuildProvider {
    private final AggregationBuilder builder;

    public NativeAggregation(AggregationBuilder builder) {
        if (builder == null) {
            throw new IllegalArgumentException("aggregation builder must not be null");
        }
        this.builder = builder;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T build() {
        return (T) builder;
    }
}
