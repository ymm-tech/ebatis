package com.ymm.ebatis.sample;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ymm.ebatis.sample.entity.RecentOrderModel;
import com.ymm.ebatis.sample.mapper.RecentOrderDeleteMapper;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.rest.RestStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author weilong.hu
 * @since 2020/6/29 16:58
 */
@Slf4j
public class ESDeleteTest extends ESAbstractTest {
    private RecentOrderDeleteMapper recentOrderDeleteMapper;

    @Before
    public void startup() {
        recentOrderDeleteMapper = createEsMapper(RecentOrderDeleteMapper.class);
    }

    @Test
    public void deleteRecentOrder() {
        RestStatus restStatus = recentOrderDeleteMapper.deleteRecentOrder(10124512292666L);
        log.info("restStatus:{}", restStatus);
    }

    @Test
    public void deleteRecentOrderModel() {
        RestStatus restStatus = recentOrderDeleteMapper.deleteRecentOrder(new RecentOrderModel());
        log.info("delete restStatus:{}", restStatus);
    }

    @Test
    public void deleteRecentOrderDeleteResponse() throws JsonProcessingException {
        DeleteResponse deleteResponse = recentOrderDeleteMapper.deleteRecentOrderDeleteResponse(10124512292666L);
        String result = getJsonResult(deleteResponse);
        log.info("delete response:{}", result);
    }

    @Test
    public void deleteRecentOrderBoolean() {
        Boolean bool = recentOrderDeleteMapper.deleteRecentOrderBoolean(10124512292666L);
        log.info("delete result:{}", bool);
    }

    @Test
    public void deleteRecentOrderBool() {
        boolean bool = recentOrderDeleteMapper.deleteRecentOrderBool(10124512292666L);
        log.info("delete result:{}", bool);
    }

    @Test
    public void deleteRecentOrderVoid() {
        recentOrderDeleteMapper.deleteRecentOrderVoid(10124512292666L);
        log.info("delete over");
    }


    @Test
    public void deleteRecentOrderBooleanFuture() throws InterruptedException, ExecutionException {
        AtomicReference<Throwable> ex = new AtomicReference<>();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        CompletableFuture<Boolean> bool = recentOrderDeleteMapper.deleteRecentOrderBooleanFuture(10124512292666L);
        bool.whenCompleteAsync((r, e) -> {
            log.info("delete result:{}", r);
            ex.set(e);
            countDownLatch.countDown();
        });
        countDownLatch.await();
        log.info("delete success restStatus：{}", bool.get());
        Assert.assertNull(ex.get());
    }


    @Test
    public void deleteRecentOrderVoidFuture() throws InterruptedException {
        AtomicReference<Throwable> ex = new AtomicReference<>();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        CompletableFuture<Void> voidCompletableFuture = recentOrderDeleteMapper.deleteRecentOrderVoidFuture(10124512292666L);
        voidCompletableFuture.whenCompleteAsync((r, e) -> {
            log.info("delete result:{}", r);
            ex.set(e);
            countDownLatch.countDown();
        });
        countDownLatch.await();
        log.info("delete success restStatus");
        Assert.assertNull(ex.get());
    }
}
