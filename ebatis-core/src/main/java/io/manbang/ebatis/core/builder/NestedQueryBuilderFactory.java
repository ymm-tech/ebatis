package io.manbang.ebatis.core.builder;

import io.manbang.ebatis.core.annotation.Nested;
import io.manbang.ebatis.core.exception.AttributeNotFoundException;
import io.manbang.ebatis.core.meta.ConditionMeta;
import io.manbang.ebatis.core.provider.PathProvider;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.Objects;
import java.util.Optional;

/**
 * @author weilong.hu
 * @since 2020/12/4 11:27
 */
class NestedQueryBuilderFactory extends AbstractQueryBuilderFactory<NestedQueryBuilder, Nested> {
    public static final NestedQueryBuilderFactory INSTANCE = new NestedQueryBuilderFactory();

    @Override
    protected NestedQueryBuilder doCreate(ConditionMeta conditionMeta, Object condition) {
        String path = null;
        ScoreMode scoreMod = null;
        if (condition instanceof PathProvider) {
            PathProvider provider = (PathProvider) condition;
            path = provider.getPath();
            scoreMod = provider.getScoreMode();
        }
        final Nested nested =
                Optional.ofNullable(conditionMeta).flatMap(c -> c.findAttributeAnnotation(Nested.class)).orElse(null);
        if (Objects.nonNull(nested)) {
            path = nested.path();
            scoreMod = nested.scoreMode();
        }
        if (Objects.isNull(path)) {
            throw new AttributeNotFoundException("条件必须实现 PathProvider或在注解上表明nest");
        }
        return QueryBuilders.nestedQuery(path, AutoQueryBuilderFactory.INSTANCE.create(conditionMeta, condition), scoreMod);


    }
}
