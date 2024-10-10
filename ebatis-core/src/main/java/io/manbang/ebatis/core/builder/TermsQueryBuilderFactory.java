package io.manbang.ebatis.core.builder;

import io.manbang.ebatis.core.annotation.Terms;
import io.manbang.ebatis.core.meta.ConditionMeta;
import lombok.val;
import org.apache.commons.lang3.ClassUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsQueryBuilder;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author 章多亮
 * @since 2020/1/7 10:35
 */
class TermsQueryBuilderFactory extends AbstractQueryBuilderFactory<TermsQueryBuilder, Terms> {
    static final TermsQueryBuilderFactory INSTANCE = new TermsQueryBuilderFactory();

    private TermsQueryBuilderFactory() {
    }

    @Override
    protected void setAnnotationMeta(TermsQueryBuilder builder, Terms terms) {
        if (builder != null) {
            builder.boost(terms.boost());
        }
    }

    @Override
    protected TermsQueryBuilder doCreate(ConditionMeta meta, Object condition) {
        String name = meta.getName();
        Collection<?> terms;
        if (meta.isArray()) {
            terms = Arrays.asList((Object[]) condition);
        } else if (meta.isCollection()) {
            terms = (Collection<?>) condition;
        } else {
            throw new IllegalArgumentException(meta.toString());
        }

        if (terms.isEmpty()) {
            return null;
        }

        val componentType = terms.iterator().next().getClass();
        if (ClassUtils.isPrimitiveOrWrapper(componentType)
                || componentType == String.class) {
            return QueryBuilders.termsQuery(name, terms);
        } else if (componentType.isEnum()) {
            val names = terms.stream().map(Object::toString).toArray(String[]::new);
            return QueryBuilders.termsQuery(name, names);
        }

        return QueryBuilders.termsQuery(name, terms);
    }
}
