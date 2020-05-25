package com.ymm.ebatis.response;

import org.elasticsearch.action.update.UpdateResponse;

/**
 * @author 章多亮
 * @since 2019/12/30 11:53
 */
public class RawUpdateResponseExtractor extends SimpleResponseExtractor<UpdateResponse> implements UpdateResponseExtractor<UpdateResponse> {
    public static final RawUpdateResponseExtractor INSTANCE = new RawUpdateResponseExtractor();

    private RawUpdateResponseExtractor() {
    }
}
