package io.manbang.ebatis.core.response;

import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.rest.RestStatus;

/**
 * @author weilong.hu
 * @since 2020/6/29 17:10
 */
public class RestStatusResponseExtractor implements ConcreteResponseExtractor<RestStatus, DocWriteResponse> {
    public static final RestStatusResponseExtractor INSTANCE = new RestStatusResponseExtractor();

    private RestStatusResponseExtractor() {
    }

    @Override
    public RestStatus doExtractData(DocWriteResponse response) {
        return response.status();
    }
}
