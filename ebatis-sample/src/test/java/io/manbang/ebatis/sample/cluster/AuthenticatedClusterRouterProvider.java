package io.manbang.ebatis.sample.cluster;

import com.google.auto.service.AutoService;
import io.manbang.ebatis.core.cluster.Cluster;
import io.manbang.ebatis.core.cluster.ClusterRouter;
import io.manbang.ebatis.core.cluster.ClusterRouterProvider;
import io.manbang.ebatis.core.cluster.Credentials;

@AutoService(ClusterRouterProvider.class)
public class AuthenticatedClusterRouterProvider implements ClusterRouterProvider {
    @Override
    public ClusterRouter getClusterRouter(String name) {
        if ("chip".equals(name)) {
            Cluster cluster = Cluster.simple("127.0.0.1", 9200, Credentials.basic("elastic", "kingdom"));
            return ClusterRouter.single(cluster);
        }

        return null;
    }
}
