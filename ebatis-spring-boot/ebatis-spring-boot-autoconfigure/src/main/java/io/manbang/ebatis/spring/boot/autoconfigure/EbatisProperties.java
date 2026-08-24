package io.manbang.ebatis.spring.boot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Ebatis配置
 *
 * @author 章多亮
 * @since 2020/1/19 15:39
 */
@Data
@ConfigurationProperties(prefix = "ebatis")
public class EbatisProperties {
    /**
     * 集群主机配置
     */
    private ClusterProperties cluster;
    /**
     * 离线模式，不会实际去连接ES
     */
    private boolean offlineEnabled;
    /**
     * 调试模式
     */
    private boolean debugEnabled;
    /** Named cluster routers, addressable by @EasyMapper(clusterRouter = "name"). */
    private Map<String, ClusterProperties> clusters = new LinkedHashMap<>();

    @Data
    public static class ClusterProperties {
        /**
         * 单集群主机配置，单节点集群，或者多节点集群的VIP地址
         * <pre>
         *     ebatis:
         *       cluster:
         *         single: hostname1:port,hostname2:port,hostname3:port
         * </pre>
         */
        private String single;
        /**
         * 随机路由集群主机配置，每个主机配置，均为一个独立集群，或者是单节点，或者是多节点VIP
         * <pre>
         *     ebatis:
         *       cluster:
         *         random:
         *           - hostname1:port,hostname2:port,hostname3:port
         *           - hostname1:port,hostname2:port,hostname3:port
         * </pre>
         */
        private String[] random;
        /**
         * 轮询路由集群主机配置，每个主机配置，均为一个独立集群，或者是单节点，或者是多节点VIP
         * <pre>
         *     ebatis:
         *       cluster:
         *         round-robbin:
         *           - hostname1:port,hostname2:port,hostname3:port
         *           - hostname1:port,hostname2:port,hostname3:port
         *
         * </pre>
         */
        private String[] roundRobbin;
        /**
         * 带权路由集群主机配置，每个主机配置，均为一个独立集群，或者是单节点，或者是多节点VIP
         * <pre>
         *     ebatis:
         *       cluster:
         *         weighted:
         *           - 50@hostname1:port,hostname2:port,hostname3:port
         *           - 50@hostname1:port,hostname2:port,hostname3:port
         * </pre>
         */
        private String[] weighted;

        private String scheme = "http";
        private String username;
        private String password;
        private int connectTimeout = 1000;
        private int socketTimeout = 30000;
        private int connectionRequestTimeout = 500;
        private int maxConnTotal = 100;
        private int maxConnPerRoute = 100;
        private Map<String, String> defaultHeaders = new LinkedHashMap<>();
        private boolean sniff;
        private int sniffInterval = 10000;
        private int sniffAfterFailureDelay = 10000;
        private SslProperties ssl = new SslProperties();
    }

    @Data
    public static class SslProperties {
        private boolean enabled;
        private String trustStore;
        private String trustStorePassword;
        private boolean verifyHostname = true;
    }
}
