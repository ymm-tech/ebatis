package com.ymm.ebatis.core.exception;

/**
 * @author 章多亮
 * @since 2020/6/2 17:45
 */
public class ClusterRouterNotFoundException extends EbatisException {
    private static final long serialVersionUID = -1638825424766300974L;

    public ClusterRouterNotFoundException(String message) {
        super(message);
    }
}
