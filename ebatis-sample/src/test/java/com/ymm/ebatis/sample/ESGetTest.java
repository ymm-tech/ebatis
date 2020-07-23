package com.ymm.ebatis.sample;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ymm.ebatis.sample.entity.RecentOrder;
import com.ymm.ebatis.sample.entity.RecentOrderModel;
import com.ymm.ebatis.sample.mapper.RecentOrderGetMapper;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.GetResponse;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author weilong.hu
 * @since 2020/6/29 10:53
 */
@Slf4j
public class ESGetTest extends ESAbstractTest {
    private RecentOrderGetMapper recentOrderGetMapper;

    @Before
    public void startup() {
        recentOrderGetMapper = createEsMapper(RecentOrderGetMapper.class);
    }

    @Test
    public void getRecentOrderLong() throws JsonProcessingException {
        RecentOrder recentOrder = recentOrderGetMapper.getRecentOrder(10124512292666L);
        String result = getJsonResult(recentOrder);
        log.info("recentOrder:{}", result);
    }

    @Test
    public void getRecentOrderString() throws JsonProcessingException {
        RecentOrder recentOrder = recentOrderGetMapper.getRecentOrder("10124512292666");
        String result = getJsonResult(recentOrder);
        log.info("recentOrder:{}", result);
    }

    @Test
    public void getRecentOrderModel() throws JsonProcessingException {
        RecentOrder recentOrder = recentOrderGetMapper.getRecentOrder(new RecentOrderModel());
        String result = getJsonResult(recentOrder);
        log.info("recentOrder:{}", result);
    }

    @Test
    public void getRecentOrderOptional() throws JsonProcessingException {
        Optional<RecentOrder> recentOrder = recentOrderGetMapper.getRecentOrderOptional(10124512292666L);
        String result = getJsonResult(recentOrder.orElse(null));
        log.info("recentOrder:{}", result);
    }

    @Test
    public void getRecentOrderGetResponse() throws JsonProcessingException {
        GetResponse getResponse = recentOrderGetMapper.getRecentOrderGetResponse(10124512292666L);
        String result = getJsonResult(getResponse);
        log.info("recentOrder:{}", result);
    }


    @Test

    public void getRecentOrderCompletableFutureWithOption() throws ExecutionException, InterruptedException, JsonProcessingException {
        CompletableFuture<Optional<RecentOrder>> recentOrder = recentOrderGetMapper.getRecentOrderCompletableFuture(10124512292666L);
        String result = getJsonResult(recentOrder.get().orElse(null));
        log.info("recentOrder:{}", result);
    }

    @Test

    public void getRecentOrderCompletableFuture() throws ExecutionException, InterruptedException, JsonProcessingException {
        CompletableFuture<RecentOrder> recentOrder = recentOrderGetMapper.getRecentOrderCompletableFuture("10124512292666");
        String result = getJsonResult(recentOrder.get());
        log.info("recentOrder:{}", result);
    }
}
