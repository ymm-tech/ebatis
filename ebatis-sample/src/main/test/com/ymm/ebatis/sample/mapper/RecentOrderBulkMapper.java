package com.ymm.ebatis.sample.mapper;

import com.ymm.ebatis.core.annotation.Bulk;
import com.ymm.ebatis.core.annotation.BulkType;
import com.ymm.ebatis.core.annotation.Update;
import com.ymm.ebatis.sample.entity.RecentOrderModel;
import com.ymm.ebatis.sample.entity.RecentOrderModelScript;
import com.ymm.ebatis.spring.annotation.EasyMapper;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author weilong.hu
 */
@EasyMapper(indices = "recent_order_index")
public interface RecentOrderBulkMapper {
    // bulk index List<BulkItemResponse>
    @Bulk(bulkType = BulkType.INDEX)
    List<BulkItemResponse> bulkIndexRecentOrderList(List<RecentOrderModel> recentOrderModels);

    // bulk index List<BulkItemResponse>
    @Bulk(bulkType = BulkType.INDEX)
    List<BulkItemResponse> bulkIndexRecentOrderList(RecentOrderModel... recentOrderModels);

    //bulk index BulkItemResponse[]
    @Bulk(bulkType = BulkType.INDEX)
    BulkItemResponse[] bulkIndexRecentOrderArray(RecentOrderModel... recentOrderModels);

    //bulk index BulkResponse
    @Bulk(bulkType = BulkType.INDEX)
    BulkResponse bulkIndexRecentOrderBulkResponse(RecentOrderModel... recentOrderModels);

    //bulk delete List<BulkItemResponse>
    @Bulk(bulkType = BulkType.DELETE)
    List<BulkItemResponse> bulkDeleteRecentOrderList(Long... ids);

    //bulk delete List<BulkItemResponse>
    @Bulk(bulkType = BulkType.DELETE)
    List<BulkItemResponse> bulkDeleteRecentOrderList(List<RecentOrderModel> recentOrderModels);

    //bulk update List<BulkItemResponse>
    @Bulk(bulkType = BulkType.UPDATE, update = @Update(docAsUpsert = true))
    List<BulkItemResponse> bulkUpdateRecentOrderList(RecentOrderModel... recentOrderModels);

    //bulk update List<BulkItemResponse>
    @Bulk(bulkType = BulkType.UPDATE)
    List<BulkItemResponse> bulkUpdateRecentOrderList(List<RecentOrderModelScript> recentOrderModelScript);

    //bulk update CompletableFuture<List<BulkItemResponse>>
    @Bulk(bulkType = BulkType.UPDATE)
    CompletableFuture<List<BulkItemResponse>> bulkUpdateRecentOrderListFuture(List<RecentOrderModelScript> recentOrderModelScript);

}
