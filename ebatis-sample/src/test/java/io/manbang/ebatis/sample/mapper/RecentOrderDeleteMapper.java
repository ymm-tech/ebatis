package io.manbang.ebatis.sample.mapper;

import io.manbang.ebatis.core.annotation.Delete;
import io.manbang.ebatis.sample.entity.RecentOrderModel;
import io.manbang.ebatis.spring.annotation.EasyMapper;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.rest.RestStatus;

import java.util.concurrent.CompletableFuture;

/**
 * @author weilong.hu
 */
@EasyMapper(indices = "recent_order_index")
public interface RecentOrderDeleteMapper {
    /**
     * 删除订单
     *
     * @param id 订单id
     * @return RestStatus
     */
    @Delete
    RestStatus deleteRecentOrder(Long id);

    /**
     * 删除订单
     *
     * @param model model
     * @return RestStatus
     */
    @Delete
    RestStatus deleteRecentOrder(RecentOrderModel model);

    /**
     * 删除订单
     *
     * @param id 订单id
     * @return DeleteResponse
     */
    @Delete
    DeleteResponse deleteRecentOrderDeleteResponse(Long id);

    /**
     * 删除订单
     *
     * @param id 订单id
     * @return 删除成功，返回<code>true</code>
     */
    @Delete
    Boolean deleteRecentOrderBoolean(Long id);

    /**
     * 删除订单
     *
     * @param id 订单id
     * @return 删除成功，返回<code>true</code>
     */
    @Delete
    boolean deleteRecentOrderBool(Long id);

    /**
     * 删除订单
     *
     * @param id 订单id
     */
    @Delete
    void deleteRecentOrderVoid(Long id);

    /**
     * 异步删除订单
     *
     * @param id 订单id
     * @return 异步结果
     */
    @Delete
    CompletableFuture<Boolean> deleteRecentOrderBooleanFuture(Long id);

    /**
     * 异步删除订单
     *
     * @param id 订单id
     * @return 异步结果
     */
    @Delete
    CompletableFuture<Void> deleteRecentOrderVoidFuture(Long id);
}
