package io.manbang.ebatis.core.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Properties;

/**
 * @author 章多亮
 * @since 2020/1/8 15:15
 */
@Slf4j
public class Env {
    private static final String DEBUG_ENABLED = "ebatis.debugEnabled";
    private static final String OFFLINE_ENABLED = "ebatis.offlineEnabled";
    private static final String SNIFF_ENABLED = "ebatis.sniffEnabled";
    private static final String EAGER_INIT = "ebatis.eagerInit";

    private static final String CLUSTER_ROUTER_NAME = "ebatis.clusterRouter";

    /**
     * 判断当前配置是否启用调试模式
     */
    @Getter
    private static boolean debugEnabled;
    /**
     * 判断当前配置是否是离线模式，如果是离线模式，则不会发出请求
     */
    @Getter
    private static boolean offlineEnabled;
    /**
     * 获取配置文件中的集群路由名称，作为默认值吧
     */
    @Getter
    private static String clusterRouterName;
    /**
     * 集群嗅探功能是否开启
     */
    @Getter
    private static boolean sniffEnabled;
    @Getter
    private static boolean eagerInit;

    static {
        loadEnvConfig();
    }

    private static void loadEnvConfig() {
        try (InputStream in = Env.class.getClassLoader().getResourceAsStream("ebatis.properties")) {
            if (in == null) {
                return;
            }

            Properties cfg = new Properties();
            cfg.load(in);

            debugEnabled = Boolean.parseBoolean(cfg.getProperty(DEBUG_ENABLED));
            offlineEnabled = Boolean.parseBoolean(cfg.getProperty(OFFLINE_ENABLED));
            sniffEnabled = Boolean.parseBoolean(cfg.getProperty(SNIFF_ENABLED));
            eagerInit = Boolean.parseBoolean(cfg.getProperty(EAGER_INIT));
            clusterRouterName = cfg.getProperty(CLUSTER_ROUTER_NAME);

        } catch (Exception e) {
            log.error("配置文件载入失败", e);
        }
    }

    private Env() {
        throw new UnsupportedOperationException();
    }
}
