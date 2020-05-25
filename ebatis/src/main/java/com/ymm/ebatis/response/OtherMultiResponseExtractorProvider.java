package com.ymm.ebatis.response;

import com.ymm.ebatis.meta.ResultType;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * @author 章多亮
 * @since 2020/1/18 17:41
 */
public class OtherMultiResponseExtractorProvider extends AbstractMultiSearchResponseExtractorProvider {
    public OtherMultiResponseExtractorProvider() {
        super(ResultType.OTHER);
    }

    /**
     * 获取去掉了 {@link CompletableFuture} 和 {@link Optional} 类型
     */
    @Override
    protected boolean isWrapped() {
        return false;
    }
}
