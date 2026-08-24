package io.manbang.ebatis.core.provider;

import org.elasticsearch.search.aggregations.AggregationBuilder;

import java.util.Collection;
import java.util.Collections;

/** Allows mapper conditions to provide native Elasticsearch aggregations. */
public interface AggregationBuilderProvider extends Provider {
    default Collection<AggregationBuilder> aggregationBuilders() {
        AggregationBuilder aggregation = aggregationBuilder();
        return aggregation == null ? Collections.emptyList() : Collections.singletonList(aggregation);
    }

    default AggregationBuilder aggregationBuilder() {
        return null;
    }
}
