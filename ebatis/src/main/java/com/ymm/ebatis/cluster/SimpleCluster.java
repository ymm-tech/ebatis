package com.ymm.ebatis.cluster;

import org.apache.http.HttpHost;

/**
 * @author 章多亮
 * @since 2019/12/18 13:53
 */
class SimpleCluster extends AbstractCluster {
    public SimpleCluster(String hostname, int port) {
        super(hostname, port);
    }

    public SimpleCluster(HttpHost[] hosts) {
        super(hosts);
    }

    public SimpleCluster(String hostname, int port, Credentials credentials) {
        super(hostname, port, credentials);
    }

    public SimpleCluster(HttpHost[] hosts, Credentials credentials) {
        super(hosts, credentials);
    }
}
