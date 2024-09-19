package io.manbang.ebatis.sample.condition;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.manbang.ebatis.core.annotation.Field;
import io.manbang.ebatis.core.annotation.MatchBoolPrefix;
import io.manbang.ebatis.core.annotation.Must;
import io.manbang.ebatis.core.annotation.QueryType;
import io.manbang.ebatis.sample.entity.Scene;
import io.manbang.ebatis.sample.entity.Type;
import lombok.Data;

import java.util.List;

@Data
public class SummaryCondition {
    @Must(queryType = QueryType.MATCH_BOOL_PREFIX,
            matchBoolPrefix = @MatchBoolPrefix(prefixLength = 2))
    private String name;
    @Must(queryType = QueryType.TERM)
    private Scene scene;
    @Field("type")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Must(queryType = QueryType.TERMS)
    private List<Type> types;
    private Long value;
}
