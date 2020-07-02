package com.ymm.ebatis.sample.mapper;

import com.ymm.ebatis.core.annotation.UpdateByQuery;
import com.ymm.ebatis.sample.condition.SampleRecentOrderCondition;
import com.ymm.ebatis.spring.annotation.EasyMapper;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.BulkByScrollTask.Status;

import java.util.concurrent.CompletableFuture;

/**
 * @author weilong.hu
 */
@EasyMapper(indices = "recent_order_index")
public interface RecentOrderUpdateByQueryMapper {
    @UpdateByQuery
    BulkByScrollResponse updateByQueryRecentOrder(SampleRecentOrderCondition sampleRecentOrderCondition);

    @UpdateByQuery
    Status updateByQueryRecentOrderStatus(SampleRecentOrderCondition sampleRecentOrderCondition);

    @UpdateByQuery
    CompletableFuture<BulkByScrollResponse> updateByQueryRecentOrderFuture(SampleRecentOrderCondition sampleRecentOrderCondition);
}
