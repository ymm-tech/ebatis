package com.ymm.ebatis.core.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

/**
 * @author 章多亮
 * @since 2020/1/8 15:15
 */
@Slf4j
public class Env {
    public static final Env INSTANCE = new Env();
    public static final String DEBUG_ENABLED = "ebatis.debugEnabled";
    public static final String OFFLINE_ENABLED = "ebatis.offlineEnabled";
    public static final String CLUSTER_ROUTER_NAME = "ebatis.clusterRouter";
    private static final PropertiesConfiguration CONFIG;

    static {
        PropertiesConfiguration cfg;
        Configurations configs = new Configurations();

        try {
            cfg = configs.properties("ebatis.properties");
        } catch (ConfigurationException e) {
            log.error("配置文件载入失败", e);
            cfg = new PropertiesConfiguration();
            cfg.addProperty(DEBUG_ENABLED, true);
        }
        CONFIG = cfg;
    }

    private Env() {
    }

    /**
     * 判断当前配置是否启用调试模式
     *
     * @return 如果当前启用调试模式，返回<code>true</code>
     */
    public static boolean isDebugEnabled() {
        return CONFIG.getBoolean(DEBUG_ENABLED, false);
    }

    /**
     * 判断当前配置是否是离线模式，如果是离线模式，则不会发出请求
     *
     * @return 如果是离线模式，返回<code>true</code>
     */
    public static boolean isOfflineEnabled() {
        return CONFIG.getBoolean(OFFLINE_ENABLED, false);
    }

    /**
     * 获取配置文件中的集群路由名称，作为默认值吧
     *
     * @return 集群路由名称
     */
    public static String getClusterRouterName() {
        return CONFIG.getString(CLUSTER_ROUTER_NAME);
    }
}
