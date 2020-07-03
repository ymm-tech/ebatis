package com.ymm.ebatis.sample.mapper;

import com.ymm.ebatis.core.annotation.SearchScroll;
import com.ymm.ebatis.core.domain.Pageable;
import com.ymm.ebatis.core.domain.ScrollResponse;
import com.ymm.ebatis.core.response.ResponseExtractor;
import com.ymm.ebatis.sample.condition.RecentOrderScrollCondition;
import com.ymm.ebatis.sample.entity.RecentOrder;
import com.ymm.ebatis.spring.annotation.EasyMapper;

/**
 * @author weilong.hu
 */
@EasyMapper(indices = "recent_order_index")
public interface RecentOrderScrollMapper {

    @SearchScroll(keepAlive = "5s")
    ScrollResponse<RecentOrder> searchScroll(RecentOrderScrollCondition condition, Pageable pageable);

    @SearchScroll(clearScroll = true)
    boolean clearSearchScroll(String scrollId, ResponseExtractor<Boolean> extractor);
}
