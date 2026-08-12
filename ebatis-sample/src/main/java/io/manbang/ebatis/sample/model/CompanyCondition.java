package io.manbang.ebatis.sample.model;

import io.manbang.ebatis.core.annotation.Must;
import io.manbang.ebatis.core.annotation.QueryType;
import io.manbang.ebatis.core.annotation.Should;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompanyCondition {
    @Must(queryType = QueryType.TERM, boost = 100.0f)
    private Long id;
    @Should(queryType = QueryType.MATCH, boost = 90.0f)
    private String name;
    @Should(queryType = QueryType.MATCH, boost = 90.0f)
    private String maskName;
}
