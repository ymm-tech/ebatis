package com.ymm.ebatis.sample.config;

import com.ymm.ebatis.core.cluster.ClusterRouter;
import com.ymm.ebatis.spring.annotation.EnableEsMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 章多亮
 * @since 2020/6/1 18:27
 */
@Configuration
@EnableEsMapper(basePackages = "com.ymm.ebatis.sample.mapper")
public class EbatisConfig {
    @Bean(destroyMethod = "close")
    public ClusterRouter clusterRouter() {
        return ClusterRouter.localhost();
    }
}
