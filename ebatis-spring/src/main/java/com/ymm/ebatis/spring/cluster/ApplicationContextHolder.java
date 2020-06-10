package com.ymm.ebatis.spring.cluster;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author 章多亮
 * @since 2020/6/3 10:51
 */
public class ApplicationContextHolder implements ApplicationContextAware {
    private static ApplicationContext context;

    public static <T> T getBean(String name, Class<T> beanClass) {
        return context.getBean(name, beanClass);
    }

    public static <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }

    @Override
    public void setApplicationContext(ApplicationContext context) {
        // 只设置一次
        if (ApplicationContextHolder.context == null) {
            ApplicationContextHolder.context = context;
        }
    }
}
