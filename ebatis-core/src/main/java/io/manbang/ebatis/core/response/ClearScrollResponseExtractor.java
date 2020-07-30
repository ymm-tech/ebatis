package io.manbang.ebatis.core.response;

import org.elasticsearch.action.search.ClearScrollResponse;

/**
 * @author 章多亮
 * @since 2020/6/8 16:13
 */
class ClearScrollResponseExtractor implements ConcreteResponseExtractor<Boolean, ClearScrollResponse> {
    static final ClearScrollResponseExtractor INSTANCE = new ClearScrollResponseExtractor();

    private ClearScrollResponseExtractor() {
    }

    @Override
    public Boolean doExtractData(ClearScrollResponse response) {
        return response.isSucceeded();
    }
}
