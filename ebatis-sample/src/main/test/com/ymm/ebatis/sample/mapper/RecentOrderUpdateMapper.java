package com.ymm.ebatis.sample.mapper;

import com.ymm.ebatis.core.annotation.Update;
import com.ymm.ebatis.sample.entity.RecentOrderModel;
import com.ymm.ebatis.spring.annotation.EasyMapper;
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
    //update UpdateResponse
    @Update(docAsUpsert = true)
    UpdateResponse updateRecentOrder(RecentOrderModel recentOrderModel);

    //update GetResult
    //todo 返回为null 是否保留
    @Update(docAsUpsert = true)
    GetResult updateRecentOrderGetResult(RecentOrderModel recentOrderModel);

    //update RestStatus
    @Update(docAsUpsert = true)
    RestStatus updateRecentOrderRestStatus(RecentOrderModel recentOrderModel);

    //update Boolean
    @Update(docAsUpsert = true)
    Boolean updateRecentOrderBoolean(RecentOrderModel recentOrderModel);

    //update boolean
    @Update(docAsUpsert = true)
    boolean updateRecentOrderBool(RecentOrderModel recentOrderModel);

    //update Result
    @Update(docAsUpsert = true)
    Result updateRecentOrderResult(RecentOrderModel recentOrderModel);

    //update void
    @Update(docAsUpsert = true)
    void updateRecentOrderVoid(RecentOrderModel recentOrderModel);

    //update CompletableFuture<Result>
    @Update(docAsUpsert = true)
    CompletableFuture<Result> updateRecentOrderFuture(RecentOrderModel recentOrderModel);

    //update CompletableFuture<Void>
    @Update(docAsUpsert = true)
    CompletableFuture<Void> updateRecentOrderFutureVoid(RecentOrderModel recentOrderModel);
}
