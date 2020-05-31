package com.ymm.ebatis.core.meta;

import com.ymm.ebatis.core.executor.RequestExecutor;
import com.ymm.ebatis.core.request.RequestFactory;

/**
 * @author 章多亮
 * @since 2020/5/27 17:25
 */
public interface RequestHandler {
    RequestExecutor getRequestExecutor();

    RequestFactory getRequestFactory();

}
