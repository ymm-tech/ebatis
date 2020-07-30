package io.manbang.ebatis.sample.mapper;

import io.manbang.ebatis.core.annotation.MultiGet;
import io.manbang.ebatis.sample.entity.RecentOrder;
import io.manbang.ebatis.sample.entity.RecentOrderModel;
import io.manbang.ebatis.spring.annotation.EasyMapper;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * @author weilong.hu
 */
@EasyMapper(indices = "recent_order_index")
public interface RecentOrderMultiGetMapper {
    /**
     * 多订单查询
     *
     * @param ids 文档id
     * @return MultiGetResponse
     */
    @MultiGet
    MultiGetResponse getRecentOrdersResponse(Long... ids);


    /**
     * 单订单查询
     *
     * @param id 文档id
     * @return MultiGetResponse
     */
    @MultiGet
    MultiGetResponse getRecentOrdersResponse(Long id);

    /**
     * 单订单查询
     *
     * @param order 订单
     * @return MultiGetResponse
     */
    @MultiGet
    MultiGetResponse getRecentOrdersResponse(RecentOrderModel order);

    /**
     * 多订单查询
     *
     * @param ids 订单id
     * @return 订单集合
     */
    @MultiGet
    List<RecentOrder> getRecentOrders(Long... ids);

    /**
     * 多订单查询
     *
     * @param orders 订单
     * @return 订单数组
     */
    @MultiGet
    RecentOrder[] getRecentOrders(List<RecentOrderModel> orders);

    /**
     * 多订单查询
     *
     * @param ids 订单id
     * @return MultiGetItemResponse[]
     */
    @MultiGet
    MultiGetItemResponse[] getRecentOrdersItemResponse(Long... ids);

    /**
     * 多订单查询
     *
     * @param orders 订单id
     * @return List<MultiGetItemResponse>
     */
    @MultiGet
    List<MultiGetItemResponse> getRecentOrdersItemResponse(List<RecentOrderModel> orders);

    /**
     * 多订单查询
     *
     * @param ids 订单id
     * @return List<Optional < RecentOrder>>
     */
    @MultiGet
    List<Optional<RecentOrder>> getRecentOrdersOptional(Long... ids);

    /**
     * 多订单查询
     *
     * @param orders 订单
     * @return Optional<RecentOrder>[]
     */
    @MultiGet
    Optional<RecentOrder>[] getRecentOrdersOptional(List<RecentOrderModel> orders);

    /**
     * 多订单异步查询
     *
     * @param orders 订单
     * @return 异步订单查询结果
     */
    @MultiGet
    CompletableFuture<Optional<RecentOrder>[]> getRecentOrdersOptionalFuture(List<RecentOrderModel> orders);

}
