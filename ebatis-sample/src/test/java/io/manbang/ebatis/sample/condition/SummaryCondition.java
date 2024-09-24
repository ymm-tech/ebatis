package io.manbang.ebatis.sample.condition;

import io.manbang.ebatis.core.annotation.Field;
import io.manbang.ebatis.core.annotation.Must;
import io.manbang.ebatis.core.annotation.MustNot;
import io.manbang.ebatis.core.annotation.QueryType;
import io.manbang.ebatis.sample.entity.Scene;
import io.manbang.ebatis.sample.entity.Type;
import lombok.Data;

import java.util.List;

@Data
public class SummaryCondition {
    @Must(queryType = QueryType.WILDCARD)
    private String name;
    @MustNot(queryType = QueryType.TERM)
    private Scene scene;
    @Field("type")
    @Must(queryType = QueryType.TERMS)
    private List<Type> types;
    private Long value;
    @Must(nested = true)
    private ModelCondition model;

    @Data
    public static class ModelCondition {
        @Must(queryType = QueryType.TERM)
        private Long id;
        @Must(queryType = QueryType.WILDCARD)
        private String name;
        @Must(nested = true)
        private Brand brand;
    }

    @Data
    public static class Brand {
        @MustNot
        private Long id;
        @Must(queryType = QueryType.WILDCARD)
        private String name;

        @Must(nested = true)
        private Branch branch;
    }

    @Data
    public static class Branch {
        private Long id;
        private String name;
    }
}
