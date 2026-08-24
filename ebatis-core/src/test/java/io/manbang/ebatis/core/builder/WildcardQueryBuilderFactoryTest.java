package io.manbang.ebatis.core.builder;

import io.manbang.ebatis.core.meta.ConditionMeta;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class WildcardQueryBuilderFactoryTest {
    @Test
    public void shouldAcceptDirectParameterWithoutNestedWildcardAnnotation() {
        WildcardQueryBuilder builder = (WildcardQueryBuilder) QueryBuilderFactory.wildCard()
                .create(new DirectParameterMeta("category"), "Men*");

        assertEquals("category", builder.fieldName());
        assertEquals("Men*", builder.value());
    }

    private static class DirectParameterMeta implements ConditionMeta {
        private final String name;

        private DirectParameterMeta(String name) {
            this.name = name;
        }

        @Override
        public Map<Class<? extends Annotation>, List<io.manbang.ebatis.core.meta.FieldMeta>> getQueryClauses(Object instance) {
            return Collections.emptyMap();
        }

        @Override public Class<?> getType() { return String.class; }
        @Override public boolean isBasic() { return true; }
        @Override public boolean isScript() { return false; }
        @Override public boolean isRange() { return false; }
        @Override public boolean isArray() { return false; }
        @Override public boolean isCollection() { return false; }
        @Override public boolean isArrayOrCollection() { return false; }
        @Override public boolean isBasicArrayOrCollection() { return false; }
        @Override public String getName() { return name; }
        @Override public String getShortName() { return name; }
        @Override public <A extends Annotation> Optional<A> findAttributeAnnotation(Class<A> annotationClass) { return Optional.empty(); }
        @Override public Type getGenericType() { return String.class; }
    }
}
