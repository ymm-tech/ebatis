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
    public static final String PRINT_DSL_ENABLED = "ebatis.prettyPrintEnabled";
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

    public static boolean debugEnabled() {
        return CONFIG.getBoolean(DEBUG_ENABLED, false);
    }

    public static boolean prettyPrintEnabled() {
        return CONFIG.getBoolean(PRINT_DSL_ENABLED, false);
    }

    public static boolean offlineEnabled() {
        return CONFIG.getBoolean(OFFLINE_ENABLED, false);
    }

    public static String getClusterRouter() {
        return CONFIG.getString(CLUSTER_ROUTER_NAME);
    }
}
