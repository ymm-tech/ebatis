package io.manbang.ebatis.sample.mapper;

import io.manbang.ebatis.core.annotation.DeleteByQuery;
import io.manbang.ebatis.sample.condition.SampleRecentOrderCondition;
import io.manbang.ebatis.spring.annotation.EasyMapper;
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
