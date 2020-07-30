package io.manbang.ebatis.sample.mapper;

import io.manbang.ebatis.core.annotation.Get;
import io.manbang.ebatis.sample.entity.RecentOrder;
import io.manbang.ebatis.sample.entity.RecentOrderModel;
import io.manbang.ebatis.spring.annotation.EasyMapper;
import org.elasticsearch.action.get.GetResponse;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * @author weilong.hu
 */
@EasyMapper(indices = "recent_order_index")
public interface RecentOrderGetMapper {
    /**
     * 查询订单
     *
     * @param id 文档id
     * @return 订单 可能为null
     */
    @Get
    RecentOrder getRecentOrder(Long id);

    /**
     * 查询订单
     *
     * @param id 文档id
     * @return 订单 可能为null
     */
    @Get
    RecentOrder getRecentOrder(String id);

    /**
     * 查询订单
     *
     * @param order 订单，需实现IdProvider
     * @return 订单 可能为null
     */
    @Get
    RecentOrder getRecentOrder(RecentOrderModel order);

    /**
     * 查询订单
     *
     * @param id 订单id
     * @return Option
     */
    @Get
    Optional<RecentOrder> getRecentOrderOptional(Long id);

    /**
     * 查询订单
     *
     * @param id 订单id
     * @return GetResponse
     */
    @Get
    GetResponse getRecentOrderGetResponse(Long id);

    /**
     * 查询订单
     *
     * @param id 订单id
     * @return 异步Optional
     */
    @Get
    CompletableFuture<Optional<RecentOrder>> getRecentOrderCompletableFuture(Long id);

    /**
     * 查询订单
     *
     * @param id 订单id
     * @return 异步订单结果
     */
    @Get
    CompletableFuture<RecentOrder> getRecentOrderCompletableFuture(String id);
}
