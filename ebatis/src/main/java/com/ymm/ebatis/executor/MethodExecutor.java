package com.ymm.ebatis.executor;

import com.ymm.ebatis.cluster.Cluster;
import com.ymm.ebatis.meta.MethodMeta;

/**
 * @author 章多亮
 * @since 2019/12/19 13:41
 */
@FunctionalInterface
public interface MethodExecutor {
    /**
     * 执行实际的ES操作
     *
     * @param client ES客户端
     * @param method ES操作方法
     * @param args   实参列表
     * @return 搜索响应数据
     */
    Object execute(Cluster client, MethodMeta method, Object[] args);
}
