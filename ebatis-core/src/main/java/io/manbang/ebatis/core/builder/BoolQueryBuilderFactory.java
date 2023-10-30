package io.manbang.ebatis.core.builder;

import io.manbang.ebatis.core.annotation.Bool;
import io.manbang.ebatis.core.meta.ClassMeta;
import io.manbang.ebatis.core.meta.ConditionMeta;
import io.manbang.ebatis.core.meta.FieldMeta;
import io.manbang.ebatis.core.meta.NestNameHolder;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;


/**
 * @author duoliang.zhang
 */
@Slf4j
class BoolQueryBuilderFactory extends AbstractQueryBuilderFactory<BoolQueryBuilder, Bool> {
    static final BoolQueryBuilderFactory INSTANCE = new BoolQueryBuilderFactory();

    private BoolQueryBuilderFactory() {
    }

    private Map<Class<? extends Annotation>, List<FieldMeta>> getQueryClauses(ConditionMeta meta, Object condition) {
        return meta == null ? ClassMeta.of(condition.getClass()).getQueryClauses() : meta.getQueryClauses(condition);
    }

    @Override
    protected BoolQueryBuilder doCreate(ConditionMeta meta, Object condition) {
        BoolQueryBuilder builder = QueryBuilders.boolQuery();

        if (meta != null && meta.isNested()) {
            NestNameHolder.get().push(meta.getName());
        }

        getQueryClauses(meta, condition)
                .forEach((key, fieldMetas) -> QueryClauseType.valueOf(key)
                        .addQueryBuilder(builder, fieldMetas, condition));

        return builder;
    }
}

