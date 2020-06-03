package com.ymm.ebatis.spring.annotation;

import com.ymm.ebatis.core.annotation.Mapper;
import com.ymm.ebatis.spring.exception.ClusterNameNotFoundException;
import com.ymm.ebatis.spring.proxy.EsMapperProxyFactoryBean;
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

@Slf4j
public class EasyMapperBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {
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

        String clusterRouter = AnnotationAttributes.fromMap(beanDefinition.getMetadata().getAnnotationAttributes(Mapper.class.getName())).getString("clusterRouter");

        // 实际的Bean对象是EsMapperProxyFactory，它是个FactoryBean，负责创建EsMapperProxy代理
        beanDefinition.setBeanClassName(EsMapperProxyFactoryBean.class.getName());
        ConstructorArgumentValues constructorArgumentValues = beanDefinition.getConstructorArgumentValues();
        constructorArgumentValues.addGenericArgumentValue(beanClassName);
        constructorArgumentValues.addGenericArgumentValue(getClusterRouterName(clusterRouter));

        super.registerBeanDefinition(definitionHolder, registry);
    }

    private String getClusterRouterName(String clusterRouterName) {
        String name = StringUtils.isBlank(clusterRouterName) ? globalClusterRouter : clusterRouterName;

        if (StringUtils.isBlank(name)) {
            throw new ClusterNameNotFoundException(clusterRouterName);
        }
        return name;
    }


    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        AnnotationMetadata metadata = beanDefinition.getMetadata();
        return metadata.isIndependent() && metadata.isInterface();
    }
}
