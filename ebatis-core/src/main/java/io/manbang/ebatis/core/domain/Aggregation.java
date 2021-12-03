package io.manbang.ebatis.core.domain;

/**
 * @author weilong.hu
 */
public interface Aggregation {
    /**
     * Create a new Terms aggregation with the given name.
     *
     * @param name name
     * @return TermsAggregation
     */
    static TermsAggregation terms(String name) {
        return new TermsAggregation(name);
    }

    /**
     * Create a new Filters aggregation with the given name.
     *
     * @param name name
     * @return FiltersAggregation
     */
    static FiltersAggregation filters(String name) {
        return new FiltersAggregation(name);
    }

    /**
     * Create a new Sum aggregation with the given name.
     *
     * @param name name
     * @return SumAggregation
     */
    static SumAggregation sum(String name) {
        return new SumAggregation(name);
    }

    /**
     * Create a new ValueCount aggregation with the given name.
     *
     * @param name name
     * @return CountAggregation
     */
    static CountAggregation count(String name) {
        return new CountAggregation(name);
    }

    /**
     * Create a new avg aggregation with the given name.
     *
     * @param name name
     * @return CountAggregation
     */
    static AvgAggregation avg(String name) {
        return new AvgAggregation(name);
    }

    /**
     * Create a new percentiles aggregation with the given name.
     *
     * @param name name
     * @return PercentilesAggregation
     */
    static PercentilesAggregation percentiles(String name) {
        return new PercentilesAggregation(name);
    }

    /**
     * Create a new PercentileRanks aggregation with the given name.
     *
     * @param name   name
     * @param values values
     * @return PercentileRanksAggregation
     */
    static PercentileRanksAggregation percentileRanks(String name, double... values) {
        return new PercentileRanksAggregation(name, values);
    }

}
