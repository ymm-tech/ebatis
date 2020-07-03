package com.ymm.ebatis.sample.cluster;

import com.google.auto.service.AutoService;
import com.ymm.ebatis.core.cluster.Cluster;
import com.ymm.ebatis.core.cluster.ClusterRouter;
import com.ymm.ebatis.core.cluster.ClusterRouterProvider;
import com.ymm.ebatis.core.cluster.Credentials;

/**
 * @author weilong.hu
 * @since 2020/6/15 17:17
 */
@AutoService(ClusterRouterProvider.class)
public class SampleClusterRouterProvider implements ClusterRouterProvider {
    public static final String SAMPLE_CLUSTER_NAME = "sampleCluster";

    @Override
    public ClusterRouter getClusterRouter(String name) {
        if (SAMPLE_CLUSTER_NAME.equalsIgnoreCase(name)) {
            Cluster cluster = Cluster.simple("192.168.199.7", 9200, Credentials.basic("stevedore", "yV8jwR5omQ"));
            ClusterRouter clusterRouter = ClusterRouter.single(cluster);
            return clusterRouter;
        } else {
            return null;
        }
    }
}
