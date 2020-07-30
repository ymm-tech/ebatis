package io.manbang.ebatis.spring.annotation;

import io.manbang.ebatis.core.annotation.Mapper;
import io.manbang.ebatis.spring.exception.ClusterRouterNameNotSetException;
import io.manbang.ebatis.spring.proxy.EasyMapperProxyFactoryBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

/**
 * Mapper接口扫描器
 *
 * @author duoliang.zhang
 */
@Slf4j
public class EasyMapperBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {
    private static final String CLUSTER_ROUTER_ATTRIBUTE_NAME = "clusterRouter";
    private final String globalClusterRouter;

    public EasyMapperBeanDefinitionScanner(BeanDefinitionRegistry registry, String globalClusterRouter) {
        super(registry, false);
        this.globalClusterRouter = globalClusterRouter;

        addIncludeFilter(new AnnotationTypeFilter(Mapper.class));
        addIncludeFilter(new AnnotationTypeFilter(EasyMapper.class));
    }

    @Override
    protected void registerBeanDefinition(BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry) {
        AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) definitionHolder.getBeanDefinition();
        String beanClassName = beanDefinition.getBeanClassName();

        String clusterRouter = AnnotationAttributes.fromMap(beanDefinition.getMetadata().getAnnotationAttributes(Mapper.class.getName())).getString(CLUSTER_ROUTER_ATTRIBUTE_NAME);

        // EasyMapperProxyFactoryBean，它是个FactoryBean，负责创建MapperProxy代理
        beanDefinition.setBeanClassName(EasyMapperProxyFactoryBean.class.getName());
        ConstructorArgumentValues constructorArgumentValues = beanDefinition.getConstructorArgumentValues();
        constructorArgumentValues.addGenericArgumentValue(beanClassName);
        constructorArgumentValues.addGenericArgumentValue(getClusterRouter(clusterRouter));

        super.registerBeanDefinition(definitionHolder, registry);
    }

    private String getClusterRouter(String clusterRouter) {
        String name = StringUtils.isEmpty(clusterRouter) ? globalClusterRouter : clusterRouter;

        if (StringUtils.isEmpty(name)) {
            throw new ClusterRouterNameNotSetException();
        }

        return name;
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        AnnotationMetadata metadata = beanDefinition.getMetadata();
        return metadata.isIndependent() && metadata.isInterface();
    }
}
