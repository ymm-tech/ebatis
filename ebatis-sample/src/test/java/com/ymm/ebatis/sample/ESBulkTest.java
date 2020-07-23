package com.ymm.ebatis.sample;

import com.google.common.collect.Lists;
import com.ymm.ebatis.sample.entity.RecentOrderModel;
import com.ymm.ebatis.sample.entity.RecentOrderModelScript;
import com.ymm.ebatis.sample.mapper.RecentOrderBulkMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkResponse;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author weilong.hu
 * @since 2020/7/1 9:56
 */
@Slf4j
public class ESBulkTest extends ESAbstractTest {
    private RecentOrderBulkMapper recentOrderBulkMapper;

    @Before
    public void startup() {
        recentOrderBulkMapper = createEsMapper(RecentOrderBulkMapper.class);
    }

    @Test
    @SneakyThrows
    public void bulkIndexRecentOrderList() {
        RecentOrderModel rom1 = new RecentOrderModel();
        RecentOrderModel rom2 = new RecentOrderModel();
        rom2.setCargoId(10124512292667L);
        List<BulkItemResponse> bulkItemResponses = recentOrderBulkMapper.bulkIndexRecentOrderList(Lists.newArrayList(rom1, rom2));
        String result = getJsonResult(bulkItemResponses);
        log.info("result:{}", result);
    }

    @Test
    @SneakyThrows
    public void bulkIndexRecentOrderListWithArray() {
        RecentOrderModel rom1 = new RecentOrderModel();
        RecentOrderModel rom2 = new RecentOrderModel();
        rom2.setCargoId(10124512292667L);
        List<BulkItemResponse> bulkItemResponses = recentOrderBulkMapper.bulkIndexRecentOrderList(rom1, rom2);
        String result = getJsonResult(bulkItemResponses);
        log.info("result:{}", result);
    }

    @Test
    @SneakyThrows
    public void bulkIndexRecentOrderArray() {
        RecentOrderModel rom1 = new RecentOrderModel();
        RecentOrderModel rom2 = new RecentOrderModel();
        rom2.setCargoId(10124512292667L);
        BulkItemResponse[] bulkItemResponses = recentOrderBulkMapper.bulkIndexRecentOrderArray(rom1, rom2);
        String result = getJsonResult(bulkItemResponses);
        log.info("result:{}", result);
    }


    @Test
    @SneakyThrows
    public void bulkIndexRecentOrderBulkResponse() {
        RecentOrderModel rom1 = new RecentOrderModel();
        RecentOrderModel rom2 = new RecentOrderModel();
        rom2.setCargoId(10124512292667L);
        BulkResponse bulkResponse = recentOrderBulkMapper.bulkIndexRecentOrderBulkResponse(rom1, rom2);
        String result = getJsonResult(bulkResponse);
        log.info("result:{}", result);
    }

    @Test
    @SneakyThrows
    public void bulkDeleteRecentOrderListWithIds() {
        List<BulkItemResponse> bulkItemResponses = recentOrderBulkMapper.bulkDeleteRecentOrderList(10124512292666L, 10124512292667L);
        String result = getJsonResult(bulkItemResponses);
        log.info("result:{}", result);
    }

    @Test
    @SneakyThrows
    public void bulkDeleteRecentOrderListWithModes() {
        RecentOrderModel rom1 = new RecentOrderModel();
        RecentOrderModel rom2 = new RecentOrderModel();
        rom2.setCargoId(10124512292667L);
        List<BulkItemResponse> bulkItemResponses = recentOrderBulkMapper.bulkDeleteRecentOrderList(Lists.newArrayList(rom1, rom2));
        String result = getJsonResult(bulkItemResponses);
        log.info("result:{}", result);
    }

    @Test
    @SneakyThrows
    public void bulkUpdateRecentOrderList() {
        RecentOrderModel rom1 = new RecentOrderModel();
        RecentOrderModel rom2 = new RecentOrderModel();
        rom2.setCargoId(10124512292667L);
        List<BulkItemResponse> bulkItemResponses = recentOrderBulkMapper.bulkUpdateRecentOrderList(rom1, rom2);
        for (BulkItemResponse bulkItemResponse : bulkItemResponses) {
            String result = getJsonResult(bulkItemResponse.getOpType());
            log.info("result:{}", result);
            log.info("msg:{}", bulkItemResponse.getFailureMessage());
        }
    }

    @Test
    @SneakyThrows
    public void bulkUpdateRecentOrderListWithScript() {
        RecentOrderModelScript rom1 = new RecentOrderModelScript();
        RecentOrderModelScript rom2 = new RecentOrderModelScript();
        rom2.setCargoId(10124512292667L);
        List<BulkItemResponse> bulkItemResponses = recentOrderBulkMapper.bulkUpdateRecentOrderList(Lists.newArrayList(rom1, rom2));
        for (BulkItemResponse bulkItemResponse : bulkItemResponses) {
            String result = getJsonResult(bulkItemResponse.getOpType());
            log.info("result:{}", result);
            log.info("msg:{}", bulkItemResponse.getFailureMessage());
        }
    }

    @Test
    @SneakyThrows
    public void bulkUpdateRecentOrderListFuture() {
        RecentOrderModelScript rom1 = new RecentOrderModelScript();
        RecentOrderModelScript rom2 = new RecentOrderModelScript();
        rom2.setCargoId(10124512292667L);
        CompletableFuture<List<BulkItemResponse>> bulkItemResponseFuture =
                recentOrderBulkMapper.bulkUpdateRecentOrderListFuture(Lists.newArrayList(rom1, rom2));
        bulkItemResponseFuture.handle((bulkItemResponses, e) -> {
            if (e != null) {
                throw new RuntimeException(e);
            } else {
                for (BulkItemResponse bulkItemResponse : bulkItemResponses) {
                    String result = getJsonResult(bulkItemResponse.getOpType());
                    log.info("result:{}", result);
                    log.info("msg:{}", bulkItemResponse.getFailureMessage());
                }
            }
            return "success";
        }).join();
    }

}
