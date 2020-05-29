package com.ymm.ebatis.response;


import com.google.auto.service.AutoService;
import com.ymm.ebatis.meta.ResultType;

/**
 * @author 章多亮
 * @since 2020/1/18 17:20
 */
@AutoService(ResponseExtractorProvider.class)
public class BulkResponseExtractorProvider extends AbstractBulkResponseExtractorProvider {
    public BulkResponseExtractorProvider() {
        super(ResultType.OTHER);
    }

    @Override
    protected boolean isWrapped() {
        return false;
    }
}
