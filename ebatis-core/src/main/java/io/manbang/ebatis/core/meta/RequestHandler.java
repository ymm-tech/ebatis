package io.manbang.ebatis.core.meta;

import io.manbang.ebatis.core.executor.RequestExecutor;
import io.manbang.ebatis.core.request.RequestFactory;

/**
 * @author 章多亮
 * @since 2020/5/27 17:25
 */
public interface RequestHandler {
    RequestExecutor getRequestExecutor();

    RequestFactory getRequestFactory();

}
