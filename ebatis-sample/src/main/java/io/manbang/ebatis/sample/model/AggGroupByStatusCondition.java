package io.manbang.ebatis.sample.model;

import io.manbang.ebatis.core.annotation.Filter;
import io.manbang.ebatis.core.annotation.QueryType;
import io.manbang.ebatis.core.domain.Aggregation;
import io.manbang.ebatis.core.provider.AggProvider;
import lombok.Data;

@Data
public class AggGroupByStatusCondition implements AggProvider {
    public static final String AGG_NAME = "GroupByStatus";
    @Filter(queryType = QueryType.BOOL)
    private ManufacturerCondition manufacturer;

    @Override
    public Aggregation[] getAggregations() {
        return new Aggregation[]{Aggregation.terms(AGG_NAME)
                .fieldName("status")};
    }
}
