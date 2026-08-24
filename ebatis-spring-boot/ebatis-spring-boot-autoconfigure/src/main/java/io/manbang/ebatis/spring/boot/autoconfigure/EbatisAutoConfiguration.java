package io.manbang.ebatis.spring.boot.autoconfigure;

import io.manbang.ebatis.core.cluster.Cluster;
import io.manbang.ebatis.core.cluster.ClusterRouter;
import io.manbang.ebatis.core.cluster.WeightedCluster;
import io.manbang.ebatis.spring.annotation.EasyMapperBeanDefinitionScanner;
import io.manbang.ebatis.spring.proxy.EasyMapperProxyFactoryBean;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.stream.Stream;


/**
 * @author 章多亮
 * @since 2020/1/19 15:37
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(EbatisProperties.class)
@ConditionalOnClass({Cluster.class, EasyMapperProxyFactoryBean.class})
public class EbatisAutoConfiguration {
    public static final int DEFAULT_PORT = 9200;
    private static final String DEFAULT_CLUSTER_ROUTER_NAME = "clusterRouter";
    private static final String EBATIS_CLUSTER_PREFIX = "ebatis.cluster";
    private final EbatisProperties properties;

    public EbatisAutoConfiguration(EbatisProperties properties) {
        this.properties = properties;
    }

    private static HttpHost[] toHttpHosts(String hosts, EbatisProperties.ClusterProperties properties) {
        return Stream.of(hosts.split(","))
                .filter(StringUtils::isNotBlank)
                .map(host -> host.split(":"))
                .map(endpoint -> new EsHost(endpoint[0].trim(), Integer.parseInt(endpoint[1].trim()), properties.getScheme()))
                .map(EsHost::toHttpHost)
                .toArray(HttpHost[]::new);
    }

    private static WeightedCluster toWeightedClusters(String weightedHosts, EbatisProperties.ClusterProperties properties) {
        String[] strings = weightedHosts.split("@");
        int weight = Integer.parseInt(strings[0]);

        HttpHost[] httpHosts = toHttpHosts(strings[1], properties);
        return new ConfiguredWeightedCluster(weight, httpHosts, properties);
    }

    private static Cluster configuredCluster(String hosts, EbatisProperties.ClusterProperties properties) {
        return new ConfiguredCluster(toHttpHosts(hosts, properties), properties);
    }


    @Bean(name = DEFAULT_CLUSTER_ROUTER_NAME, destroyMethod = "close")
    @ConditionalOnMissingBean(ClusterRouter.class)
    @ConditionalOnProperty(prefix = EBATIS_CLUSTER_PREFIX, name = "single")
    public ClusterRouter singleClusterRouter() {
        Cluster cluster = configuredCluster(properties.getCluster().getSingle(), properties.getCluster());
        return ClusterRouter.single(cluster);
    }

    @Bean(name = DEFAULT_CLUSTER_ROUTER_NAME, destroyMethod = "close")
    @ConditionalOnMissingBean(ClusterRouter.class)
    @ConditionalOnProperty(prefix = EBATIS_CLUSTER_PREFIX, name = "random[0]")
    public ClusterRouter randomClusterRouter() {
        Cluster[] clusters = Stream.of(properties.getCluster().getRandom())
                .map(hosts -> configuredCluster(hosts, properties.getCluster()))
                .toArray(Cluster[]::new);
        return ClusterRouter.random(clusters);
    }


    @Bean(name = DEFAULT_CLUSTER_ROUTER_NAME, destroyMethod = "close")
    @ConditionalOnMissingBean(ClusterRouter.class)
    @ConditionalOnProperty(prefix = EBATIS_CLUSTER_PREFIX, name = "round-robbin[0]")
    public ClusterRouter roundRobbinClusterRouter() {
        Cluster[] clusters = Stream.of(properties.getCluster().getRoundRobbin())
                .map(hosts -> configuredCluster(hosts, properties.getCluster()))
                .toArray(Cluster[]::new);
        return ClusterRouter.roundRobbin(clusters);
    }

    @Bean(name = DEFAULT_CLUSTER_ROUTER_NAME, destroyMethod = "close")
    @ConditionalOnMissingBean(ClusterRouter.class)
    @ConditionalOnProperty(prefix = EBATIS_CLUSTER_PREFIX, name = "weighted[0]")
    public ClusterRouter weightedClusterRouter() {
        WeightedCluster[] clusters = Stream.of(properties.getCluster().getWeighted())
                .map(hosts -> toWeightedClusters(hosts, properties.getCluster()))
                .toArray(WeightedCluster[]::new);
        return ClusterRouter.weighted(clusters);
    }

    @Bean(name = "ebatisClusterRouters", destroyMethod = "close")
    public NamedClusterRouters namedClusterRouters() {
        NamedClusterRouters routers = new NamedClusterRouters();
        properties.getClusters().forEach((name, cluster) -> routers.put(name, createRouter(cluster)));
        return routers;
    }

    private ClusterRouter createRouter(EbatisProperties.ClusterProperties cluster) {
        if (StringUtils.isNotBlank(cluster.getSingle())) {
            return ClusterRouter.single(configuredCluster(cluster.getSingle(), cluster));
        }
        if (cluster.getRandom() != null && cluster.getRandom().length > 0) {
            return ClusterRouter.random(Stream.of(cluster.getRandom())
                    .map(hosts -> configuredCluster(hosts, cluster)).toArray(Cluster[]::new));
        }
        if (cluster.getRoundRobbin() != null && cluster.getRoundRobbin().length > 0) {
            return ClusterRouter.roundRobbin(Stream.of(cluster.getRoundRobbin())
                    .map(hosts -> configuredCluster(hosts, cluster)).toArray(Cluster[]::new));
        }
        if (cluster.getWeighted() != null && cluster.getWeighted().length > 0) {
            return ClusterRouter.weighted(Stream.of(cluster.getWeighted())
                    .map(hosts -> toWeightedClusters(hosts, cluster)).toArray(WeightedCluster[]::new));
        }
        throw new IllegalArgumentException("Named Ebatis cluster requires single, random or round-robbin hosts");
    }

    @Data
    public static class EsHost {
        /**
         * 主机名
         */
        private final String hostname;
        /**
         * 端口号
         */
        private final int port;
        private final String scheme;

        public HttpHost toHttpHost() {
            return new HttpHost(hostname, port <= 0 ? DEFAULT_PORT : port,
                    StringUtils.defaultIfBlank(scheme, "http"));
        }
    }

    public static class NamedClusterRouters extends LinkedHashMap<String, ClusterRouter> implements java.io.Closeable {
        @Override
        public void close() {
            values().forEach(router -> {
                try {
                    router.close();
                } catch (java.io.IOException exception) {
                    log.warn("关闭命名 Elasticsearch 集群失败", exception);
                }
            });
        }
    }

    public static class AutoConfiguredEasyMapperRegistrar implements ImportBeanDefinitionRegistrar, BeanFactoryAware {
        private BeanFactory beanFactory;

        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            if (!AutoConfigurationPackages.has(this.beanFactory)) {
                log.warn("无法确定 EsMapper 扫描范围，自动 EsMapper 禁用！！！");
                return;
            }

            log.info("自动扫描 @EasyMapper 注解的接口");

            List<String> packages = AutoConfigurationPackages.get(this.beanFactory);
            new EasyMapperBeanDefinitionScanner(registry, DEFAULT_CLUSTER_ROUTER_NAME).scan(packages.toArray(new String[0]));
        }

        @Override
        public void setBeanFactory(BeanFactory beanFactory) {
            this.beanFactory = beanFactory;
        }
    }

    @Configuration
    @Import(AutoConfiguredEasyMapperRegistrar.class)
    @ConditionalOnMissingBean(type = "io.manbang.ebatis.spring.annotation.EasyMapperRegistrar")
    public static class EsMapperScannerNotFound implements InitializingBean {

        @Override
        public void afterPropertiesSet() {
            log.debug("没有配置扫描 @EnableEasyMapper，由 EbatisAutoConfiguration 自动配置完成");
        }
    }
}
