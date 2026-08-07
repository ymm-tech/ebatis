package io.manbang.ebatis.core.builder;

import io.manbang.ebatis.core.annotation.Field;
import io.manbang.ebatis.core.annotation.Prefix;
import io.manbang.ebatis.core.annotation.QueryType;
import io.manbang.ebatis.core.annotation.Should;
import io.manbang.ebatis.core.annotation.Term;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ShouldQueryContractTest {
    @Test
    public void shouldKeepAllClausesAndMinimumShouldMatch() {
        ModelCondition condition = new ModelCondition("HC32L196KCTA", "HC32L196KCTA");

        BoolQueryBuilder builder = (BoolQueryBuilder) QueryBuilderFactory.bool().create(null, condition);

        assertEquals("1", builder.minimumShouldMatch());
        assertEquals(2, builder.should().size());
        assertTrue(builder.toString().contains("\"boost\" : 1000.0"));
        assertTrue(builder.toString().contains("\"boost\" : 500.0"));
    }

    public static class ModelCondition {
        @Field("name")
        @Should(queryType = QueryType.TERM, minimumShouldMatch = "1",
                term = @Term(caseInsensitive = true, boost = 1000.0f))
        private final String exact;

        @Field("name")
        @Should(queryType = QueryType.PREFIX,
                prefix = @Prefix(caseInsensitive = true, boost = 500.0f))
        private final String prefix;

        private ModelCondition(String exact, String prefix) {
            this.exact = exact;
            this.prefix = prefix;
        }

        public String getExact() {
            return exact;
        }

        public String getPrefix() {
            return prefix;
        }
    }
}
