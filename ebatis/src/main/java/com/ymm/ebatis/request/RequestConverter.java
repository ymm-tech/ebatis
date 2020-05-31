package com.ymm.ebatis.request;

import org.elasticsearch.client.Request;

@FunctionalInterface
public interface RequestConverter {
    Request toRequest();
}
