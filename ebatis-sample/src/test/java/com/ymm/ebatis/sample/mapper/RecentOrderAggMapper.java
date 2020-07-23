package com.ymm.ebatis.sample.mapper;

import com.ymm.ebatis.core.annotation.Agg;
import com.ymm.ebatis.core.domain.Pageable;
import com.ymm.ebatis.sample.condition.RecentOrderAggCondition;
import com.ymm.ebatis.spring.annotation.EasyMapper;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;

import java.util.List;
import java.util.Map;

/**
 * @author weilong.hu
 */
@EasyMapper(indices = "recent_order_index")
public interface RecentOrderAggMapper {
    // agg SearchResponse
    @Agg
    SearchResponse aggRecentOrder(Pageable pageable, RecentOrderAggCondition agg);

    //agg Aggregations
    @Agg(aggOnly = true)
    Aggregations aggsRecentOrder(RecentOrderAggCondition agg);

    //agg List<Aggregation>
    @Agg
    List<Aggregation> aggRecentOrders(RecentOrderAggCondition agg);

    //agg Map<String, Aggregation>
    @Agg
    Map<String, Aggregation> aggRecentOrderMap(RecentOrderAggCondition agg);
}
