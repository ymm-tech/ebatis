package io.manbang.ebatis.sample.mapper;

import io.manbang.ebatis.core.annotation.Update;
import io.manbang.ebatis.sample.entity.RecentOrderModel;
import io.manbang.ebatis.sample.entity.RecentOrderModelScript;
import io.manbang.ebatis.spring.annotation.EasyMapper;
import org.elasticsearch.action.DocWriteResponse.Result;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.index.get.GetResult;
import org.elasticsearch.rest.RestStatus;

import java.util.concurrent.CompletableFuture;

/**
 * @author weilong.hu
 */
@EasyMapper(indices = "recent_order_index")
public interface RecentOrderUpdateMapper {
    /**
     * 使用脚本更新订单，支持部分更新
     *
     * @param orderScript 订单
     * @return UpdateResponse
     */
    @Update
    UpdateResponse updateRecentOrder(RecentOrderModelScript orderScript);

    /**
     * 更新订单，支持部分更新，如果文档不存在，则将部分更新文档建立索引
     *
     * @param order 订单
     * @return UpdateResponse
     */
    @Update(docAsUpsert = true)
    UpdateResponse updateRecentOrder(RecentOrderModel order);

    /**
     * 更新订单，支持部分更新
     *
     * @param order 订单
     * @return GetResult
     */
    @Update
    GetResult updateRecentOrderGetResult(RecentOrderModel order);

    /**
     * 更新订单，支持部分更新
     *
     * @param order 订单
     * @return RestStatus状态码
     */
    @Update
    RestStatus updateRecentOrderRestStatus(RecentOrderModel order);

    /**
     * 更新订单，支持部分更新
     *
     * @param order 订单
     * @return 更新成功，返回<code>true</code>
     */
    @Update
    Boolean updateRecentOrderBoolean(RecentOrderModel order);

    /**
     * 更新订单，支持部分更新
     *
     * @param order 订单
     * @return 更新成功，返回<code>true</code>
     */
    @Update
    boolean updateRecentOrderBool(RecentOrderModel order);

    /**
     * 更新订单，支持部分更新
     *
     * @param order 订单
     * @return Result
     */
    @Update
    Result updateRecentOrderResult(RecentOrderModel order);

    /**
     * 更新订单，支持部分更新
     *
     * @param order 订单
     */
    @Update
    void updateRecentOrderVoid(RecentOrderModel order);

    /**
     * 异步更新订单，支持部分更新
     *
     * @param order 订单
     * @return 异步结果
     */
    @Update
    CompletableFuture<Result> updateRecentOrderFuture(RecentOrderModel order);

    /**
     * 异步更新订单，支持部分更新
     *
     * @param order 订单
     * @return 异步结果
     */
    @Update
    CompletableFuture<Void> updateRecentOrderFutureVoid(RecentOrderModel order);
}
