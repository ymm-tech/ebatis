package com.ymm.ebatis.sample.cluster;

import com.google.auto.service.AutoService;
import com.ymm.ebatis.core.cluster.Cluster;
import com.ymm.ebatis.core.cluster.ClusterRouter;
import com.ymm.ebatis.core.cluster.ClusterRouterProvider;

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
            return ClusterRouter.single(Cluster.localhost());
        } else {
            return null;
        }
    }
}
