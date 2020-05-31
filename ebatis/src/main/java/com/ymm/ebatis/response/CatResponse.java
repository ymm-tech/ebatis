package com.ymm.ebatis.response;

import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.common.io.stream.StreamOutput;

public class CatResponse extends ActionResponse {
    public CatResponse() {
        System.out.println();
    }

    @Override
    public void writeTo(StreamOutput out) {
        // do nothing
    }
}
