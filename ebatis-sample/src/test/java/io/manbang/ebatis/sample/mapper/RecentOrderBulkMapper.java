package io.manbang.ebatis.sample.mapper;

import io.manbang.ebatis.core.annotation.Bulk;
import io.manbang.ebatis.core.annotation.BulkType;
import io.manbang.ebatis.core.annotation.Update;
import io.manbang.ebatis.sample.entity.RecentOrderModel;
import io.manbang.ebatis.sample.entity.RecentOrderModelScript;
import io.manbang.ebatis.spring.annotation.EasyMapper;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author weilong.hu
 */
@EasyMapper(indices = "recent_order_index")
public interface RecentOrderBulkMapper {
    /**
     * 批量创建订单
     *
     * @param orders 订单
     * @return 响应
     */
    @Bulk(bulkType = BulkType.INDEX)
    List<BulkItemResponse> bulkIndexRecentOrderList(List<RecentOrderModel> orders);

    /**
     * 批量创建订单
     *
     * @param orders 订单
     * @return 响应
     */
    @Bulk(bulkType = BulkType.INDEX)
    List<BulkItemResponse> bulkIndexRecentOrderList(RecentOrderModel... orders);

    /**
     * 批量创建订单
     *
     * @param orders 订单
     * @return 响应
     */
    @Bulk(bulkType = BulkType.INDEX)
    BulkItemResponse[] bulkIndexRecentOrderArray(RecentOrderModel... orders);

    /**
     * 批量创建订单
     *
     * @param orders 订单
     * @return 响应
     */
    @Bulk(bulkType = BulkType.INDEX)
    BulkResponse bulkIndexRecentOrderBulkResponse(RecentOrderModel... orders);

    /**
     * 批量删除订单
     *
     * @param ids 订单id
     * @return 响应
     */
    @Bulk(bulkType = BulkType.DELETE)
    List<BulkItemResponse> bulkDeleteRecentOrderList(Long... ids);

    /**
     * 批量删除订单
     *
     * @param orders 订单
     * @return 响应
     */
    @Bulk(bulkType = BulkType.DELETE)
    List<BulkItemResponse> bulkDeleteRecentOrderList(List<RecentOrderModel> orders);

    /**
     * 批量更新订单，订单不存在时，则插入订单
     *
     * @param orders 订单
     * @return 响应
     */
    @Bulk(bulkType = BulkType.UPDATE, update = @Update(docAsUpsert = true))
    List<BulkItemResponse> bulkUpdateRecentOrderList(RecentOrderModel... orders);

    /**
     * 脚本批量更新订单
     *
     * @param orderScripts 脚本
     * @return 响应
     */
    @Bulk(bulkType = BulkType.UPDATE)
    List<BulkItemResponse> bulkUpdateRecentOrderList(List<RecentOrderModelScript> orderScripts);

    /**
     * 脚本更新订单
     *
     * @param orderScripts 脚本
     * @return 异步响应
     */
    @Bulk(bulkType = BulkType.UPDATE)
    CompletableFuture<List<BulkItemResponse>> bulkUpdateRecentOrderListFuture(List<RecentOrderModelScript> orderScripts);

}
