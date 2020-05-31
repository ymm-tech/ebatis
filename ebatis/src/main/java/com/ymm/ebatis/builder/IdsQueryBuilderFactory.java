package com.ymm.ebatis.builder;

import com.ymm.ebatis.annotation.Ids;
import com.ymm.ebatis.meta.ConditionMeta;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * @author 章多亮
 * @since 2020/1/7 9:13
 */
class IdsQueryBuilderFactory extends AbstractQueryBuilderFactory<IdsQueryBuilder, Ids> {
    static final IdsQueryBuilderFactory INSTANCE = new IdsQueryBuilderFactory();

    private IdsQueryBuilderFactory() {
    }

    @Override
    protected IdsQueryBuilder doCreate(ConditionMeta meta, Object condition) {
        IdsQueryBuilder builder = QueryBuilders.idsQuery();

        if (meta.isBasicArrayOrCollection()) {
            if (meta.isArray()) {
                String[] ids = Stream.of((Object[]) condition)
                        .map(String::valueOf)
                        .toArray(String[]::new);
                builder.addIds(ids);
            } else if (meta.isCollection()) {
                String[] ids = ((Collection<?>) condition).stream()
                        .map(String::valueOf)
                        .toArray(String[]::new);

                builder.addIds(ids);
            }
        } else if (meta.isBasic()) {
            builder.addIds(String.valueOf(condition));
        } else {
            throw new IllegalArgumentException("Ids查询，类型不支持：" + meta);
        }

        return builder;
    }
}
