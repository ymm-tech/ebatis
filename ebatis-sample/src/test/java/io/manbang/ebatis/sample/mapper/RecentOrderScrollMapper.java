package io.manbang.ebatis.sample.mapper;

import io.manbang.ebatis.core.annotation.SearchScroll;
import io.manbang.ebatis.core.domain.Pageable;
import io.manbang.ebatis.core.domain.ScrollResponse;
import io.manbang.ebatis.core.response.ResponseExtractor;
import io.manbang.ebatis.sample.condition.RecentOrderScrollCondition;
import io.manbang.ebatis.sample.entity.RecentOrder;
import io.manbang.ebatis.spring.annotation.EasyMapper;

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
