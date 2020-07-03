package com.ymm.ebatis.sample;

import com.ymm.ebatis.sample.entity.RecentOrderModel;
import com.ymm.ebatis.sample.entity.RecentOrderModelScript;
import com.ymm.ebatis.sample.mapper.RecentOrderUpdateMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.DocWriteResponse.Result;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.index.get.GetResult;
import org.elasticsearch.rest.RestStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;


/**
 * @author weilong.hu
 * @since 2020/6/29 19:40
 */
@Slf4j
public class ESUpdateTest extends ESAbstractTest {
    private RecentOrderUpdateMapper recentOrderUpdateMapper;

    @Before
    public void startup() {
        recentOrderUpdateMapper = createEsMapper(RecentOrderUpdateMapper.class);
    }

    @Test
    @SneakyThrows
    public void updateRecentOrder() {
        UpdateResponse updateResponse = recentOrderUpdateMapper.updateRecentOrder(new RecentOrderModel());
        log.info("update result:{}", updateResponse);
    }

    @Test
    @SneakyThrows
    public void updateRecentOrderScript() {
        UpdateResponse updateResponse = recentOrderUpdateMapper.updateRecentOrder(new RecentOrderModelScript());
        log.info("update result:{}", updateResponse);
    }


    @Test
    @SneakyThrows
    public void updateRecentOrderGetResult() {
        GetResult getResult = recentOrderUpdateMapper.updateRecentOrderGetResult(new RecentOrderModel());
        log.info("update result:{}", getResult);
    }

    @Test
    @SneakyThrows
    public void updateRecentOrderRestStatus() {
        RestStatus restStatus = recentOrderUpdateMapper.updateRecentOrderRestStatus(new RecentOrderModel());
        log.info("update result:{}", restStatus);
    }

    @Test
    @SneakyThrows
    public void updateRecentOrderBoolean() {
        Boolean bool = recentOrderUpdateMapper.updateRecentOrderBoolean(new RecentOrderModel());
        log.info("update result:{}", bool);
    }

    @Test
    @SneakyThrows
    public void updateRecentOrderBool() {
        boolean bool = recentOrderUpdateMapper.updateRecentOrderBool(new RecentOrderModel());
        log.info("update result:{}", bool);
    }

    @Test
    @SneakyThrows
    public void updateRecentOrderResult() {
        Result result = recentOrderUpdateMapper.updateRecentOrderResult(new RecentOrderModel());
        log.info("update result:{}", result);
    }

    @Test
    @SneakyThrows
    public void updateRecentOrderVoid() {
        recentOrderUpdateMapper.updateRecentOrderVoid(new RecentOrderModel());
        log.info("update success");
    }

    @Test
    @SneakyThrows
    public void updateRecentOrderFuture() {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        AtomicReference<Throwable> ex = new AtomicReference<>();
        CompletableFuture<Result> resultCompletableFuture = recentOrderUpdateMapper.updateRecentOrderFuture(new RecentOrderModel());
        resultCompletableFuture.whenCompleteAsync((r, e) -> {
            log.info("update result:{}", r);
            ex.set(e);
            countDownLatch.countDown();
        });
        countDownLatch.await();
        log.info("update success:{}", resultCompletableFuture.get());
        Assert.assertNull(ex.get());
    }


    @Test
    @SneakyThrows
    public void updateRecentOrderFutureVoid() {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        AtomicReference<Throwable> ex = new AtomicReference<>();
        CompletableFuture<Void> resultCompletableFuture = recentOrderUpdateMapper.updateRecentOrderFutureVoid(new RecentOrderModel());
        resultCompletableFuture.whenCompleteAsync((r, e) -> {
            log.info("update result:{}", r);
            ex.set(e);
            countDownLatch.countDown();
        });
        countDownLatch.await();
        log.info("update success");
        Assert.assertNull(ex.get());
    }
}
