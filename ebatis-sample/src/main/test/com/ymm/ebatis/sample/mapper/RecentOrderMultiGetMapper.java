package com.ymm.ebatis.sample.mapper;

import com.ymm.ebatis.core.annotation.MultiGet;
import com.ymm.ebatis.sample.entity.RecentOrder;
import com.ymm.ebatis.sample.entity.RecentOrderModel;
import com.ymm.ebatis.spring.annotation.EasyMapper;
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
    //MultiGet MultiGetResponse
    @MultiGet
    MultiGetResponse getRecentOrdersResponse(Long... ids);

    //MultiGet MultiGetResponse
    @MultiGet
    MultiGetResponse getRecentOrdersResponse(Long id);

    //MultiGet MultiGetResponse
    @MultiGet
    MultiGetResponse getRecentOrdersResponse(RecentOrderModel recentOrderModel);

    //MultiGet List<Entity>
    @MultiGet
    List<RecentOrder> getRecentOrders(Long... ids);

    //MultiGet Entity[]
    @MultiGet
    RecentOrder[] getRecentOrders(List<RecentOrderModel> recentOrderModel);

    //MultiGet MultiGetItemResponse[]
    @MultiGet
    MultiGetItemResponse[] getRecentOrdersItemResponse(Long... ids);

    //MultiGet  List<MultiGetItemResponse>
    @MultiGet
    List<MultiGetItemResponse> getRecentOrdersItemResponse(List<RecentOrderModel> recentOrderModel);

    //MultiGet  List<Optional<Entity>>
    @MultiGet
    List<Optional<RecentOrder>> getRecentOrdersOptional(Long... ids);

    //MultiGet Optional<Entity>[]
    @MultiGet
    Optional<RecentOrder>[] getRecentOrdersOptional(List<RecentOrderModel> recentOrderModel);

    //MultiGet CompletableFuture<Optional<Entity>[]>
    @MultiGet
    CompletableFuture<Optional<RecentOrder>[]> getRecentOrdersOptionalFuture(List<RecentOrderModel> recentOrderModel);

}
