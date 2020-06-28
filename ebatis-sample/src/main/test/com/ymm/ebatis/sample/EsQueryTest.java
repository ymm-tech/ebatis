package com.ymm.ebatis.sample;

import com.google.common.collect.Lists;
import com.ymm.ebatis.core.common.ObjectMapperHolder;
import com.ymm.ebatis.core.domain.Page;
import com.ymm.ebatis.core.domain.Pageable;
import com.ymm.ebatis.core.proxy.MapperProxyFactory;
import com.ymm.ebatis.sample.cluster.SampleClusterRouterProvider;
import com.ymm.ebatis.sample.condition.RecentOrderCondition;
import com.ymm.ebatis.sample.condition.SampleRecentOrderCondition;
import com.ymm.ebatis.sample.entity.RecentOrder;
import com.ymm.ebatis.sample.mapper.RecentOrderMultiSearchMapper;
import com.ymm.ebatis.sample.mapper.RecentOrderSearchMapper;
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
    private RecentOrderSearchMapper recentOrderMapper;
    private RecentOrderMultiSearchMapper recentOrderMultiSearchMapper;

    @Before
    public void startup() {
        recentOrderMapper = createEsMapper(RecentOrderSearchMapper.class);
        recentOrderMultiSearchMapper = createEsMapper(RecentOrderMultiSearchMapper.class);
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
        Page<RecentOrder> recentOrders = recentOrderMapper.queryRecentOrderPage(Pageable.of(1, 10), recentOrderCondition);
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

    @SneakyThrows
    @Test
    public void multiSearchListPageWithArray() {
        RecentOrderCondition recentOrderCondition = new RecentOrderCondition();
        recentOrderCondition.setCargoId(10124512292966L);
        SampleRecentOrderCondition sampleRecentOrderCondition = new SampleRecentOrderCondition();
        sampleRecentOrderCondition.setCargoId(10124512292911L);
        List<Page<RecentOrder>> pages = recentOrderMultiSearchMapper.queryRecentOrderListPage(
                new SampleRecentOrderCondition[]{recentOrderCondition, sampleRecentOrderCondition},
                new Pageable[]{Pageable.of(0, 10), Pageable.of(1, 10)});
        String s = ObjectMapperHolder.objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(pages);
        log.info("result:{}", s);
    }

    @SneakyThrows
    @Test
    public void multiSearchListPageWithList() {
        RecentOrderCondition recentOrderCondition = new RecentOrderCondition();
        recentOrderCondition.setCargoId(10124512292966L);
        SampleRecentOrderCondition sampleRecentOrderCondition = new SampleRecentOrderCondition();
        sampleRecentOrderCondition.setCargoId(10124512292911L);
        List<Page<RecentOrder>> pages = recentOrderMultiSearchMapper.queryRecentOrderListPage(
                Lists.newArrayList(recentOrderCondition, sampleRecentOrderCondition),
                Lists.newArrayList(Pageable.of(0, 10), Pageable.of(1, 10)));
        String s = ObjectMapperHolder.objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(pages);
        log.info("result:{}", s);
    }

    @SneakyThrows
    @Test
    public void multiSearchListPageWithSingle() {
        RecentOrderCondition recentOrderCondition = new RecentOrderCondition();
        recentOrderCondition.setCargoId(10124512292911L);
        List<Page<RecentOrder>> pages = recentOrderMultiSearchMapper.queryRecentOrderListPage(
                recentOrderCondition,
                Pageable.of(0, 10));
        String s = ObjectMapperHolder.objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(pages);
        log.info("result:{}", s);
    }

    @SneakyThrows
    @Test
    public void multiSearchQueryRecentOrderPageArray() {
        RecentOrderCondition recentOrderCondition = new RecentOrderCondition();
        recentOrderCondition.setCargoId(10124512292966L);
        SampleRecentOrderCondition sampleRecentOrderCondition = new SampleRecentOrderCondition();
        sampleRecentOrderCondition.setCargoId(10124512292911L);
        Page<RecentOrder>[] pages = recentOrderMultiSearchMapper.queryRecentOrderPageArray(
                new SampleRecentOrderCondition[]{recentOrderCondition, sampleRecentOrderCondition},
                new Pageable[]{Pageable.of(0, 10), Pageable.of(1, 10)});
        String s = ObjectMapperHolder.objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(pages);
        log.info("result:{}", s);
    }

    @SneakyThrows
    @Test
    public void multiSearchListListWithArray() {
        RecentOrderCondition recentOrderCondition = new RecentOrderCondition();
        recentOrderCondition.setCargoId(10124512292966L);
        SampleRecentOrderCondition sampleRecentOrderCondition = new SampleRecentOrderCondition();
        sampleRecentOrderCondition.setCargoId(10124512292911L);
        List<List<RecentOrder>> recentOrders = recentOrderMultiSearchMapper.queryRecentOrderListList(
                new SampleRecentOrderCondition[]{recentOrderCondition, sampleRecentOrderCondition});
        String s = ObjectMapperHolder.objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(recentOrders);
        log.info("result:{}", s);
    }

    @SneakyThrows
    @Test
    public void multiSearchArrayArrayWithArray() {
        RecentOrderCondition recentOrderCondition = new RecentOrderCondition();
        recentOrderCondition.setCargoId(10124512292966L);
        SampleRecentOrderCondition sampleRecentOrderCondition = new SampleRecentOrderCondition();
        sampleRecentOrderCondition.setCargoId(10124512292911L);
        RecentOrder[][] recentOrders = recentOrderMultiSearchMapper.queryRecentOrderArrayArray(
                new SampleRecentOrderCondition[]{recentOrderCondition, sampleRecentOrderCondition});
        String s = ObjectMapperHolder.objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(recentOrders);
        log.info("result:{}", s);
    }

    @SneakyThrows
    @Test
    public void multiSearchListArrayWithArray() {
        RecentOrderCondition recentOrderCondition = new RecentOrderCondition();
        recentOrderCondition.setCargoId(10124512292966L);
        SampleRecentOrderCondition sampleRecentOrderCondition = new SampleRecentOrderCondition();
        sampleRecentOrderCondition.setCargoId(10124512292911L);
        List<RecentOrder[]> recentOrders = recentOrderMultiSearchMapper.queryRecentOrderListArray(
                new SampleRecentOrderCondition[]{recentOrderCondition, sampleRecentOrderCondition});
        String s = ObjectMapperHolder.objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(recentOrders);
        log.info("result:{}", s);
    }

}
