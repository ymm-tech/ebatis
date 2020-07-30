package io.manbang.ebatis.core.response;

import org.elasticsearch.action.ActionResponse;

/**
 * @author weilong.hu
 * @since 2020/6/28 20:07
 */
public class VoidResponseExtractor implements ConcreteResponseExtractor<Void, ActionResponse> {
    public static final VoidResponseExtractor INSTANCE = new VoidResponseExtractor();

    private VoidResponseExtractor() {
    }

    @Override
    public Void doExtractData(ActionResponse response) {
        return null;
    }
}
