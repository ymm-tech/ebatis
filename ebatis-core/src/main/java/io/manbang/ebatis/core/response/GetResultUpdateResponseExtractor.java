package io.manbang.ebatis.core.response;

import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.index.get.GetResult;

/**
 * @author weilong.hu
 * @since 2020/6/29 20:01
 */
public class GetResultUpdateResponseExtractor implements UpdateResponseExtractor<GetResult> {
    public static final GetResultUpdateResponseExtractor INSTANCE = new GetResultUpdateResponseExtractor();

    private GetResultUpdateResponseExtractor() {
    }

    @Override
    public GetResult doExtractData(UpdateResponse response) {
        return response.getGetResult();
    }
}
