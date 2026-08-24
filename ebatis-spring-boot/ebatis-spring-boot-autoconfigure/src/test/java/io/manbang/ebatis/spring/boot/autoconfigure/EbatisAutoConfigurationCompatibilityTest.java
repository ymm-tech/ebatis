package io.manbang.ebatis.spring.boot.autoconfigure;

import io.manbang.ebatis.core.cluster.Cluster;
import io.manbang.ebatis.core.cluster.ClusterRouter;
import io.manbang.ebatis.core.cluster.WeightedClusterRouter;
import org.elasticsearch.client.RequestOptions;
import org.junit.Assume;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EbatisAutoConfigurationCompatibilityTest {
    @Test
    public void legacySingleClusterConfigurationStillCreatesHttpRouter() throws IOException {
        EbatisProperties properties = new EbatisProperties();
        properties.getCluster().setSingle("127.0.0.1:9200");
        ClusterRouter router = new EbatisAutoConfiguration(properties).singleClusterRouter();
        try {
            Cluster cluster = router.route(null);
            assertTrue(cluster instanceof ConfiguredCluster);
            assertTrue(cluster.getName().contains("http://127.0.0.1:9200"));
        } finally {
            router.close();
        }
    }

    @Test
    public void namedAndWeightedClusterConfigurationsRemainRoutable() throws IOException {
        EbatisProperties properties = new EbatisProperties();
        EbatisProperties.ClusterProperties named = new EbatisProperties.ClusterProperties();
        named.setSingle("127.0.0.1:9243");
        named.setScheme("https");
        properties.getClusters().put("archive", named);

        EbatisProperties.ClusterProperties weighted = properties.getCluster();
        weighted.setWeighted(new String[]{"3@127.0.0.1:9200"});
        EbatisAutoConfiguration configuration = new EbatisAutoConfiguration(properties);
        EbatisAutoConfiguration.NamedClusterRouters routers = configuration.namedClusterRouters();
        ClusterRouter weightedRouter = configuration.weightedClusterRouter();
        try {
            assertTrue(routers.get("archive").route(null).getName().contains("https://127.0.0.1:9243"));
            assertEquals(3, ((WeightedClusterRouter) weightedRouter).getWeightedClusters()[0].getWeight());
        } finally {
            routers.close();
            weightedRouter.close();
        }
    }

    @Test
    public void configuredLegacySingleClusterConnectsWithBasicAuthentication() throws IOException {
        String host = System.getProperty("ebatis.it.host", "");
        String username = System.getProperty("ebatis.it.username", "");
        String password = System.getProperty("ebatis.it.password", "");
        Assume.assumeTrue(!host.isEmpty() && !username.isEmpty() && !password.isEmpty());

        EbatisProperties properties = new EbatisProperties();
        properties.getCluster().setSingle(host + ":" + Integer.getInteger("ebatis.it.port", 9200));
        properties.getCluster().setUsername(username);
        properties.getCluster().setPassword(password);
        ClusterRouter router = new EbatisAutoConfiguration(properties).singleClusterRouter();
        try {
            assertEquals("7.15.0", router.route(null).highLevelClient()
                    .info(RequestOptions.DEFAULT).getVersion().getNumber());
        } finally {
            router.close();
        }
    }
}
