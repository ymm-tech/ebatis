package com.ymm.ebatis.sample.mapper;

import com.ymm.ebatis.core.annotation.DeleteByQuery;
import com.ymm.ebatis.sample.condition.SampleRecentOrderCondition;
import com.ymm.ebatis.spring.annotation.EasyMapper;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.BulkByScrollTask.Status;

import java.util.concurrent.CompletableFuture;

/**
 * @author weilong.hu
 */
@EasyMapper(indices = "recent_order_index")
public interface RecentOrderDeleteByQueryMapper {
    @DeleteByQuery
    BulkByScrollResponse deleteByQueryRecentOrder(SampleRecentOrderCondition sampleRecentOrderCondition);

    @DeleteByQuery
    Status deleteByQueryRecentOrderStatus(SampleRecentOrderCondition sampleRecentOrderCondition);

    @DeleteByQuery
    CompletableFuture<BulkByScrollResponse> deleteByQueryRecentOrderFuture(SampleRecentOrderCondition sampleRecentOrderCondition);
}
