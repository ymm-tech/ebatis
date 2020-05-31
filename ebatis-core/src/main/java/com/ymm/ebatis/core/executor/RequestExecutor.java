package com.ymm.ebatis.core.executor;

import com.ymm.ebatis.core.cluster.Cluster;
import com.ymm.ebatis.core.meta.MethodMeta;

/**
 * @author 章多亮
 * @since 2019/12/19 13:41
 */
@FunctionalInterface
public interface RequestExecutor {
    /**
     * 获取搜索请求执行器
     *
     * @return 搜索请求执行器
     */
    static RequestExecutor search() {
        return SearchRequestExecutor.INSTANCE;
    }

    /**
     * 获取批量搜索请求执行器
     *
     * @return 批量搜索请求执行器
     */
    static RequestExecutor multiSearch() {
        return MultiSearchRequestExecutor.INSTANCE;
    }

    /**
     * 获取聚合请求执行器
     *
     * @return 聚合请求执行器
     */
    static RequestExecutor agg() {
        return AggRequestExecutor.INSTANCE;
    }

    /**
     * 获取批量操作请求执行器
     *
     * @return 批量请求执行器
     */
    static RequestExecutor bulk() {
        return BulkRequestExecutor.INSTANCE;
    }

    /**
     * 获取删除请求执行器
     *
     * @return 删除请求执行器
     */
    static RequestExecutor delete() {
        return DeleteRequestExecutor.INSTANCE;
    }

    /**
     * 获取索引请求执行器
     *
     * @return 索引请求执行器
     */
    static RequestExecutor index() {
        return IndexRequestExecutor.INSTANCE;
    }

    /**
     * 获取搜索更新请求执行器
     *
     * @return 搜索更新请求执行器
     */
    static RequestExecutor updateByQuery() {
        return UpdateByQueryRequestExecutor.INSTANCE;
    }

    /**
     * 获取搜索删除请求执行器
     *
     * @return 搜索删除执行器
     */
    static RequestExecutor deleteByQuery() {
        return DeleteByQueryRequestExecutor.INSTANCE;
    }

    /**
     * 获取更新请求执行器
     *
     * @return 更新请求执行器
     */
    static RequestExecutor update() {
        return UpdateRequestExecutor.INSTANCE;
    }

    static RequestExecutor cat() {
        return CatRequestExecutor.INSTANCE;
    }

    /**
     * 执行实际的ES操作
     *
     * @param cluster ES客户端
     * @param method  ES操作方法
     * @param args    实参列表
     * @return 搜索响应数据
     */
    Object execute(Cluster cluster, MethodMeta method, Object[] args);
}
