package com.ymm.ebatis.sample;

import com.ymm.ebatis.core.common.ObjectMapperHolder;
import com.ymm.ebatis.core.proxy.MapperProxyFactory;
import com.ymm.ebatis.sample.cluster.SampleClusterRouterProvider;
import com.ymm.ebatis.sample.entity.RecentOrder;
import com.ymm.ebatis.sample.entity.RecentOrderModel;
import com.ymm.ebatis.sample.mapper.RecentOrderGetMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.GetResponse;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * @author weilong.hu
 * @since 2020/6/29 10:53
 */
@Slf4j
public class ESGetTest {
    private RecentOrderGetMapper recentOrderGetMapper;

    @Before
    public void startup() {
        recentOrderGetMapper = createEsMapper(RecentOrderGetMapper.class);
    }

    protected <R> R createEsMapper(Class<R> mapperClass) {
        return MapperProxyFactory.getMapperProxy(mapperClass, SampleClusterRouterProvider.SAMPLE_CLUSTER_NAME);
    }

    @Test
    @SneakyThrows
    public void getRecentOrderLong() {
        RecentOrder recentOrder = recentOrderGetMapper.getRecentOrder(10124512292666L);
        String result = ObjectMapperHolder.objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(recentOrder);
        log.info("recentOrder:{}", result);
    }

    @Test
    @SneakyThrows
    public void getRecentOrderString() {
        RecentOrder recentOrder = recentOrderGetMapper.getRecentOrder("10124512292666");
        String result = ObjectMapperHolder.objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(recentOrder);
        log.info("recentOrder:{}", result);
    }

    @Test
    @SneakyThrows
    public void getRecentOrderModel() {
        RecentOrder recentOrder = recentOrderGetMapper.getRecentOrder(new RecentOrderModel());
        String result = ObjectMapperHolder.objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(recentOrder);
        log.info("recentOrder:{}", result);
    }

    @Test
    @SneakyThrows
    public void getRecentOrderOptional() {
        Optional<RecentOrder> recentOrder = recentOrderGetMapper.getRecentOrderOptional(10124512292666L);
        String result = ObjectMapperHolder.objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(recentOrder.orElse(null));
        log.info("recentOrder:{}", result);
    }

    @Test
    @SneakyThrows
    public void getRecentOrderGetResponse() {
        GetResponse getResponse = recentOrderGetMapper.getRecentOrderGetResponse(10124512292666L);
        String result = ObjectMapperHolder.objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(getResponse);
        log.info("recentOrder:{}", result);
    }


    @Test
    @SneakyThrows
    public void getRecentOrderCompletableFutureWithOption() {
        CompletableFuture<Optional<RecentOrder>> recentOrder = recentOrderGetMapper.getRecentOrderCompletableFuture(10124512292666L);
        String result = ObjectMapperHolder.objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(recentOrder.get().orElse(null));
        log.info("recentOrder:{}", result);
    }

    @Test
    @SneakyThrows
    public void getRecentOrderCompletableFuture() {
        CompletableFuture<RecentOrder> recentOrder = recentOrderGetMapper.getRecentOrderCompletableFuture("10124512292666");
        String result = ObjectMapperHolder.objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(recentOrder.get());
        log.info("recentOrder:{}", result);
    }
}
