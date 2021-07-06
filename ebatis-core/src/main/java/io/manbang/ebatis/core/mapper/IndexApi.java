package io.manbang.ebatis.core.mapper;

import org.elasticsearch.action.admin.indices.refresh.RefreshResponse;

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


}
