package com.ymm.ebatis.sample;

import com.google.common.collect.Lists;
import com.ymm.ebatis.core.domain.Page;
import com.ymm.ebatis.core.domain.Pageable;
import com.ymm.ebatis.core.domain.Range;
import com.ymm.ebatis.core.domain.Script;
import com.ymm.ebatis.sample.condition.RecentOrderCondition;
import com.ymm.ebatis.sample.condition.SampleRecentOrderCondition;
import com.ymm.ebatis.sample.condition.base.Protocol;
import com.ymm.ebatis.sample.condition.base.RateMode;
import com.ymm.ebatis.sample.condition.base.SecurityTran;
import com.ymm.ebatis.sample.condition.base.ShipperInfo;
import com.ymm.ebatis.sample.entity.RecentOrder;
import com.ymm.ebatis.sample.mapper.RecentOrderMultiSearchMapper;
import com.ymm.ebatis.sample.mapper.RecentOrderSearchMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

/**
 * @author weilong.hu
 * @since 2020/6/15 13:32
 */
@Slf4j
public class EsQueryTest extends ESAbstractTest {
    private RecentOrderSearchMapper recentOrderMapper;
    private RecentOrderMultiSearchMapper recentOrderMultiSearchMapper;

    @Before
    public void startup() {
        recentOrderMapper = createEsMapper(RecentOrderSearchMapper.class);
        recentOrderMultiSearchMapper = createEsMapper(RecentOrderMultiSearchMapper.class);
    }


    @SneakyThrows
    @Test
    public void queryRecentOrderArray() {
        RecentOrderCondition condition = getCondition();
        condition.setCargoId(10124512292911L);
        RecentOrder[] recentOrders = recentOrderMapper.queryRecentOrderArray(condition);
        String s = getJsonResult(recentOrders);
        log.info("result:{}", s);
    }

    @SneakyThrows
    @Test
    public void queryRecentOrderPage() {
        RecentOrderCondition condition = getCondition();
        Page<RecentOrder> recentOrders = recentOrderMapper.queryRecentOrderPage(Pageable.of(1, 10), condition);
        String s = getJsonResult(recentOrders);
        log.info("result:{}", s);
    }


    @SneakyThrows
    @Test
    public void queryRecentOrderList() {
        RecentOrderCondition condition = getCondition();
        condition.setCargoId(10124512292911L);
        List<RecentOrder> recentOrders = recentOrderMapper.queryRecentOrderList(condition);
        String s = getJsonResult(recentOrders);
        log.info("result:{}", s);
    }

    @SneakyThrows
    @Test
    public void queryRecentOrderSearchResponse() {
        RecentOrderCondition condition = getCondition();
        condition.setCargoId(10124512292911L);
        SearchResponse searchResponse = recentOrderMapper.queryRecentOrderSearchResponse(condition);
        String s = getJsonResult(searchResponse);
        log.info("result:{}", s);
    }

    @SneakyThrows
    @Test
    public void queryRecentOrderCompletableFutureList() {
        RecentOrderCondition condition = getCondition();
        condition.setCargoId(10124512292911L);
        CompletableFuture<List<RecentOrder>> listCompletableFuture = recentOrderMapper.queryRecentOrderCompletableFutureList(condition);
        List<RecentOrder> recentOrders = listCompletableFuture.get();
        String s = getJsonResult(recentOrders);
        log.info("result:{}", s);
    }

    @SneakyThrows
    @Test
    public void queryRecentOrderArrayScoreFunction() {
        RecentOrderCondition condition = getCondition();
        condition.setCargoId(10124512292911L);
        RecentOrder[] recentOrders = recentOrderMapper.queryRecentOrderArrayScoreFunction(condition);
        String s = getJsonResult(recentOrders);
        log.info("result:{}", s);
    }

    @SneakyThrows
    @Test
    public void queryRecentOrder() {
        RecentOrder recentOrder = recentOrderMapper.queryRecentOrder(new SampleRecentOrderCondition());
        String s = getJsonResult(recentOrder);
        log.info("result:{}", s);
    }

    @SneakyThrows
    @Test
    public void queryRecentOrderCount() {
        Long count = recentOrderMapper.queryRecentOrderCount(new SampleRecentOrderCondition());
        String s = getJsonResult(count);
        log.info("RecentOrder count:{}", s);
    }

    @SneakyThrows
    @Test
    public void queryRecentOrderCountWithBase() {
        long count = recentOrderMapper.queryRecentOrderCount();
        String s = getJsonResult(count);
        log.info("RecentOrder count:{}", s);
    }

    @SneakyThrows
    @Test
    public void queryRecentOrderCountWithBoolean() {
        Boolean bool = recentOrderMapper.queryRecentOrderCountWithBoolean(new SampleRecentOrderCondition());
        String s = getJsonResult(bool);
        log.info("RecentOrder exist:{}", s);
    }


    @SneakyThrows
    @Test
    public void queryRecentOrderCountWithBool() {
        boolean bool = recentOrderMapper.queryRecentOrderCountWithBool(getCondition());
        String s = getJsonResult(bool);
        log.info("RecentOrder exist:{}", s);
    }

    @SneakyThrows
    @Test
    public void multiSearchListPageWithArray() {
        RecentOrderCondition condition = getCondition();
        condition.setCargoId(10124512292966L);
        SampleRecentOrderCondition sampleRecentOrderCondition = new SampleRecentOrderCondition();
        sampleRecentOrderCondition.setCargoId(10124512292911L);
        List<Page<RecentOrder>> pages = recentOrderMultiSearchMapper.queryRecentOrderListPage(
                new SampleRecentOrderCondition[]{condition, sampleRecentOrderCondition},
                new Pageable[]{Pageable.of(0, 10), Pageable.of(1, 10)});
        String s = getJsonResult(pages);
        log.info("result:{}", s);
    }

    @SneakyThrows
    @Test
    public void multiSearchListPageWithList() {
        RecentOrderCondition condition = getCondition();
        condition.setCargoId(10124512292966L);
        SampleRecentOrderCondition sampleRecentOrderCondition = new SampleRecentOrderCondition();
        sampleRecentOrderCondition.setCargoId(10124512292911L);
        List<Page<RecentOrder>> pages = recentOrderMultiSearchMapper.queryRecentOrderListPage(
                Lists.newArrayList(condition, sampleRecentOrderCondition),
                Lists.newArrayList(Pageable.of(0, 10), Pageable.of(1, 10)));
        String s = getJsonResult(pages);
        log.info("result:{}", s);
    }

    @SneakyThrows
    @Test
    public void multiSearchListPageWithSingle() {
        RecentOrderCondition condition = getCondition();
        condition.setCargoId(10124512292911L);
        List<Page<RecentOrder>> pages = recentOrderMultiSearchMapper.queryRecentOrderListPage(
                condition,
                Pageable.of(0, 10));
        String s = getJsonResult(pages);
        log.info("result:{}", s);
    }

    @SneakyThrows
    @Test
    public void multiSearchQueryRecentOrderPageArray() {
        RecentOrderCondition condition = getCondition();
        condition.setCargoId(10124512292966L);
        SampleRecentOrderCondition sampleRecentOrderCondition = new SampleRecentOrderCondition();
        sampleRecentOrderCondition.setCargoId(10124512292911L);
        Page<RecentOrder>[] pages = recentOrderMultiSearchMapper.queryRecentOrderPageArray(
                new SampleRecentOrderCondition[]{condition, sampleRecentOrderCondition},
                new Pageable[]{Pageable.of(0, 10), Pageable.of(1, 10)});
        String s = getJsonResult(pages);
        log.info("result:{}", s);
    }

    @SneakyThrows
    @Test
    public void multiSearchListListWithArray() {
        RecentOrderCondition condition = getCondition();
        condition.setCargoId(10124512292966L);
        SampleRecentOrderCondition sampleRecentOrderCondition = new SampleRecentOrderCondition();
        sampleRecentOrderCondition.setCargoId(10124512292911L);
        List<List<RecentOrder>> recentOrders = recentOrderMultiSearchMapper.queryRecentOrderListList(
                new SampleRecentOrderCondition[]{condition, sampleRecentOrderCondition});
        String s = getJsonResult(recentOrders);
        log.info("result:{}", s);
    }

    @SneakyThrows
    @Test
    public void multiSearchArrayArrayWithArray() {
        RecentOrderCondition condition = getCondition();
        condition.setCargoId(10124512292966L);
        SampleRecentOrderCondition sampleRecentOrderCondition = new SampleRecentOrderCondition();
        sampleRecentOrderCondition.setCargoId(10124512292911L);
        RecentOrder[][] recentOrders = recentOrderMultiSearchMapper.queryRecentOrderArrayArray(
                new SampleRecentOrderCondition[]{condition, sampleRecentOrderCondition});
        String s = getJsonResult(recentOrders);
        log.info("result:{}", s);
    }

    @SneakyThrows
    @Test
    public void multiSearchListArrayWithArray() {
        RecentOrderCondition condition = getCondition();
        condition.setCargoId(10124512292966L);
        SampleRecentOrderCondition sampleRecentOrderCondition = new SampleRecentOrderCondition();
        sampleRecentOrderCondition.setCargoId(10124512292911L);
        List<RecentOrder[]> recentOrders = recentOrderMultiSearchMapper.queryRecentOrderListArray(
                new SampleRecentOrderCondition[]{condition, sampleRecentOrderCondition});
        String s = getJsonResult(recentOrders);
        log.info("result:{}", s);
    }

    @SneakyThrows
    @Test
    public void multiSearchSearchResponse() {
        RecentOrderCondition condition = getCondition();
        condition.setCargoId(10124512292966L);
        SampleRecentOrderCondition sampleRecentOrderCondition = new SampleRecentOrderCondition();
        sampleRecentOrderCondition.setCargoId(10124512292911L);
        MultiSearchResponse multiSearchResponse = recentOrderMultiSearchMapper.queryRecentOrderMultiSearchResponse(
                new SampleRecentOrderCondition[]{condition, sampleRecentOrderCondition});
        String s = getJsonResult(multiSearchResponse);
        log.info("result:{}", s);
    }


    @SneakyThrows
    @Test
    public void multiSearchArrayArrayFuture() {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        RecentOrderCondition condition = getCondition();
        condition.setCargoId(10124512292966L);
        SampleRecentOrderCondition sampleRecentOrderCondition = new SampleRecentOrderCondition();
        sampleRecentOrderCondition.setCargoId(10124512292911L);
        CompletableFuture<RecentOrder[][]> completableFuture = recentOrderMultiSearchMapper.queryRecentOrderArrayArrayFuture(
                new SampleRecentOrderCondition[]{condition, sampleRecentOrderCondition});
        completableFuture.whenCompleteAsync((r, e) -> {
            log.info("result length:{}", r.length);
            countDownLatch.countDown();
        });
        countDownLatch.await();
        String s = getJsonResult(completableFuture.join());
        log.info("result:{}", s);
    }

    @SneakyThrows
    @Test
    public void multiSearchArrayListy() {
        RecentOrderCondition condition = getCondition();
        condition.setCargoId(10124512292966L);
        SampleRecentOrderCondition sampleRecentOrderCondition = new SampleRecentOrderCondition();
        sampleRecentOrderCondition.setCargoId(10124512292911L);
        List<RecentOrder>[] recentOrders = recentOrderMultiSearchMapper.queryRecentOrderArrayList(
                new SampleRecentOrderCondition[]{condition, sampleRecentOrderCondition});
        String s = getJsonResult(recentOrders);
        log.info("result:{}", s);
    }

    @SneakyThrows
    @Test
    public void queryRecentOrderCounts() {
        RecentOrderCondition condition = getCondition();
        condition.setCargoId(10124512292966L);
        SampleRecentOrderCondition sampleRecentOrderCondition = new SampleRecentOrderCondition();
        sampleRecentOrderCondition.setCargoId(10124512292911L);
        List<Long> count = recentOrderMultiSearchMapper.queryRecentOrderCount(
                new SampleRecentOrderCondition[]{condition, sampleRecentOrderCondition});
        String s = getJsonResult(count);
        log.info("result:{}", s);
    }

    @SneakyThrows
    @Test
    public void queryRecentOrderArrayCounts() {
        RecentOrderCondition condition = getCondition();
        condition.setCargoId(10124512292966L);
        SampleRecentOrderCondition sampleRecentOrderCondition = new SampleRecentOrderCondition();
        sampleRecentOrderCondition.setCargoId(10124512292911L);
        Long[] count = recentOrderMultiSearchMapper.queryRecentOrderCounts(
                new SampleRecentOrderCondition[]{condition, sampleRecentOrderCondition});
        String s = getJsonResult(count);
        log.info("result:{}", s);
    }

    @SneakyThrows
    @Test
    public void queryRecentOrderBasicCounts() {
        RecentOrderCondition condition = getCondition();
        condition.setCargoId(10124512292966L);
        SampleRecentOrderCondition sampleRecentOrderCondition = new SampleRecentOrderCondition();
        sampleRecentOrderCondition.setCargoId(10124512292911L);
        long[] count = recentOrderMultiSearchMapper.queryRecentOrderBasicCounts(
                new SampleRecentOrderCondition[]{condition, sampleRecentOrderCondition});
        String s = getJsonResult(count);
        log.info("result:{}", s);
    }

    @SneakyThrows
    @Test
    public void queryRecentOrderBooleanCounts() {
        RecentOrderCondition condition = getCondition();
        condition.setCargoId(10124512292966L);
        SampleRecentOrderCondition sampleRecentOrderCondition = new SampleRecentOrderCondition();
        sampleRecentOrderCondition.setCargoId(10124512292911L);
        List<Boolean> bool = recentOrderMultiSearchMapper.queryRecentOrderBooleanCounts(
                new SampleRecentOrderCondition[]{condition, sampleRecentOrderCondition});
        String s = getJsonResult(bool);
        log.info("result:{}", s);
    }

    @SneakyThrows
    @Test
    public void queryRecentOrderBooleanArrayCounts() {
        RecentOrderCondition condition = getCondition();
        condition.setCargoId(10124512292966L);
        SampleRecentOrderCondition sampleRecentOrderCondition = new SampleRecentOrderCondition();
        sampleRecentOrderCondition.setCargoId(10124512292911L);
        Boolean[] bool = recentOrderMultiSearchMapper.queryRecentOrderBooleanArrayCounts(
                new SampleRecentOrderCondition[]{condition, sampleRecentOrderCondition});
        String s = getJsonResult(bool);
        log.info("result:{}", s);
    }

    @SneakyThrows
    @Test
    public void queryRecentOrderBoolArrayCounts() {
        RecentOrderCondition condition = getCondition();
        condition.setCargoId(10124512292966L);
        SampleRecentOrderCondition sampleRecentOrderCondition = new SampleRecentOrderCondition();
        sampleRecentOrderCondition.setCargoId(10124512292911L);
        boolean[] bool = recentOrderMultiSearchMapper.queryRecentOrderBoolArrayCounts(
                new SampleRecentOrderCondition[]{condition, sampleRecentOrderCondition});
        String s = getJsonResult(bool);
        log.info("result:{}", s);
    }

    protected RecentOrderCondition getCondition() {
        RecentOrderCondition condition = new RecentOrderCondition();
        condition.setCargoId(10124512292911L);
        condition.setCargoType(2);
        condition.setOrderSource(Lists.newArrayList(2, 4, 6, 8));
        condition.setType(new Integer[]{1, 3, 5, 7});

        Protocol protocol = new Protocol();
        protocol.setProtocolStatus(0);
        RateMode rateMode = new RateMode();
        rateMode.setRateModeFlag(0);
        protocol.setRateMode(rateMode);
        condition.setProtocol(protocol);

        condition.setSecurityTranList(Lists.newArrayList(
                SecurityTran.builder().securityTran(Lists.newArrayList(1, 2, 3)).build(),
                SecurityTran.builder().securityTran(Lists.newArrayList(4, 5, 6)).build()));

        condition.setSecurityTrans(new SecurityTran[]{
                SecurityTran.builder().securityTran(Lists.newArrayList(7, 8, 9)).build(),
                SecurityTran.builder().securityTran(Lists.newArrayList(10, 11, 12)).build()});

        condition.setStartAreaCode(false);
        condition.setChannel(Range.of(1, 100).closeLeft());
        condition.setScript(Script.stored("666"));
        condition.setChannels(Lists.newArrayList(Range.of(100, 200).closeLeft(), Range.of(300, 500).closeLeft()));
        condition.setScripts(new Script[]{Script.stored("888"), Script.stored("1024")});
        condition.setShipperInfo(ShipperInfo.builder().shipperTelephone(18030000725L).shipperTelephoneMask(999725L).shipperUserId(123321L).build());
        condition.setShipperInfos(new Object[]{ShipperInfo.builder().shipperTelephone(18031111725L).shipperTelephoneMask(999726L).shipperUserId(456654L).build()});
        condition.setUnloadAddress("**沈阳市皇姑区**");
        return condition;
    }
}
