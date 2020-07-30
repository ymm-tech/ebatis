package io.manbang.ebatis.core.interceptor;

import io.manbang.ebatis.core.exception.InterceptorExcepiton;
import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.apache.commons.lang3.concurrent.LazyInitializer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author weilong.hu
 * @date 2020-04-22
 */
public class InterceptorFactory {
    private static final LazyInitializer<Interceptor> INTERCEPTORS;

    static {
        INTERCEPTORS = new LazyInitializer<Interceptor>() {
            @Override
            protected Interceptor initialize() {
                ServiceLoader<Interceptor> interceptorServiceLoader = ServiceLoader.load(Interceptor.class);
                List<Interceptor> interceptorList = new ArrayList<>();
                for (Interceptor interceptor : interceptorServiceLoader) {
                    interceptorList.add(interceptor);
                }
                interceptorList.sort(Comparator.comparingInt(Interceptor::getOrder));
                return new Interceptors(interceptorList);
            }
        };
    }

    public static Interceptor interceptors() {
        try {
            return INTERCEPTORS.get();
        } catch (ConcurrentException e) {
            throw new InterceptorExcepiton("获取 Interceptor 失败", e);
        }
    }

}
