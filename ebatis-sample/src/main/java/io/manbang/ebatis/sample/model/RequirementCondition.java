package io.manbang.ebatis.sample.model;

import io.manbang.ebatis.core.annotation.Must;
import io.manbang.ebatis.core.annotation.QueryType;
import io.manbang.ebatis.core.domain.Range;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class RequirementCondition {
    @Must(queryType = QueryType.TERM)
    private Long id;
    @Must(nested = true, queryType = QueryType.BOOL)
    private ModelCondition model;
    @Must(queryType = QueryType.FUZZY, boost = 2.0f)
    private String batchNo;
    private Range<Long> expectedPrice;
    @Must(queryType = QueryType.RANGE)
    private Range<Date> deadline;
    @Must(queryType = QueryType.TERMS)
    private String[] units;
    @Must(nested = true, queryType = QueryType.BOOL)
    private ManufacturerCondition manufacturer;
}
