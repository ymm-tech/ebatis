package com.ymm.ebatis.core.builder;

import com.ymm.ebatis.core.annotation.Terms;
import com.ymm.ebatis.core.meta.ConditionMeta;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsQueryBuilder;

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
    protected TermsQueryBuilder doCreate(ConditionMeta meta, Object condition) {
        String name = meta.getName();
        if (meta.isArray()) {
            Object[] terms = (Object[]) condition;
            return QueryBuilders.termsQuery(name, terms);
        } else if (meta.isCollection()) {
            Collection<?> terms = (Collection<?>) condition;
            return QueryBuilders.termsQuery(name, terms);
        } else {
            throw new IllegalArgumentException(meta.toString());
        }
    }
}
