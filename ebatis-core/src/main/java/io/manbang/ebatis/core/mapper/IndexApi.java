package io.manbang.ebatis.core.mapper;

import org.elasticsearch.action.admin.indices.refresh.RefreshResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.indices.CreateIndexResponse;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author weilong.hu
 * @since 2021/07/05 14:58
 */
public interface IndexApi {
    /**
     * 刷新索引
     *
     * @param indices 待刷新的索引,使用null或 _all刷新所有索引.
     * @return CompletableFuture
     */
    RefreshResponse refresh(String... indices);

    /**
     * 异步刷新索引
     *
     * @param indices 待刷新的索引,使用null或 _all刷新所有索引.
     * @return CompletableFuture
     */
    CompletableFuture<RefreshResponse> refreshAsync(String... indices);

    /**
     * 创建索引
     *
     * @param index    index name
     * @param settings settings
     * @param source   source
     * @return CreateIndexResponse
     */
    @SuppressWarnings("unchecked")
    CreateIndexResponse create(String index, Map settings, Map source);

    /**
     * 异步创建索引
     *
     * @param index    index name
     * @param settings settings
     * @param source   source
     * @return CompletableFuture 异步响应
     */
    @SuppressWarnings("unchecked")
    CompletableFuture<CreateIndexResponse> createAsync(String index, Map settings, Map source);

    /**
     * 删除索引
     *
     * @param indices 索引名
     * @return AcknowledgedResponse 删除响应
     */
    AcknowledgedResponse delete(String... indices);

    /**
     * 异步删除索引
     *
     * @param indices 索引名
     * @return AcknowledgedResponse 删除响应
     */
    CompletableFuture<AcknowledgedResponse> deleteAsync(String... indices);
}
