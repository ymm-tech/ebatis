package com.ymm.ebatis.core.response;

import org.elasticsearch.action.ActionResponse;

/**
 * 是什么，就返回什么，就这么单纯
 *
 * @author 章多亮
 * @since 2020/6/2 13:54
 */
class RawResponseExtractor implements ResponseExtractor<ActionResponse> {
    static final RawResponseExtractor INSTANCE = new RawResponseExtractor();

    private RawResponseExtractor() {
    }

    @Override
    public ActionResponse extractData(ActionResponse response) {
        return response;
    }
}
