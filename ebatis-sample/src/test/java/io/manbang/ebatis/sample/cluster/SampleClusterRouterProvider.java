package io.manbang.ebatis.sample.cluster;

import com.google.auto.service.AutoService;
import io.manbang.ebatis.core.cluster.Cluster;
import io.manbang.ebatis.core.cluster.ClusterRouter;
import io.manbang.ebatis.core.cluster.ClusterRouterProvider;
import io.manbang.ebatis.core.cluster.Credentials;

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
            return ClusterRouter.single(cluster);

        } else {
            return null;
        }
    }
}
