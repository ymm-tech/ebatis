package io.manbang.ebatis.core.provider;

import org.elasticsearch.index.query.MoreLikeThisQueryBuilder;

public interface MoreLikeThisProvider extends Provider {
    default String[] fields() {
        return null;
    }

    default String[] likeTexts() {
        return new String[0];
    }

    default MoreLikeThisQueryBuilder.Item[] likeItems() {
        return new MoreLikeThisQueryBuilder.Item[0];
    }
}
