package com.ymm.ebatis.sample.mapper;

import com.ymm.ebatis.core.annotation.UpdateByQuery;
import com.ymm.ebatis.sample.condition.SampleRecentOrderCondition;
import com.ymm.ebatis.spring.annotation.EasyMapper;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.BulkByScrollTask.Status;

import java.util.concurrent.CompletableFuture;

/**
 * @author weilong.hu
 */
@EasyMapper(indices = "recent_order_index")
public interface RecentOrderUpdateByQueryMapper {
    /**
     * 根据指定条件更新订单
     *
     * @param order 查询条件
     * @return 更新响应
     */
    @UpdateByQuery
    BulkByScrollResponse updateByQueryRecentOrder(SampleRecentOrderCondition order);

    /**
     * 根据指定条件更新订单
     *
     * @param order 查询条件
     * @return 更新响应
     */
    @UpdateByQuery
    Status updateByQueryRecentOrderStatus(SampleRecentOrderCondition order);

    /**
     * 根据指定条件更新订单
     *
     * @param order 查询条件
     * @return 更新响应
     */
    @UpdateByQuery
    CompletableFuture<BulkByScrollResponse> updateByQueryRecentOrderFuture(SampleRecentOrderCondition order);
}
