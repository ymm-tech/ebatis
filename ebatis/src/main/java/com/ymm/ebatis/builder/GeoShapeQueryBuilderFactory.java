package com.ymm.ebatis.builder;

import com.ymm.ebatis.annotation.GeoShape;
import com.ymm.ebatis.meta.ConditionMeta;
import org.elasticsearch.index.query.GeoShapeQueryBuilder;

/**
 * @author 章多亮
 * @since 2020/1/8 16:45
 */
public class GeoShapeQueryBuilderFactory extends AbstractQueryBuilderFactory<GeoShapeQueryBuilder, GeoShape> {
    @Override
    protected void setOptionalMeta(GeoShapeQueryBuilder builder, GeoShape annotation) {
        super.setOptionalMeta(builder, annotation);
    }

    @Override
    protected GeoShapeQueryBuilder doCreate(ConditionMeta<?> conditionMeta, Object condition) {
        return null;
    }
}
