package io.manbang.ebatis.core.response;

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
        //todo NOT_FOUND是否作为删除成功判断
        return response.status() == RestStatus.OK;
    }
}
