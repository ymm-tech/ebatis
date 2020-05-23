package com.ymm.ebatis.core.builder;

import com.ymm.ebatis.core.annotation.GeoShape;
import com.ymm.ebatis.core.meta.ConditionMeta;
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
