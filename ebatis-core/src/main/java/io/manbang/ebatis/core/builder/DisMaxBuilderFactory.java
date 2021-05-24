package io.manbang.ebatis.core.builder;

import io.manbang.ebatis.core.annotation.DisMax;
import io.manbang.ebatis.core.exception.ConditionNotSupportException;
import io.manbang.ebatis.core.meta.ConditionMeta;
import io.manbang.ebatis.core.provider.DisMaxProvider;
import org.elasticsearch.index.query.DisMaxQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author weilong.hu
 * @since 2021/5/24 9:53
 */
class DisMaxBuilderFactory extends AbstractQueryBuilderFactory<QueryBuilder, DisMax> {
    static final DisMaxBuilderFactory INSTANCE = new DisMaxBuilderFactory();

    @Override
    protected QueryBuilder doCreate(ConditionMeta meta, Object condition) {
        if (!(condition instanceof DisMaxProvider)) {
            throw new ConditionNotSupportException("条件必须实现: DisMaxProvider");
        }
        final DisMaxProvider disMaxProvider = (DisMaxProvider) condition;
        final DisMaxQueryBuilder disMaxQueryBuilder = QueryBuilders.disMaxQuery().tieBreaker(disMaxProvider.tieBreaker());

        final Object[] conditions = Optional.ofNullable(disMaxProvider.conditions()).orElseThrow(() -> new ConditionNotSupportException("DisMaxProvider condition不能为null"));
        Arrays.stream(conditions).forEach(c -> disMaxQueryBuilder.add(QueryBuilderFactory.bool().create(null, c)));
        return disMaxQueryBuilder;
    }
}
