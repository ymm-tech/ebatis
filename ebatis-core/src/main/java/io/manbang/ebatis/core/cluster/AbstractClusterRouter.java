package io.manbang.ebatis.core.cluster;

import io.manbang.ebatis.core.meta.MethodMeta;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author 章多亮
 * @since 2019/12/18 10:05
 */
@Slf4j
public abstract class AbstractClusterRouter implements ClusterRouter {
    @Override
    public void close() throws IOException {
        for (Cluster cluster : getClusters()) {
            cluster.close();
        }
    }

    @Override
    public Cluster route(MethodMeta meta) {
        Cluster cluster = choose(meta);
        log.debug("选定集群: {}", cluster.getName());
        return cluster;
    }

    private Cluster choose(MethodMeta meta) {
        return getLoadBalancer(meta).choose(getClusters());
    }

    /**
     * 获取集群列表，当前应用能使用的全部集群
     *
     * @return 集群列表
     */
    protected abstract Cluster[] getClusters();

    /**
     * 通过方法元数据可以获取当前调用的上下文信息，根据这个上下文信息，作为依据选择当前执行的方法应高使用那个集群，执行操作
     *
     * @param meta 方法元数据
     * @return 集群负载均衡器
     */
    protected abstract ClusterLoadBalancer getLoadBalancer(MethodMeta meta);
}
