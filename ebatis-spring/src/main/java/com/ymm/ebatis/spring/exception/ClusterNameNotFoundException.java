package com.ymm.ebatis.spring.exception;

/**
 * @author 章多亮
 * @since 2020/5/22 16:21
 */
public class ClusterNameNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -5230178198338391345L;

    public ClusterNameNotFoundException(String clusterRouterName) {
    }
}
