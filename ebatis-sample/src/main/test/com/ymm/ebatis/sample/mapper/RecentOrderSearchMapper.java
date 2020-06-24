package com.ymm.ebatis.sample.mapper;

import com.ymm.ebatis.core.annotation.QueryType;
import com.ymm.ebatis.core.annotation.Search;
import com.ymm.ebatis.core.domain.Page;
import com.ymm.ebatis.core.domain.Pageable;
import com.ymm.ebatis.sample.condition.RecentOrderCondition;
import com.ymm.ebatis.sample.entity.RecentOrder;
import com.ymm.ebatis.spring.annotation.EasyMapper;
import org.elasticsearch.action.search.SearchResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author weilong.hu
 */
@EasyMapper(indices = "recent_order_index")
public interface RecentOrderSearchMapper {
    //Search Entity[]测试
    @Search(queryType = QueryType.BOOL)
    RecentOrder[] queryRecentOrderArray(RecentOrderCondition recentOrderCondition);

    //Search Page<Entity>测试
    @Search(queryType = QueryType.BOOL)
    Page<RecentOrder> queryRecentOrderPage(Pageable pageable, RecentOrderCondition recentOrderCondition);

    //Search List<Entity>测试
    @Search(queryType = QueryType.BOOL)
    List<RecentOrder> queryRecentOrderList(RecentOrderCondition recentOrderCondition);

    //Search SearchResponse测试
    @Search(queryType = QueryType.BOOL)
    SearchResponse queryRecentOrderSearchResponse(RecentOrderCondition recentOrderCondition);

    //Search CompletableFuture<List<Entity>>
    @Search(queryType = QueryType.BOOL)
    CompletableFuture<List<RecentOrder>> queryRecentOrderCompletableFutureList(RecentOrderCondition recentOrderCondition);

    //Search FUNCTION_SCORE测试
    @Search(queryType = QueryType.FUNCTION_SCORE)
    RecentOrder[] queryRecentOrderArrayScoreFunction(RecentOrderCondition recentOrderCondition);
}
