package com.ymm.ebatis.sample.mapper;

import com.ymm.ebatis.core.annotation.Get;
import com.ymm.ebatis.sample.entity.RecentOrder;
import com.ymm.ebatis.sample.entity.RecentOrderModel;
import com.ymm.ebatis.spring.annotation.EasyMapper;
import org.elasticsearch.action.get.GetResponse;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * @author weilong.hu
 */
@EasyMapper(indices = "recent_order_index")
public interface RecentOrderGetMapper {
    //Get Entity Long
    @Get
    RecentOrder getRecentOrder(Long id);

    //Get Entity String
    @Get
    RecentOrder getRecentOrder(String id);

    //Get Entity Model
    @Get
    RecentOrder getRecentOrder(RecentOrderModel recentOrderModel);

    //Get Optional<Entity> Long
    @Get
    Optional<RecentOrder> getRecentOrderOptional(Long id);

    //Get GetResponse Long
    @Get
    GetResponse getRecentOrderGetResponse(Long id);

    //Get CompletableFuture<Optional<Entity>> Long
    @Get
    CompletableFuture<Optional<RecentOrder>> getRecentOrderCompletableFuture(Long id);

    //Get CompletableFuture<Entity> String
    @Get
    CompletableFuture<RecentOrder> getRecentOrderCompletableFuture(String id);
}
