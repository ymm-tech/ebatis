package com.ymm.ebatis.core.cluster;

import org.apache.http.HttpHost;

/**
 * @author 章多亮
 * @since 2019/12/18 11:01
 */
class FixedWeightedCluster extends AbstractWeightedCluster {
    private final int weight;

    public FixedWeightedCluster(int weight, String hostname, int port) {
        this(weight, new HttpHost[]{new HttpHost(hostname, port)}, null);
    }

    public FixedWeightedCluster(int weight, HttpHost[] hosts) {
        this(weight, hosts, null);
    }

    public FixedWeightedCluster(int weight, HttpHost[] hosts, Credentials credentials) {
        super(hosts, credentials);
        this.weight = weight < 0 ? DEFAULT_WEIGHT : weight;
    }

    public FixedWeightedCluster(int weight, String hostname, int port, Credentials credentials) {
        this(weight, new HttpHost[]{new HttpHost(hostname, port)},  credentials);
    }

    @Override
    public int getWeight() {
        return weight;
    }
}
