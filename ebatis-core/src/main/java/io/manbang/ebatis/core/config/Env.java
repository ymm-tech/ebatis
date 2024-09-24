package io.manbang.ebatis.core.config;

import lombok.Getter;
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
    private static final String DEBUG_ENABLED = "ebatis.debugEnabled";
    private static final String OFFLINE_ENABLED = "ebatis.offlineEnabled";
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

    static {
        try (InputStream in = Env.class.getClassLoader().getResourceAsStream("ebatis.properties")) {
            if (Objects.nonNull(in)) {
                Properties cfg = new Properties();
                cfg.load(in);
                debugEnabled = Boolean.parseBoolean(cfg.getProperty(DEBUG_ENABLED));
                offlineEnabled = Boolean.parseBoolean(cfg.getProperty(OFFLINE_ENABLED));
                clusterRouterName = cfg.getProperty(CLUSTER_ROUTER_NAME);
            } else {
                log.info("未检测到ebatis.properties配置,默认不开启调试模式,离线模式.");
            }
        } catch (Exception e) {
            log.error("配置文件载入失败", e);
        }
    }

    private Env() {
        throw new UnsupportedOperationException();
    }
}
