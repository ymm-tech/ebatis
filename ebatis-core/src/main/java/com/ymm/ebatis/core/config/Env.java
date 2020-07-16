package com.ymm.ebatis.core.config;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

/**
 * @author 章多亮
 * @since 2020/1/8 15:15
 */
@Slf4j
public class Env {
    public static final String DEBUG_ENABLED = "ebatis.debugEnabled";
    public static final String OFFLINE_ENABLED = "ebatis.offlineEnabled";
    public static final String CLUSTER_ROUTER_NAME = "ebatis.clusterRouter";

    public static boolean IS_DEBUG_ENABLED = false;
    public static boolean IS_OFFLINE_ENABLED = false;
    public static String DEFAULT_CLUSTER_ROUTER_NAME;

    static {
        try (InputStream in = Env.class.getClassLoader().getResourceAsStream("ebatis.properties")) {
            if (Objects.nonNull(in)) {
                Properties cfg = new Properties();
                cfg.load(in);
                IS_DEBUG_ENABLED = Boolean.parseBoolean(cfg.getProperty(DEBUG_ENABLED));
                IS_OFFLINE_ENABLED = Boolean.parseBoolean(cfg.getProperty(OFFLINE_ENABLED));
                DEFAULT_CLUSTER_ROUTER_NAME = cfg.getProperty(CLUSTER_ROUTER_NAME);
            } else {
                log.info("未检测到ebatis.properties配置,默认不开启调试模式,离线模式.");
            }
        } catch (Exception e) {
            log.error("配置文件载入失败", e);
        }
    }

    private Env() {
    }

    /**
     * 判断当前配置是否启用调试模式
     *
     * @return 如果当前启用调试模式，返回<code>true</code>
     */
    public static boolean isDebugEnabled() {
        return IS_DEBUG_ENABLED;
    }

    /**
     * 判断当前配置是否是离线模式，如果是离线模式，则不会发出请求
     *
     * @return 如果是离线模式，返回<code>true</code>
     */
    public static boolean isOfflineEnabled() {
        return IS_OFFLINE_ENABLED;
    }

    /**
     * 获取配置文件中的集群路由名称，作为默认值吧
     *
     * @return 集群路由名称
     */
    public static String getClusterRouterName() {
        return DEFAULT_CLUSTER_ROUTER_NAME;
    }
}
