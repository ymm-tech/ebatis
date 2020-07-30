package io.manbang.ebatis.sample.mapper;

import io.manbang.ebatis.core.annotation.Index;
import io.manbang.ebatis.sample.entity.RecentOrderModel;
import io.manbang.ebatis.spring.annotation.EasyMapper;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.rest.RestStatus;

import java.util.concurrent.CompletableFuture;

/**
 * @author weilong.hu
 */
@EasyMapper(indices = "recent_order_index")
public interface RecentOrderIndexMapper {
    /**
     * 创建一笔订单
     *
     * @param order order 订单
     * @return 创建成功，返回<code>true</code>
     */
    @Index
    Boolean indexRecentOrderBoolean(RecentOrderModel order);

    /**
     * 创建一笔订单
     *
     * @param order order 订单
     * @return 创建成功，返回<code>true</code>
     */
    @Index
    boolean indexRecentOrderBool(RecentOrderModel order);

    /**
     * 创建一笔订单
     *
     * @param order order 订单
     * @return 创建成功，返回文档id
     */
    @Index
    String indexRecentOrderString(RecentOrderModel order);

    /**
     * 创建一笔订单
     *
     * @param order order 订单
     */
    @Index
    void indexRecentOrderVoid(RecentOrderModel order);

    /**
     * 创建一笔订单
     *
     * @param order order 订单
     * @return 创建成功，返回文档response
     */
    @Index
    IndexResponse indexRecentOrderIndexResponse(RecentOrderModel order);

    /**
     * 创建一笔订单
     *
     * @param order order 订单
     * @return 创建成功，返回RestStatus状态码
     */
    @Index
    RestStatus indexRecentOrderRestStatus(RecentOrderModel order);

    /**
     * 创建一笔订单
     *
     * @param order order 订单
     * @return 创建成功，返回异步RestStatus状态码
     */
    @Index
    CompletableFuture<RestStatus> indexRecentOrderCompletableFuture(RecentOrderModel order);

    /**
     * 创建一笔订单
     *
     * @param order order 订单
     * @return 创建成功，返回异步结果
     */
    @Index
    CompletableFuture<Void> indexRecentOrderFutureVoid(RecentOrderModel order);
}
