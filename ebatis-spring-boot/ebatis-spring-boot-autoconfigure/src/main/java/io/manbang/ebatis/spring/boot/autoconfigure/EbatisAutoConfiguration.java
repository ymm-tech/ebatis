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

    private static HttpHost[] toHttpHosts(String hosts) {
        return Stream.of(hosts.split(","))
                .filter(StringUtils::isNotBlank)
                .map(host -> host.split(":"))
                .map(endpoint -> new EsHost(endpoint[0].trim(), Integer.parseInt(endpoint[1].trim())))
                .map(EsHost::toHttpHost)
                .toArray(HttpHost[]::new);
    }

    private static WeightedCluster toWeightedClusters(String weightedHosts) {
        String[] strings = weightedHosts.split("@");
        int weight = Integer.parseInt(strings[0]);

        HttpHost[] httpHosts = toHttpHosts(strings[1]);
        return Cluster.weighted(weight, httpHosts);
    }


    @Bean(name = DEFAULT_CLUSTER_ROUTER_NAME, destroyMethod = "close")
    @ConditionalOnMissingBean(ClusterRouter.class)
    @ConditionalOnProperty(prefix = EBATIS_CLUSTER_PREFIX, name = "single")
    public ClusterRouter singleClusterRouter() {
        HttpHost[] httpHosts = toHttpHosts(properties.getCluster().getSingle());
        Cluster cluster = Cluster.simple(httpHosts);
        return ClusterRouter.single(cluster);
    }

    @Bean(name = DEFAULT_CLUSTER_ROUTER_NAME, destroyMethod = "close")
    @ConditionalOnMissingBean(ClusterRouter.class)
    @ConditionalOnProperty(prefix = EBATIS_CLUSTER_PREFIX, name = "random[0]")
    public ClusterRouter randomClusterRouter() {
        Cluster[] clusters = Stream.of(properties.getCluster().getRandom())
                .map(EbatisAutoConfiguration::toHttpHosts)
                .map(Cluster::simple)
                .toArray(Cluster[]::new);
        return ClusterRouter.random(clusters);
    }


    @Bean(name = DEFAULT_CLUSTER_ROUTER_NAME, destroyMethod = "close")
    @ConditionalOnMissingBean(ClusterRouter.class)
    @ConditionalOnProperty(prefix = EBATIS_CLUSTER_PREFIX, name = "round-robbin[0]")
    public ClusterRouter roundRobbinClusterRouter() {
        Cluster[] clusters = Stream.of(properties.getCluster().getRoundRobbin())
                .map(EbatisAutoConfiguration::toHttpHosts)
                .map(Cluster::simple)
                .toArray(Cluster[]::new);
        return ClusterRouter.roundRobbin(clusters);
    }

    @Bean(name = DEFAULT_CLUSTER_ROUTER_NAME, destroyMethod = "close")
    @ConditionalOnMissingBean(ClusterRouter.class)
    @ConditionalOnProperty(prefix = EBATIS_CLUSTER_PREFIX, name = "weighted[0]")
    public ClusterRouter weightedClusterRouter() {
        WeightedCluster[] clusters = Stream.of(properties.getCluster().getWeighted())
                .map(EbatisAutoConfiguration::toWeightedClusters)
                .toArray(WeightedCluster[]::new);
        return ClusterRouter.weighted(clusters);
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

        public HttpHost toHttpHost() {
            return new HttpHost(hostname, port <= 0 ? DEFAULT_PORT : port);
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
