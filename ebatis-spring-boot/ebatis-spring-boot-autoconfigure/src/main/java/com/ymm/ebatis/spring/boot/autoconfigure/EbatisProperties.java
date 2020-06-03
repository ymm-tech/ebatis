package com.ymm.ebatis.spring.boot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

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
    }
}
