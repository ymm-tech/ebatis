package com.ymm.ebatis.response;

import org.elasticsearch.action.delete.DeleteResponse;

public interface DeleteResponseExtractor<T> extends ConcreteResponseExtractor<T, DeleteResponse> {
}
