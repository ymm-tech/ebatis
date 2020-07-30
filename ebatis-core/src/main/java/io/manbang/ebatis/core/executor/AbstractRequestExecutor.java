package io.manbang.ebatis.core.executor;

import io.manbang.ebatis.core.cluster.Cluster;
import io.manbang.ebatis.core.interceptor.DefaultRequestInfo;
import io.manbang.ebatis.core.interceptor.Interceptor;
import io.manbang.ebatis.core.interceptor.InterceptorFactory;
import io.manbang.ebatis.core.meta.MethodMeta;
import io.manbang.ebatis.core.request.RequestFactory;
import io.manbang.ebatis.core.response.ResponseExtractor;
import io.manbang.ebatis.core.session.ClusterSession;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionRequest;

import java.util.concurrent.CompletableFuture;

/**
 * 请求执行器，负责整个ES请求的执行流程
 *
 * @author 章多亮
 * @since 2019/12/19 13:45
 */
@Slf4j
public abstract class AbstractRequestExecutor<R extends ActionRequest> implements RequestExecutor {
    private final Interceptor interceptor;
    private final RequestFactory<R> requestFactory;

    protected AbstractRequestExecutor() {
        this.interceptor = InterceptorFactory.interceptors();
        this.requestFactory = getRequestFactory();
    }

    @Override
    public Object execute(Cluster cluster, MethodMeta meta, Object[] args) {
        interceptor.preRequest(args);
        R request = requestFactory.create(meta, args);
        //打印request
        interceptor.postRequest(new DefaultRequestInfo<>(request, args));
        ResponseExtractor<?> extractor = meta.getResponseExtractor(args);

        ClusterSession session = ClusterSession.of(cluster);
        CompletableFuture<?> future = getRequestAction(session).call(request, extractor);
        return meta.getResultType().adaptResult(future);
    }

    /**
     * 子类只要具体要执行什么样的请求操作，父类只负责执行对应的请求
     *
     * @param session 会话
     * @return 请求执行器
     */
    protected abstract RequestAction<R> getRequestAction(ClusterSession session);

    /**
     * 获取请求创建工厂，子类知道自己应该创建什么类型的工厂
     *
     * @return 请求创建工厂
     */
    protected abstract RequestFactory<R> getRequestFactory();

    /**
     * 请求执行动作
     *
     * @param <R> 请求类型
     */
    @FunctionalInterface
    protected interface RequestAction<R extends ActionRequest> {
        CompletableFuture<?> call(R request, ResponseExtractor<?> extractor);
    }
}
