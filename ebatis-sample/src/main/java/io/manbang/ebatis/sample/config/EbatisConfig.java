package io.manbang.ebatis.sample.config;

import io.manbang.ebatis.core.cluster.ClusterRouter;
import io.manbang.ebatis.spring.annotation.EnableEasyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 章多亮
 * @since 2020/6/1 18:27
 */
@Configuration
@EnableEasyMapper(basePackages = "io.manbang.ebatis.sample.mapper")
public class EbatisConfig {
    @Bean(destroyMethod = "close")
    public ClusterRouter clusterRouter() {
        return ClusterRouter.localhost();
    }
}
