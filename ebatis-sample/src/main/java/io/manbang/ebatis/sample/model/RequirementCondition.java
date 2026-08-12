package io.manbang.ebatis.sample.model;

import io.manbang.ebatis.core.annotation.MatchType;
import io.manbang.ebatis.core.annotation.Must;
import io.manbang.ebatis.core.annotation.QueryType;
import io.manbang.ebatis.core.annotation.Wildcard;
import io.manbang.ebatis.core.domain.Range;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class RequirementCondition {
    @Must(queryType = QueryType.TERM)
    private Long id;
    @Must(nested = true, queryType = QueryType.BOOL)
    private ModelCondition model;
    @Must(queryType = QueryType.WILDCARD, boost = 2.0f, wildcard = @Wildcard(matchType = MatchType.ENDS_WITH))
    private String batchNo;
    private Range<Long> expectedPrice;
    @Must(queryType = QueryType.RANGE)
    private Range<LocalDate> deadline;
    private Range<LocalDateTime> createTime;
    private Range<LocalDateTime> updateTime;
    @Must(queryType = QueryType.TERMS)
    private String[] units;
    @Must(nested = true, queryType = QueryType.BOOL)
    private ManufacturerCondition manufacturer;

}
