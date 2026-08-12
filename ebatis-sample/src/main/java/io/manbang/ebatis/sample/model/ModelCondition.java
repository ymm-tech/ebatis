package io.manbang.ebatis.sample.model;

import io.manbang.ebatis.core.annotation.Field;
import io.manbang.ebatis.core.annotation.Must;
import io.manbang.ebatis.core.annotation.QueryType;
import io.manbang.ebatis.core.annotation.Should;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModelCondition {
    @Must(queryType = QueryType.TERM, boost = 100)
    private Long id;

    @Field("name")
    @Should(queryType = QueryType.TERM, boost = 90)
    private String termName;
    @Field("name")
    @Should(queryType = QueryType.FUZZY, boost = 80)
    private String fuzzyName;
    @Field("name")
    @Should(queryType = QueryType.WILDCARD, boost = 80)
    private String wildcardName;

}
