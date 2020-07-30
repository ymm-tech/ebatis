package io.manbang.ebatis.spring.annotation;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
class EasyMapperRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes mapperAttributes =
                AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(EnableEasyMapper.class.getName(), false));
        metadata.isIndependent();

        new EasyMapperBeanDefinitionScanner(registry, getClusterName(mapperAttributes)).scan(getPackagesToScan(mapperAttributes, metadata));
    }

    private String getClusterName(AnnotationAttributes mapperAttributes) {
        return mapperAttributes.getString("clusterRouter");
    }

    private String[] getPackagesToScan(AnnotationAttributes attributes, AnnotationMetadata metadata) {
        Set<String> packages = Stream.of(attributes.getStringArray("basePackages"))
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet());

        if (packages.isEmpty()) {
            return new String[]{metadata.getClassName().substring(0, metadata.getClassName().lastIndexOf('.'))};
        } else {
            return packages.toArray(new String[0]);
        }
    }
}
