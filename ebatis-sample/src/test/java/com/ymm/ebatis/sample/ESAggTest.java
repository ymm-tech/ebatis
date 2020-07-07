package com.ymm.ebatis.sample;

import com.ymm.ebatis.core.domain.Pageable;
import com.ymm.ebatis.sample.condition.RecentOrderAggCondition;
import com.ymm.ebatis.sample.mapper.RecentOrderAggMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * @author weilong.hu
 * @since 2020/7/6 22:30
 */
@Slf4j
public class ESAggTest extends ESAbstractTest {
    private RecentOrderAggMapper recentOrderAggMapper;

    @Before
    public void startup() {
        recentOrderAggMapper = createEsMapper(RecentOrderAggMapper.class);
    }

    @Test
    @SneakyThrows
    public void aggRecentOrder() {
        SearchResponse searchResponse = recentOrderAggMapper.aggRecentOrder(Pageable.of(0, 10), new RecentOrderAggCondition());
        String result = getJsonResult(searchResponse);
        log.info("result:{}", result);
    }

    @Test
    @SneakyThrows
    public void aggsRecentOrder() {
        Aggregations aggregations = recentOrderAggMapper.aggsRecentOrder(new RecentOrderAggCondition());
        String result = getJsonResult(aggregations);
        log.info("result:{}", result);
    }

    @Test
    @SneakyThrows
    public void aggRecentOrders() {
        List<Aggregation> aggregations = recentOrderAggMapper.aggRecentOrders(new RecentOrderAggCondition());
        String result = getJsonResult(aggregations);
        log.info("result:{}", result);
    }

    @Test
    @SneakyThrows
    public void aggRecentOrderMap() {
        Map<String, Aggregation> aggMap = recentOrderAggMapper.aggRecentOrderMap(new RecentOrderAggCondition());
        String result = getJsonResult(aggMap);
        log.info("result:{}", result);
    }
}
