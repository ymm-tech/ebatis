package io.manbang.ebatis.core.response;

import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.DocWriteResponse.Result;

/**
 * @author weilong.hu
 * @since 2020/6/29 20:23
 */
public class ResultResponseExtractor implements ConcreteResponseExtractor<Result, DocWriteResponse> {
    public static final ResultResponseExtractor INSTANCE = new ResultResponseExtractor();

    private ResultResponseExtractor() {

    }

    @Override
    public Result doExtractData(DocWriteResponse response) {
        return response.getResult();
    }
}
