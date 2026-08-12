package io.manbang.ebatis.sample.model;

import io.manbang.ebatis.core.annotation.Must;
import io.manbang.ebatis.core.annotation.QueryType;
import io.manbang.ebatis.core.domain.Range;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class DictionaryCondition {
    @Must(queryType = QueryType.TERM)
    private Long id;
    @Must(queryType = QueryType.MATCH)
    private String name;
    @Must(queryType = QueryType.FUZZY)
    private String code;
    @Must(queryType = QueryType.TERM)
    private String type;
    @Must(queryType = QueryType.RANGE)
    private Range<Date> updateTime;
    @Must(queryType = QueryType.TERM)
    private Boolean deleted;
}
