package io.manbang.ebatis.core.cluster;

import org.apache.http.HttpHost;

/**
 * 权重集群
 *
 * @author 章多亮
 * @since 2019/12/18 10:23
 */
public abstract class AbstractWeightedCluster extends AbstractCluster implements WeightedCluster {
    public static final int DEFAULT_WEIGHT = 10;

    public AbstractWeightedCluster(String hostname, int port) {
        super(hostname, port);
    }

    public AbstractWeightedCluster(HttpHost... hosts) {
        super(hosts);
    }

    public AbstractWeightedCluster(String hostname, int port, Credentials credentials) {
        super(hostname, port, credentials);
    }

    public AbstractWeightedCluster(HttpHost[] hosts, Credentials credentials) {
        super(hosts, credentials);
    }
}
