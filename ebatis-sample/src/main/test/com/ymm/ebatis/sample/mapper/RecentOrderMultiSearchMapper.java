package com.ymm.ebatis.sample.mapper;

import com.ymm.ebatis.core.annotation.MultiSearch;
import com.ymm.ebatis.core.annotation.QueryType;
import com.ymm.ebatis.core.domain.Page;
import com.ymm.ebatis.core.domain.Pageable;
import com.ymm.ebatis.sample.condition.SampleRecentOrderCondition;
import com.ymm.ebatis.sample.entity.RecentOrder;
import com.ymm.ebatis.spring.annotation.EasyMapper;

import java.util.List;

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
}
