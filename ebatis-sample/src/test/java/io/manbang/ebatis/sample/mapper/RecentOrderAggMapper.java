package io.manbang.ebatis.sample.mapper;

import io.manbang.ebatis.core.annotation.Agg;
import io.manbang.ebatis.core.domain.Pageable;
import io.manbang.ebatis.sample.condition.RecentOrderAggCondition;
import io.manbang.ebatis.spring.annotation.EasyMapper;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;

import java.util.List;
import java.util.Map;

/**
 * @author weilong.hu
 */
@EasyMapper(indices = "recent_order_index", types = "tweet")
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
