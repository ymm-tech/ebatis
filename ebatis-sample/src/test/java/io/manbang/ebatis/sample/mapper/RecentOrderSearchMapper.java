package io.manbang.ebatis.sample.mapper;

import io.manbang.ebatis.core.annotation.QueryType;
import io.manbang.ebatis.core.annotation.Search;
import io.manbang.ebatis.core.domain.Page;
import io.manbang.ebatis.core.domain.Pageable;
import io.manbang.ebatis.sample.condition.RecentOrderCondition;
import io.manbang.ebatis.sample.condition.SampleRecentOrderCondition;
import io.manbang.ebatis.sample.entity.RecentOrder;
import io.manbang.ebatis.spring.annotation.EasyMapper;
import org.elasticsearch.action.search.SearchResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author weilong.hu
 */
@EasyMapper(indices = "recent_order_index")
public interface RecentOrderSearchMapper {
    /**
     * 搜索订单
     *
     * @param condition 搜索条件
     * @return 订单集合
     */
    @Search(queryType = QueryType.BOOL)
    RecentOrder[] queryRecentOrderArray(RecentOrderCondition condition);

    /**
     * 搜索订单
     *
     * @param pageable  分页信息
     * @param condition 搜索条件
     * @return 订单分页
     */
    @Search(queryType = QueryType.BOOL)
    Page<RecentOrder> queryRecentOrderPage(Pageable pageable, RecentOrderCondition condition);

    //Search List<Entity>测试
    @Search(queryType = QueryType.BOOL)
    List<RecentOrder> queryRecentOrderList(RecentOrderCondition condition);

    //Search SearchResponse测试
    @Search(queryType = QueryType.BOOL)
    SearchResponse queryRecentOrderSearchResponse(RecentOrderCondition condition);

    //Search CompletableFuture<List<Entity>>
    @Search(queryType = QueryType.BOOL)
    CompletableFuture<List<RecentOrder>> queryRecentOrderCompletableFutureList(RecentOrderCondition condition);

    //Search FUNCTION_SCORE测试
    @Search(queryType = QueryType.FUNCTION_SCORE)
    RecentOrder[] queryRecentOrderArrayScoreFunction(RecentOrderCondition condition);

    //Search Entity 测试
    @Search(queryType = QueryType.BOOL)
    RecentOrder queryRecentOrder(SampleRecentOrderCondition condition);

    //Search Long 测试
    @Search(queryType = QueryType.BOOL)
    Long queryRecentOrderCount(SampleRecentOrderCondition condition);

    //Search long 测试
    @Search(queryType = QueryType.BOOL)
    long queryRecentOrderCount();

    //Search Boolean 测试
    @Search(queryType = QueryType.BOOL)
    Boolean queryRecentOrderCountWithBoolean(SampleRecentOrderCondition condition);

    //Search Boolean 测试
    @Search(queryType = QueryType.BOOL)
    boolean queryRecentOrderCountWithBool(SampleRecentOrderCondition condition);

}
