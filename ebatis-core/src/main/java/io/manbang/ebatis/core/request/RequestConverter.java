package io.manbang.ebatis.core.request;

import org.elasticsearch.client.Request;

@FunctionalInterface
public interface RequestConverter {
    Request toRequest();
}
