package com.ymm.ebatis.sample;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import com.ymm.ebatis.sample.entity.RecentOrder;
import com.ymm.ebatis.sample.entity.RecentOrderModel;
import com.ymm.ebatis.sample.mapper.RecentOrderMultiGetMapper;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * @author weilong.hu
 * @since 2020/7/2 14:33
 */
@Slf4j
public class ESMultiGetTest extends ESAbstractTest {
    private RecentOrderMultiGetMapper recentOrderMultiGetMapper;

    @Before
    public void startup() {
        recentOrderMultiGetMapper = createEsMapper(RecentOrderMultiGetMapper.class);
    }

    @Test
    public void getRecentOrderLong() throws JsonProcessingException {
        RecentOrderModel rom1 = new RecentOrderModel();
        RecentOrderModel rom2 = new RecentOrderModel();
        rom2.setCargoId(10124512292668L);
        RecentOrder[] recentOrders = recentOrderMultiGetMapper.getRecentOrders(Lists.newArrayList(rom1, rom2));
        String result = getJsonResult(recentOrders);
        log.info("recentOrder:{}", result);
    }

    @Test
    public void getRecentOrdersResponse() throws JsonProcessingException {
        MultiGetResponse recentOrdersResponse = recentOrderMultiGetMapper.getRecentOrdersResponse(10124512292666L, 10124512292668L);
        String result = getJsonResult(recentOrdersResponse);
        log.info("recentOrder:{}", result);
    }

    @Test
    public void getRecentOrdersResponseWithid() throws JsonProcessingException {
        MultiGetResponse recentOrdersResponse = recentOrderMultiGetMapper.getRecentOrdersResponse(10124512292666L);
        String result = getJsonResult(recentOrdersResponse);
        log.info("recentOrder:{}", result);
    }

    @Test
    public void getRecentOrdersResponseWithModel() throws JsonProcessingException {
        MultiGetResponse recentOrdersResponse = recentOrderMultiGetMapper.getRecentOrdersResponse(new RecentOrderModel());
        String result = getJsonResult(recentOrdersResponse);
        log.info("recentOrder:{}", result);
    }

    @Test
    public void getRecentOrdersResponseWithList() throws JsonProcessingException {
        List<RecentOrder> recentOrders = recentOrderMultiGetMapper.getRecentOrders(10124512292666L, 10124512292668L);
        String result = getJsonResult(recentOrders);
        log.info("recentOrder:{}", result);
    }

    @Test
    public void getRecentOrdersItemResponse() throws JsonProcessingException {
        MultiGetItemResponse[] recentOrdersItemResponse = recentOrderMultiGetMapper.getRecentOrdersItemResponse(10124512292666L, 10124512292668L);
        String result = getJsonResult(recentOrdersItemResponse);
        log.info("recentOrder:{}", result);
    }


    @Test
    public void getRecentOrdersItemResponseWithList() throws JsonProcessingException {
        RecentOrderModel rom1 = new RecentOrderModel();
        RecentOrderModel rom2 = new RecentOrderModel();
        rom2.setCargoId(10124512292668L);
        List<MultiGetItemResponse> recentOrdersItemResponse = recentOrderMultiGetMapper.getRecentOrdersItemResponse(Lists.newArrayList(rom1, rom2));
        String result = getJsonResult(recentOrdersItemResponse);
        log.info("recentOrder:{}", result);
    }

    @Test
    public void getRecentOrdersResponseWithOptional() throws JsonProcessingException {
        List<Optional<RecentOrder>> recentOrders = recentOrderMultiGetMapper.getRecentOrdersOptional(10124512292666L, 10124512292668L);
        for (Optional<RecentOrder> recentOrder : recentOrders) {
            if (recentOrder.isPresent()) {
                String result = getJsonResult(recentOrder.get());
                log.info("recentOrder:{}", result);
            } else {
                log.info("recentOrder is not exist!");
            }
        }

    }

    @Test
    public void getRecentOrdersOptional() throws JsonProcessingException {
        RecentOrderModel rom1 = new RecentOrderModel();
        RecentOrderModel rom2 = new RecentOrderModel();
        rom2.setCargoId(10124512292668L);
        Optional<RecentOrder>[] recentOrders = recentOrderMultiGetMapper.getRecentOrdersOptional(Lists.newArrayList(rom1, rom2));
        for (Optional<RecentOrder> recentOrder : recentOrders) {
            if (recentOrder.isPresent()) {
                String result = getJsonResult(recentOrder.get());
                log.info("recentOrder:{}", result);
            } else {
                log.info("recentOrder is not exist!");
            }
        }
    }

    @Test
    public void getRecentOrdersOptionalFuture() {
        RecentOrderModel rom1 = new RecentOrderModel();
        RecentOrderModel rom2 = new RecentOrderModel();
        rom2.setCargoId(10124512292668L);
        CompletableFuture<Optional<RecentOrder>[]> recentOrdersOptionalFuture = recentOrderMultiGetMapper.getRecentOrdersOptionalFuture(Lists.newArrayList(rom1, rom2));
        recentOrdersOptionalFuture.whenComplete((recentOrders, ex) -> {
            for (Optional<RecentOrder> recentOrder : recentOrders) {
                if (recentOrder.isPresent()) {
                    try {
                        String result = getJsonResult(recentOrder.get());
                        log.info("recentOrder:{}", result);
                    } catch (JsonProcessingException e) {
                        log.error("recentOrder", e);
                    }

                } else {
                    log.info("recentOrder is not exist!");
                }
            }
        }).join();

    }
}
