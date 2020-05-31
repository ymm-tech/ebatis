package com.ymm.ebatis.core.response;


import com.google.auto.service.AutoService;
import com.ymm.ebatis.core.meta.ResultType;

/**
 * @author 章多亮
 * @since 2020/1/18 17:05
 */
@AutoService(ResponseExtractorProvider.class)
public class IndexResponseExtractorProvider extends AbstractIndexResponseExtractorProvider {
    public IndexResponseExtractorProvider() {
        super(ResultType.OTHER);
    }

    @Override
    protected boolean isWrapped() {
        return false;
    }
}
