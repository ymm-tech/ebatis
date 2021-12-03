package io.manbang.ebatis.core.builder;

import io.manbang.ebatis.core.annotation.GeoShape;
import io.manbang.ebatis.core.exception.ConditionNotSupportException;
import io.manbang.ebatis.core.meta.ConditionMeta;
import io.manbang.ebatis.core.provider.BuildProvider;
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
        if (!(condition instanceof io.manbang.ebatis.core.domain.GeoShape)) {
            throw new ConditionNotSupportException("条件必须实现: io.manbang.ebatis.core.domain.GeoShape");
        }
        return ((BuildProvider) condition).build();
    }
}