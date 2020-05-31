package com.ymm.ebatis.core.request;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.Request;

@Setter
@Getter
@Accessors(fluent = true)
public class CatCountRequest extends CatRequest {
    private String index;
    private String format;

    @Override
    protected String getSubEndpoint() {
        if (StringUtils.isNotBlank(index)) {
            return "/count/" + index;
        } else {
            return "/count";
        }
    }

    @Override
    protected void customize(Request request) {
        if (StringUtils.isNotBlank(format)) {
            request.addParameter("format", format);
        }
    }
}
