package io.manbang.ebatis.core.provider;

import org.elasticsearch.search.builder.SearchSourceBuilder;

/** Native escape hatch for post-filter, suggest, rescore and other search-source options. */
public interface SearchSourceBuilderProvider extends Provider {
    void customize(SearchSourceBuilder source);
}
