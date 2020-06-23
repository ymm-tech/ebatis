package com.ymm.ebatis.sample;

import com.ymm.ebatis.core.common.ObjectMapperHolder;
import com.ymm.ebatis.core.domain.Page;
import com.ymm.ebatis.core.domain.Pageable;
import com.ymm.ebatis.core.proxy.MapperProxyFactory;
import com.ymm.ebatis.sample.cluster.SampleClusterRouterProvider;
import com.ymm.ebatis.sample.condition.RecentOrderCondition;
import com.ymm.ebatis.sample.entity.RecentOrder;
import com.ymm.ebatis.sample.mapper.RecentOrderMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author weilong.hu
 * @since 2020/6/15 13:32
 */
@Slf4j
public class EsQueryTest {
    private RecentOrderMapper recentOrderMapper;

    @Before
    public void startup() {
        recentOrderMapper = createEsMapper(RecentOrderMapper.class);
    }

    protected <R> R createEsMapper(Class<R> mapperClass) {
        return MapperProxyFactory.getMapperProxy(mapperClass, SampleClusterRouterProvider.SAMPLE_CLUSTER_NAME);
    }

    @SneakyThrows
    @Test
    public void queryRecentOrderArray() {
        RecentOrderCondition recentOrderCondition = new RecentOrderCondition();
        recentOrderCondition.setCargoId(10124512292911L);
        RecentOrder[] recentOrders = recentOrderMapper.queryRecentOrderArray(recentOrderCondition);
        String s = ObjectMapperHolder.objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(recentOrders);
        log.info("result:{}", s);
    }

    @SneakyThrows
    @Test
    public void queryRecentOrderPage() {
        RecentOrderCondition recentOrderCondition = new RecentOrderCondition();
        recentOrderCondition.setCargoId(10124512292911L);
        Page<RecentOrder> recentOrders = recentOrderMapper.queryRecentOrderPage(recentOrderCondition, Pageable.of(0, 10));
        String s = ObjectMapperHolder.objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(recentOrders);
        log.info("result:{}", s);
    }

    @SneakyThrows
    @Test
    public void queryRecentOrderList() {
        RecentOrderCondition recentOrderCondition = new RecentOrderCondition();
        recentOrderCondition.setCargoId(10124512292911L);
        List<RecentOrder> recentOrders = recentOrderMapper.queryRecentOrderList(recentOrderCondition);
        String s = ObjectMapperHolder.objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(recentOrders);
        log.info("result:{}", s);
    }

    @SneakyThrows
    @Test
    public void queryRecentOrderSearchResponse() {
        RecentOrderCondition recentOrderCondition = new RecentOrderCondition();
        recentOrderCondition.setCargoId(10124512292911L);
        SearchResponse searchResponse = recentOrderMapper.queryRecentOrderSearchResponse(recentOrderCondition);
        String s = ObjectMapperHolder.objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(searchResponse);
        log.info("result:{}", s);
    }

    @SneakyThrows
    @Test
    public void queryRecentOrderCompletableFutureList() {
        RecentOrderCondition recentOrderCondition = new RecentOrderCondition();
        recentOrderCondition.setCargoId(10124512292911L);
        CompletableFuture<List<RecentOrder>> listCompletableFuture = recentOrderMapper.queryRecentOrderCompletableFutureList(recentOrderCondition);
        List<RecentOrder> recentOrders = listCompletableFuture.get();
        String s = ObjectMapperHolder.objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(recentOrders);
        log.info("result:{}", s);
    }

    @SneakyThrows
    @Test
    public void queryRecentOrderArrayScoreFunction() {
        RecentOrderCondition recentOrderCondition = new RecentOrderCondition();
        recentOrderCondition.setCargoId(10124512292911L);
        RecentOrder[] recentOrders = recentOrderMapper.queryRecentOrderArrayScoreFunction(recentOrderCondition);
        String s = ObjectMapperHolder.objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(recentOrders);
        log.info("result:{}", s);
    }

}
