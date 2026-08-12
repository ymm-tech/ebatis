package io.manbang.ebatis.core.builder;

import io.manbang.ebatis.core.annotation.Terms;
import io.manbang.ebatis.core.meta.ConditionMeta;
import lombok.val;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsQueryBuilder;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

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
        builder.boost(terms.boost());
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

        Object termValue = terms.iterator().next();
        if (termValue instanceof Enum) {
            val names = terms.stream()
                    .map(Enum.class::cast)
                    .map(Enum::name)
                    .collect(Collectors.toList());

            return QueryBuilders.termsQuery(name, names);
        }

        return QueryBuilders.termsQuery(name, terms);
    }
}
