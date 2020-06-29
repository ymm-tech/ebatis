package com.ymm.ebatis.core.response;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.rest.RestStatus;

/**
 * @author weilong.hu
 * @since 2020/6/29 18:45
 */
public class BooleanDeleteResponseExtractor implements DeleteResponseExtractor<Boolean> {
    public static final BooleanDeleteResponseExtractor INSTANCE = new BooleanDeleteResponseExtractor();

    private BooleanDeleteResponseExtractor() {
    }

    @Override
    public Boolean doExtractData(DeleteResponse response) {
        return response.status() == RestStatus.OK;
    }
}
