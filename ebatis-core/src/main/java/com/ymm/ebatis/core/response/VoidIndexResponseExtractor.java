package com.ymm.ebatis.core.response;

import org.elasticsearch.action.index.IndexResponse;

/**
 * @author weilong.hu
 * @since 2020/6/28 20:07
 */
public class VoidIndexResponseExtractor implements IndexResponseExtractor<Void> {
    public static final VoidIndexResponseExtractor INSTANCE = new VoidIndexResponseExtractor();

    private VoidIndexResponseExtractor() {
    }

    @Override
    public Void doExtractData(IndexResponse response) {
        return null;
    }
}
