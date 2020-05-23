package com.ymm.ebatis.core.builder;

import com.ymm.ebatis.core.annotation.Terms;
import com.ymm.ebatis.core.common.DslUtils;
import com.ymm.ebatis.core.meta.ConditionMeta;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsQueryBuilder;

import java.util.Collection;

/**
 * @author 章多亮
 * @since 2020/1/7 10:35
 */
public class TermsQueryBuilderFactory extends AbstractQueryBuilderFactory<TermsQueryBuilder, Terms> {
    public static final TermsQueryBuilderFactory INSTANCE = new TermsQueryBuilderFactory();

    private TermsQueryBuilderFactory() {
    }

    @Override
    protected TermsQueryBuilder doCreate(ConditionMeta<?> conditionMeta, Object condition) {
        Class<?> conditionClass = condition.getClass();
        String name = conditionMeta.getName();
        if (DslUtils.isBasicClass(conditionClass)) {
            return QueryBuilders.termsQuery(name, condition);
        } else if (conditionClass.isArray()) {
            Object[] terms = (Object[]) condition;
            if (terms.length == 1) {
                return QueryBuilders.termsQuery(name, terms[0]);
            }
            return QueryBuilders.termsQuery(name, terms);
        } else if (condition instanceof Collection) {
            Collection<?> terms = (Collection<?>) condition;
            if (terms.size() == 1) {
                return QueryBuilders.termsQuery(name, terms.iterator().next());
            }
            return QueryBuilders.termsQuery(name, terms);
        } else {
            throw new IllegalArgumentException("Ids查询，类型不支持：" + conditionClass);
        }
    }
}
