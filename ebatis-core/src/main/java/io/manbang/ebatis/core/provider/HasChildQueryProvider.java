package io.manbang.ebatis.core.provider;

import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.QueryBuilder;

public interface HasChildQueryProvider extends Provider {
    String childType();

    QueryBuilder childQuery();

    default ScoreMode scoreMode() {
        return ScoreMode.None;
    }
}
