package com.ymm.ebatis.sample.mapper;

import com.ymm.ebatis.core.annotation.Index;
import com.ymm.ebatis.sample.entity.RecentOrderModel;
import com.ymm.ebatis.spring.annotation.EasyMapper;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.rest.RestStatus;

/**
 * @author weilong.hu
 */
@EasyMapper(indices = "recent_order_index")
public interface RecentOrderIndexMapper {
    //index Boolean
    @Index
    Boolean indexRecentOrderBoolean(RecentOrderModel recentOrderModel);

    //index boolean
    @Index
    boolean indexRecentOrderBool(RecentOrderModel recentOrderModel);

    //index String
    @Index
    String indexRecentOrderString(RecentOrderModel recentOrderModel);

    //index void
    @Index
    void indexRecentOrderVoid(RecentOrderModel recentOrderModel);

    //index IndexResponse
    @Index
    IndexResponse indexRecentOrderIndexResponse(RecentOrderModel recentOrderModel);

    //index RestStatus
    @Index
    RestStatus indexRecentOrderRestStatus(RecentOrderModel recentOrderModel);
}
