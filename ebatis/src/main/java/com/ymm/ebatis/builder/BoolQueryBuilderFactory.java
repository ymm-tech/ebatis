package com.ymm.ebatis.builder;

import com.ymm.ebatis.annotation.Bool;
import com.ymm.ebatis.meta.ConditionMeta;
import com.ymm.ebatis.meta.FieldMeta;
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
public class BoolQueryBuilderFactory extends AbstractQueryBuilderFactory<BoolQueryBuilder, Bool> {
    public static final BoolQueryBuilderFactory INSTANCE = new BoolQueryBuilderFactory();

    private BoolQueryBuilderFactory() {
    }

    @Override
    protected BoolQueryBuilder doCreate(ConditionMeta meta, Object condition) {
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        if (condition == null) {
            return builder;
        }

        Map<Class<? extends Annotation>, List<FieldMeta>> queryClauses = meta.getQueryClauses(condition);

        queryClauses.forEach((key, fieldMetas) -> QueryClauseType.valueOf(key).addQueryBuilder(builder, fieldMetas, condition));

        return builder;
    }
}

