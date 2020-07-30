package io.manbang.ebatis.core.response;

import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.rest.RestStatus;

/**
 * @author weilong.hu
 * @since 2020/6/29 20:16
 */
public class BooleanUpdateResponseExtractor implements UpdateResponseExtractor<Boolean> {
    public static final BooleanUpdateResponseExtractor INSTANCE = new BooleanUpdateResponseExtractor();

    private BooleanUpdateResponseExtractor() {

    }

    @Override
    public Boolean doExtractData(UpdateResponse response) {
        return response.status() == RestStatus.CREATED || response.status() == RestStatus.OK;
    }
}
