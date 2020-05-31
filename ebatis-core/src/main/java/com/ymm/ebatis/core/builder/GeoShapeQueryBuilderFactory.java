package com.ymm.ebatis.core.builder;

import com.ymm.ebatis.core.annotation.GeoShape;
import com.ymm.ebatis.core.meta.ConditionMeta;
import org.elasticsearch.index.query.GeoShapeQueryBuilder;

/**
 * @author 章多亮
 * @since 2020/1/8 16:45
 */
class GeoShapeQueryBuilderFactory extends AbstractQueryBuilderFactory<GeoShapeQueryBuilder, GeoShape> {
    static final GeoShapeQueryBuilderFactory INSTANCE = new GeoShapeQueryBuilderFactory();

    private GeoShapeQueryBuilderFactory() {
    }

    @Override
    protected GeoShapeQueryBuilder doCreate(ConditionMeta meta, Object condition) {
        return null;
    }
}
