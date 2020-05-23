package com.ymm.ebatis.core.executor;

import com.ymm.ebatis.core.cluster.Cluster;
import com.ymm.ebatis.core.domain.ContextHolder;
import com.ymm.ebatis.core.interceptor.DefaultRequestInfo;
import com.ymm.ebatis.core.interceptor.Interceptor;
import com.ymm.ebatis.core.interceptor.InterceptorFactory;
import com.ymm.ebatis.core.meta.MethodMeta;
import com.ymm.ebatis.core.response.ResponseExtractor;
import com.ymm.ebatis.core.response.ResponseExtractorFactory;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.common.TriFunction;

/**
 * @author 章多亮
 * @since 2019/12/19 13:45
 */
@Slf4j
public abstract class AbstractMethodExecutor<R extends ActionRequest> implements MethodExecutor {
    private static final Interceptor INTERCEPTOR = InterceptorFactory.interceptors();

    @Override
    public Object execute(Cluster client, MethodMeta method, Object[] args) {
        try {
            INTERCEPTOR.preRequest(args);
            R request = createActionRequest(method, args);
            //打印request
            INTERCEPTOR.postRequest(new DefaultRequestInfo<>(request, args));
            ResponseExtractor<?> extractor = method.getResponseExtractor(args).orElseGet(() -> ResponseExtractorFactory.getResponseExtractor(method));
            return getExecutor(method).apply(client, request, extractor);
        } finally {
            ContextHolder.remove();
        }
    }

    /**
     * 根据映射方法，返回方法执行器
     *
     * @param method Mapper方法
     * @return 方法执行器
     */
    protected abstract TriFunction<Cluster, R, ResponseExtractor<?>, Object> getExecutor(MethodMeta method);


    /**
     * 创建动作请求
     *
     * @param method 方法
     * @param args   方法实参
     * @return 请求
     */
    protected abstract R createActionRequest(MethodMeta method, Object[] args);
}
