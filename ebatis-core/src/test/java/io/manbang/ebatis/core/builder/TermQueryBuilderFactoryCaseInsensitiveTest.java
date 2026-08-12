package io.manbang.ebatis.core.builder;

import io.manbang.ebatis.core.annotation.Field;
import io.manbang.ebatis.core.annotation.QueryType;
import io.manbang.ebatis.core.annotation.Should;
import io.manbang.ebatis.core.annotation.Term;
import io.manbang.ebatis.core.meta.FieldMeta;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TermQueryBuilderFactoryCaseInsensitiveTest {
    @Test
    public void shouldApplyCaseInsensitiveAndBoostFromAnnotation() throws NoSuchFieldException {
        FieldMeta meta = FieldMeta.of(TermCondition.class.getDeclaredField("name"));

        TermQueryBuilder builder = (TermQueryBuilder) QueryBuilderFactory.term().create(meta, "HC32L196KCTA");

        assertEquals("name", builder.fieldName());
        assertEquals("HC32L196KCTA", builder.value());
        assertFalse(builder.caseInsensitive());
        assertEquals(7.0f, builder.boost(), 0.0f);
    }

    @Test
    public void shouldKeepCaseInsensitiveForStringValue() throws NoSuchFieldException {
        FieldMeta meta = FieldMeta.of(DefaultTermCondition.class.getDeclaredField("name"));

        TermQueryBuilder builder = (TermQueryBuilder) QueryBuilderFactory.term().create(meta, "HC32L196KCTA");

        assertTrue(builder.caseInsensitive());
    }

    @Test
    public void shouldDisableCaseInsensitiveForNonStringValue() throws NoSuchFieldException {
        FieldMeta meta = FieldMeta.of(DefaultTermCondition.class.getDeclaredField("id"));

        TermQueryBuilder builder = (TermQueryBuilder) QueryBuilderFactory.term().create(meta, 42L);

        assertEquals(42L, builder.value());
        assertFalse(builder.caseInsensitive());
        assertFalse(builder.toString().contains("case_insensitive"));
    }

    public static class TermCondition {
        @Field("name")
        @Should(queryType = QueryType.TERM,
                term = @Term(caseInsensitive = false, boost = 7.0f))
        private String name;

        public String getName() {
            return name;
        }
    }

    public static class DefaultTermCondition {
        @Field("name")
        @Should(queryType = QueryType.TERM, term = @Term)
        private String name;

        @Field("id")
        @Should(queryType = QueryType.TERM, term = @Term)
        private Long id;

        public String getName() {
            return name;
        }

        public Long getId() {
            return id;
        }
    }
}
