package io.manbang.ebatis.core.provider;

import org.elasticsearch.index.query.QueryBuilder;

public interface HasParentQueryProvider extends Provider {
    String parentType();

    QueryBuilder parentQuery();

    default boolean score() {
        return false;
    }
}
