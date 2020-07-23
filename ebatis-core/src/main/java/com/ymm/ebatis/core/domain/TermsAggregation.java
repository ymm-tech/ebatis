package com.ymm.ebatis.core.domain;

import com.ymm.ebatis.core.annotation.Order;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author weilong.hu
 * @since 2020/7/6 16:46
 */
public class TermsAggregation implements SubAggregation<TermsAggregation> {
    /**
     * 聚合名称
     */
    private String name;

    /**
     * 聚合字段名称
     */
    private String fieldName;

    /**
     * 脚本
     */
    private Script script;

    /**
     * 桶聚合顺序
     */
    private List<Order> orders = new ArrayList<>();

    /**
     * 返回多少桶聚合结果
     */
    private int size = 10;

    /**
     * 子聚合
     */
    private List<Aggregation> subAggregations = new ArrayList<>();

    private Map<String, Object> metaData;

    private int shardSize = -1;

    private boolean showTermDocCountError = false;

    private long minDocCount = 1;

    private long shardMinDocCount = 0;

    TermsAggregation(String name) {
        this.name = name;
    }

    public Script getScript() {
        return script;
    }

    public TermsAggregation script(Script script) {
        this.script = script;
        return this;
    }

    public String getName() {
        return name;
    }

    public TermsAggregation name(String name) {
        this.name = name;
        return this;
    }

    public String getFieldName() {
        return fieldName;
    }

    public TermsAggregation fieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public int getSize() {
        return size;
    }

    public TermsAggregation size(int size) {
        this.size = size;
        return this;
    }

    public List<Aggregation> getSubAggregations() {
        return subAggregations;
    }

    public int getShardSize() {
        return shardSize;
    }

    public TermsAggregation shardSize(int shardSize) {
        this.shardSize = shardSize;
        return this;
    }

    public boolean isShowTermDocCountError() {
        return showTermDocCountError;
    }

    public TermsAggregation showTermDocCountError(boolean showTermDocCountError) {
        this.showTermDocCountError = showTermDocCountError;
        return this;
    }

    public long getMinDocCount() {
        return minDocCount;
    }

    public TermsAggregation minDocCount(long minDocCount) {
        this.minDocCount = minDocCount;
        return this;
    }

    public long getShardMinDocCount() {
        return shardMinDocCount;
    }

    public TermsAggregation shardMinDocCount(long shardMinDocCount) {
        this.shardMinDocCount = shardMinDocCount;
        return this;
    }

    public TermsAggregation order(Order... order) {
        Collections.addAll(orders, order);
        return this;
    }

    @Override
    public TermsAggregation subAgg(Aggregation... aggs) {
        Collections.addAll(subAggregations, aggs);
        return this;
    }

    public Map<String, Object> getMetaData() {
        return metaData;
    }

    public TermsAggregation metaData(Map<String, Object> metaData) {
        this.metaData = metaData;
        return this;
    }

    @Override
    public AggregationBuilder toAggBuilder() {
        TermsAggregationBuilder agg = AggregationBuilders.terms(name)
                .size(size)
                .showTermDocCountError(showTermDocCountError)
                .minDocCount(minDocCount)
                .shardMinDocCount(shardMinDocCount);

        if (Objects.nonNull(fieldName)) {
            agg.field(fieldName);
        }

        if (Objects.nonNull(script)) {
            agg.script(script.toEsScript());
        }

        if (shardSize != -1) {
            agg.shardSize(shardSize);
        }

        if (!orders.isEmpty()) {
            orders.forEach(order -> agg.order(order.order()));
        }
        if (!subAggregations.isEmpty()) {
            subAggregations.forEach(subAgg -> agg.subAggregation(subAgg.toAggBuilder()));
        }

        if (Objects.nonNull(metaData)) {
            agg.setMetaData(metaData);
        }
        return agg;
    }
}
