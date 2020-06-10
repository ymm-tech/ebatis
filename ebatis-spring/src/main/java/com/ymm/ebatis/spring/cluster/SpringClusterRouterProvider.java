package com.ymm.ebatis.spring.cluster;

import com.google.auto.service.AutoService;
import com.ymm.ebatis.core.cluster.ClusterRouter;
import com.ymm.ebatis.core.cluster.ClusterRouterProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

/**
 * @author 章多亮
 * @since 2020/6/3 10:37
 */
@Slf4j
@AutoService(ClusterRouterProvider.class)
public class SpringClusterRouterProvider implements ClusterRouterProvider {

    @Override
    public ClusterRouter getClusterRouter(String name) {
        try {
            return ApplicationContextHolder.getBean(name, ClusterRouter.class);
        } catch (NoSuchBeanDefinitionException ignore) {
            return null;
        } catch (Exception e) {
            log.error("创建集群路由失败：{}", name, e);
            throw e;
        }
    }
}
