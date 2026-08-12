package io.manbang.ebatis.sample.condition;

import io.manbang.ebatis.core.annotation.Must;
import io.manbang.ebatis.core.annotation.Order;
import io.manbang.ebatis.core.domain.Aggregation;
import io.manbang.ebatis.core.domain.FiltersAggregation;
import io.manbang.ebatis.core.domain.MaxAggregation;
import io.manbang.ebatis.core.domain.MinAggregation;
import io.manbang.ebatis.core.domain.PercentileRanksAggregation;
import io.manbang.ebatis.core.domain.PercentilesAggregation;
import io.manbang.ebatis.core.domain.TermsAggregation;
import io.manbang.ebatis.core.provider.AggProvider;
import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * @author weilong.hu
 * @since 2020/7/6 22:18
 */
public class RecentOrderAggCondition extends SampleRecentOrderCondition implements AggProvider {
    @Override
    public Aggregation[] getAggregations() {
        TermsAggregation terms1 =
                Aggregation.terms("grpByEventType").fieldName("startCityCode").order(Order.COUNT_DESC, Order.KEY_ASC);
        TermsAggregation terms2 = Aggregation.terms("grpByUserId").fieldName("startProvinceCode").order(Order.COUNT_DESC);
        TermsAggregation terms3 = Aggregation.terms("grpByRoute").fieldName("startAreaCode").order(Order.COUNT_DESC);
        terms1.subAgg(terms2);
        terms1.subAgg(Aggregation.sum("sumTotalAmount").fieldName("totalAmount"));
        terms2.subAgg(terms3);
        final Condition condition = new Condition();
        condition.setCargoType(1);
        final FiltersAggregation filter = Aggregation.filters("type").filter("cargoType", condition);
        final PercentilesAggregation percentiles = Aggregation.percentiles("percentiles").fieldName("updateTime");
        long time = System.currentTimeMillis();
        final PercentileRanksAggregation percentileRanks = Aggregation.percentileRanks("percentileRanks",
                time - TimeUnit.DAYS.toMillis(365 * 2),
                time - TimeUnit.DAYS.toMillis(365),
                time - TimeUnit.DAYS.toMillis(180),
                time - TimeUnit.DAYS.toMillis(90),
                time - TimeUnit.DAYS.toMillis(30),
                time
        ).fieldName("updateTime");
        final MinAggregation minCargoTime = Aggregation.min("minCargoTime").fieldName("cargoTime");
        final MaxAggregation maxCargoTime = Aggregation.max("maxCargoTime").fieldName("cargoTime");
        return new Aggregation[]{terms1, terms2, filter, percentiles, percentileRanks, minCargoTime, maxCargoTime};
    }

    @Data
    public static class Condition {
        @Must
        private Integer cargoType;
    }
}
