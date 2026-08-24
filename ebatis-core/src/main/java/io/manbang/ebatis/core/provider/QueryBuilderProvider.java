package io.manbang.ebatis.core.provider;

import org.elasticsearch.index.query.QueryBuilder;

/** Allows mapper conditions to provide an Elasticsearch native query. */
public interface QueryBuilderProvider extends Provider {
    QueryBuilder queryBuilder();
}
