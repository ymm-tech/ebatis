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
            String host = System.getProperty("ebatis.it.host", "127.0.0.1");
            int port = Integer.getInteger("ebatis.it.port", 9200);
            String username = System.getProperty("ebatis.it.username", "");
            String password = System.getProperty("ebatis.it.password", "");
            Cluster cluster = Cluster.simple(host, port, Credentials.basic(username, password));
            return ClusterRouter.single(cluster);
        }

        return null;
    }
}
