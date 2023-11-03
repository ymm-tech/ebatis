package io.manbang.ebatis.sample.model;

import io.manbang.ebatis.core.annotation.Must;
import io.manbang.ebatis.core.annotation.QueryType;
import io.manbang.ebatis.core.domain.Range;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ProductCondition {
    @Must(queryType = QueryType.TERM, boost = 100.0f)
    private Long id;
    @Must(queryType = QueryType.MATCH, boost = 90.0f)
    private String name;
    private Range<Date> createTime;
    private Range<Date> updateTime;
    private Range<Long> highestPrice;
    private Range<Long> lowestPrice;
    private Range<Long> preferredPrice;
    private Range<Long> quantity;
    private Range<Long> miniPackQuantity;
    @Must(nested = true, queryType = QueryType.BOOL)
    private ModelCondition model;
    @Must(nested = true, queryType = QueryType.BOOL)
    private CompanyCondition supplier;
    private Boolean spot;
    private String batchNo;
}
