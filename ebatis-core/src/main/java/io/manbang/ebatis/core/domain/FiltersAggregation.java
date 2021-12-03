package io.manbang.ebatis.core.domain;

import io.manbang.ebatis.core.builder.QueryBuilderFactory;
import io.manbang.ebatis.core.provider.BuildProvider;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.FiltersAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filter.FiltersAggregator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author weilong.hu
 * @since 2021/4/19 16:54
 */
public class FiltersAggregation implements SubAggregation<FiltersAggregation>, BuildProvider {
    /**
     * 聚合名称
     */
    private String name;
    /**
     * 子聚合
     */
    private List<Aggregation> subAggregations = new ArrayList<>();
    /**
     * filters
     */
    private Map<String, Object> filters = new LinkedHashMap<>();

    public FiltersAggregation(String name) {
        this.name = name;
    }


    public FiltersAggregation filter(String key, Object condition) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(condition);
        filters.put(key, condition);
        return this;
    }

    @Override
    public FiltersAggregation subAgg(Aggregation... aggs) {
        Collections.addAll(subAggregations, aggs);
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T build() {
        List<FiltersAggregator.KeyedFilter> keyedFilters = new ArrayList<>();
        for (Map.Entry<String, Object> entry : filters.entrySet()) {
            keyedFilters.add(new FiltersAggregator.KeyedFilter(entry.getKey(), QueryBuilderFactory.bool().create(null, entry.getValue())));
        }
        final FiltersAggregationBuilder filters = AggregationBuilders.filters(name, keyedFilters.toArray(new FiltersAggregator.KeyedFilter[0]));
        if (!subAggregations.isEmpty()) {
            subAggregations.forEach(subAgg -> {
                        final AggregationBuilder build = ((BuildProvider) subAgg).build();
                        filters.subAggregation(build);
                    }
            );
        }
        return (T) filters;
    }
}
