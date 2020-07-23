package com.ymm.ebatis.sample.mapper;

import com.ymm.ebatis.core.annotation.DeleteByQuery;
import com.ymm.ebatis.sample.condition.SampleRecentOrderCondition;
import com.ymm.ebatis.spring.annotation.EasyMapper;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.BulkByScrollTask.Status;

import java.util.concurrent.CompletableFuture;

/**
 * @author weilong.hu
 */
@EasyMapper(indices = "recent_order_index")
public interface RecentOrderDeleteByQueryMapper {
    /**
     * 根据指定条件删除订单
     *
     * @param order 查询条件
     * @return 查询删除响应
     */
    @DeleteByQuery
    BulkByScrollResponse deleteByQueryRecentOrder(SampleRecentOrderCondition order);

    /**
     * 根据指定条件删除订单
     *
     * @param order 查询条件
     * @return 查询删除状态
     */
    @DeleteByQuery
    Status deleteByQueryRecentOrderStatus(SampleRecentOrderCondition order);

    /**
     * 根据指定条件删除订单
     *
     * @param order 查询条件
     * @return 异步查询删除响应
     */
    @DeleteByQuery
    CompletableFuture<BulkByScrollResponse> deleteByQueryRecentOrderFuture(SampleRecentOrderCondition order);
}
