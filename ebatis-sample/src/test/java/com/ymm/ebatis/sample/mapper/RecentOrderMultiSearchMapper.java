package com.ymm.ebatis.sample.mapper;

import com.ymm.ebatis.core.annotation.MultiSearch;
import com.ymm.ebatis.core.annotation.QueryType;
import com.ymm.ebatis.core.domain.Page;
import com.ymm.ebatis.core.domain.Pageable;
import com.ymm.ebatis.sample.condition.SampleRecentOrderCondition;
import com.ymm.ebatis.sample.entity.RecentOrder;
import com.ymm.ebatis.spring.annotation.EasyMapper;
import org.elasticsearch.action.search.MultiSearchResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author weilong.hu
 */
@EasyMapper(indices = "recent_order_index")
public interface RecentOrderMultiSearchMapper {
    //MultiSearch List<Page<Entity>>  形参数组
    @MultiSearch(queryType = QueryType.BOOL)
    List<Page<RecentOrder>> queryRecentOrderListPage(SampleRecentOrderCondition[] sampleRecentOrderConditions, Pageable[] pageables);

    //MultiSearch List<Page<Entity>>  形参集合
    @MultiSearch(queryType = QueryType.BOOL)
    List<Page<RecentOrder>> queryRecentOrderListPage(List<SampleRecentOrderCondition> sampleRecentOrderConditions, List<Pageable> pageables);

    //MultiSearch List<Page<Entity>>  非集合数组形参
    @MultiSearch(queryType = QueryType.BOOL)
    List<Page<RecentOrder>> queryRecentOrderListPage(SampleRecentOrderCondition sampleRecentOrderConditions, Pageable pageables);

    //MultiSearch List<Page<Entity>>
    @MultiSearch(queryType = QueryType.BOOL)
    Page<RecentOrder>[] queryRecentOrderPageArray(SampleRecentOrderCondition[] sampleRecentOrderConditions, Pageable[] pageables);

    //MultiSearch List<Page<Entity>>
    @MultiSearch(queryType = QueryType.BOOL)
    List<List<RecentOrder>> queryRecentOrderListList(SampleRecentOrderCondition[] sampleRecentOrderConditions);

    //MultiSearch Entity[][]
    @MultiSearch(queryType = QueryType.BOOL)
    RecentOrder[][] queryRecentOrderArrayArray(SampleRecentOrderCondition[] sampleRecentOrderConditions);

    //MultiSearch List<Entity[]>
    @MultiSearch(queryType = QueryType.BOOL)
    List<RecentOrder[]> queryRecentOrderListArray(SampleRecentOrderCondition[] sampleRecentOrderConditions);

    //MultiSearch MultiSearchResponse
    @MultiSearch(queryType = QueryType.BOOL)
    MultiSearchResponse queryRecentOrderMultiSearchResponse(SampleRecentOrderCondition[] sampleRecentOrderConditions);

    //MultiSearch List<Entity>[]
    @MultiSearch(queryType = QueryType.BOOL)
    List<RecentOrder>[] queryRecentOrderArrayList(SampleRecentOrderCondition[] sampleRecentOrderConditions);

    //MultiSearch Entity[][]
    @MultiSearch(queryType = QueryType.BOOL)
    CompletableFuture<RecentOrder[][]> queryRecentOrderArrayArrayFuture(SampleRecentOrderCondition[] sampleRecentOrderConditions);

    //MultiSearch List<Long>
    @MultiSearch(queryType = QueryType.BOOL)
    List<Long> queryRecentOrderCount(SampleRecentOrderCondition[] sampleRecentOrderConditions);

    //MultiSearch  Long[]
    @MultiSearch(queryType = QueryType.BOOL)
    Long[] queryRecentOrderCounts(SampleRecentOrderCondition[] sampleRecentOrderConditions);

    //MultiSearch long[]
    @MultiSearch(queryType = QueryType.BOOL)
    long[] queryRecentOrderBasicCounts(SampleRecentOrderCondition[] sampleRecentOrderConditions);

    //MultiSearch List<Boolean>
    @MultiSearch(queryType = QueryType.BOOL)
    List<Boolean> queryRecentOrderBooleanCounts(SampleRecentOrderCondition[] sampleRecentOrderConditions);

    //MultiSearch Boolean[]
    @MultiSearch(queryType = QueryType.BOOL)
    Boolean[] queryRecentOrderBooleanArrayCounts(SampleRecentOrderCondition[] sampleRecentOrderConditions);

    //MultiSearch boolean[]
    @MultiSearch(queryType = QueryType.BOOL)
    boolean[] queryRecentOrderBoolArrayCounts(SampleRecentOrderCondition[] sampleRecentOrderConditions);
}
