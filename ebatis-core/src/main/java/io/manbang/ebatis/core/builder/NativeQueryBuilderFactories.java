package io.manbang.ebatis.core.builder;

import io.manbang.ebatis.core.domain.Script;
import io.manbang.ebatis.core.exception.ConditionNotSupportException;
import io.manbang.ebatis.core.meta.ConditionMeta;
import io.manbang.ebatis.core.provider.GeoBoundingBoxProvider;
import io.manbang.ebatis.core.provider.GeoPolygonProvider;
import io.manbang.ebatis.core.provider.HasChildQueryProvider;
import io.manbang.ebatis.core.provider.HasParentQueryProvider;
import io.manbang.ebatis.core.provider.MoreLikeThisProvider;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.join.query.JoinQueryBuilders;

final class NativeQueryBuilderFactories {
    static final QueryBuilderFactory QUERY_STRING = (meta, value) -> QueryBuilders.queryStringQuery(String.valueOf(value));
    static final QueryBuilderFactory SIMPLE_QUERY_STRING = (meta, value) -> QueryBuilders.simpleQueryStringQuery(String.valueOf(value));
    static final QueryBuilderFactory REGEXP = (meta, value) -> QueryBuilders.regexpQuery(requiredField(meta), String.valueOf(value));
    static final QueryBuilderFactory WRAPPER = (meta, value) -> QueryBuilders.wrapperQuery(String.valueOf(value));
    static final QueryBuilderFactory SCRIPT = (meta, value) -> {
        if (!(value instanceof Script)) {
            throw unsupported("SCRIPT", Script.class);
        }
        return QueryBuilders.scriptQuery(((Script) value).toEsScript());
    };
    static final QueryBuilderFactory MORE_LIKE_THIS = (meta, value) -> {
        if (!(value instanceof MoreLikeThisProvider)) {
            throw unsupported("MLT", MoreLikeThisProvider.class);
        }
        MoreLikeThisProvider provider = (MoreLikeThisProvider) value;
        return QueryBuilders.moreLikeThisQuery(provider.fields(), provider.likeTexts(), provider.likeItems());
    };
    static final QueryBuilderFactory HAS_CHILD = (meta, value) -> {
        if (!(value instanceof HasChildQueryProvider)) {
            throw unsupported("HAS_CHILD", HasChildQueryProvider.class);
        }
        HasChildQueryProvider provider = (HasChildQueryProvider) value;
        return JoinQueryBuilders.hasChildQuery(provider.childType(), provider.childQuery(), provider.scoreMode());
    };
    static final QueryBuilderFactory HAS_PARENT = (meta, value) -> {
        if (!(value instanceof HasParentQueryProvider)) {
            throw unsupported("HAS_PARENT", HasParentQueryProvider.class);
        }
        HasParentQueryProvider provider = (HasParentQueryProvider) value;
        return JoinQueryBuilders.hasParentQuery(provider.parentType(), provider.parentQuery(), provider.score());
    };
    static final QueryBuilderFactory GEO_BOUNDING_BOX = (meta, value) -> {
        if (!(value instanceof GeoBoundingBoxProvider)) {
            throw unsupported("GEO_BOUNDING_BOX", GeoBoundingBoxProvider.class);
        }
        GeoBoundingBoxProvider provider = (GeoBoundingBoxProvider) value;
        return QueryBuilders.geoBoundingBoxQuery(requiredField(meta)).setCorners(provider.topLeft(), provider.bottomRight());
    };
    static final QueryBuilderFactory GEO_POLYGON = (meta, value) -> {
        if (!(value instanceof GeoPolygonProvider)) {
            throw unsupported("GEO_POLYGON", GeoPolygonProvider.class);
        }
        return QueryBuilders.geoPolygonQuery(requiredField(meta), ((GeoPolygonProvider) value).points());
    };

    private NativeQueryBuilderFactories() {
    }

    private static String requiredField(ConditionMeta meta) {
        if (meta == null || meta.getName() == null || meta.getName().isEmpty()) {
            throw new ConditionNotSupportException("This query type requires a mapped field name");
        }
        return meta.getName();
    }

    private static ConditionNotSupportException unsupported(String type, Class<?> provider) {
        return new ConditionNotSupportException(type + " condition must implement " + provider.getName());
    }
}
