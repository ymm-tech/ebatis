package io.manbang.ebatis.sample;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import io.manbang.ebatis.sample.entity.RecentOrderModel;
import io.manbang.ebatis.sample.entity.RecentOrderModelScript;
import io.manbang.ebatis.sample.mapper.RecentOrderBulkMapper;
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
    public void bulkIndexRecentOrderList() throws JsonProcessingException {
        RecentOrderModel rom1 = new RecentOrderModel();
        RecentOrderModel rom2 = new RecentOrderModel();
        rom2.setCargoId(10124512292667L);
        List<BulkItemResponse> bulkItemResponses = recentOrderBulkMapper.bulkIndexRecentOrderList(Lists.newArrayList(rom1, rom2));
        String result = getJsonResult(bulkItemResponses);
        log.info("result:{}", result);
    }

    @Test
    public void bulkIndexRecentOrderListWithArray() throws JsonProcessingException {
        RecentOrderModel rom1 = new RecentOrderModel();
        RecentOrderModel rom2 = new RecentOrderModel();
        rom2.setCargoId(10124512292667L);
        List<BulkItemResponse> bulkItemResponses = recentOrderBulkMapper.bulkIndexRecentOrderList(rom1, rom2);
        String result = getJsonResult(bulkItemResponses);
        log.info("result:{}", result);
    }

    @Test
    public void bulkIndexRecentOrderArray() throws JsonProcessingException {
        RecentOrderModel rom1 = new RecentOrderModel();
        RecentOrderModel rom2 = new RecentOrderModel();
        rom2.setCargoId(10124512292667L);
        BulkItemResponse[] bulkItemResponses = recentOrderBulkMapper.bulkIndexRecentOrderArray(rom1, rom2);
        String result = getJsonResult(bulkItemResponses);
        log.info("result:{}", result);
    }


    @Test
    public void bulkIndexRecentOrderBulkResponse() throws JsonProcessingException {
        RecentOrderModel rom1 = new RecentOrderModel();
        RecentOrderModel rom2 = new RecentOrderModel();
        rom2.setCargoId(10124512292667L);
        BulkResponse bulkResponse = recentOrderBulkMapper.bulkIndexRecentOrderBulkResponse(rom1, rom2);
        String result = getJsonResult(bulkResponse);
        log.info("result:{}", result);
    }

    @Test
    public void bulkDeleteRecentOrderListWithIds() throws JsonProcessingException {
        List<BulkItemResponse> bulkItemResponses = recentOrderBulkMapper.bulkDeleteRecentOrderList(10124512292666L, 10124512292667L);
        String result = getJsonResult(bulkItemResponses);
        log.info("result:{}", result);
    }

    @Test
    public void bulkDeleteRecentOrderListWithModes() throws JsonProcessingException {
        RecentOrderModel rom1 = new RecentOrderModel();
        RecentOrderModel rom2 = new RecentOrderModel();
        rom2.setCargoId(10124512292667L);
        List<BulkItemResponse> bulkItemResponses = recentOrderBulkMapper.bulkDeleteRecentOrderList(Lists.newArrayList(rom1, rom2));
        String result = getJsonResult(bulkItemResponses);
        log.info("result:{}", result);
    }

    @Test
    public void bulkUpdateRecentOrderList() throws JsonProcessingException {
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
    public void bulkUpdateRecentOrderListWithScript() throws JsonProcessingException {
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
                    try {
                        String result = getJsonResult(bulkItemResponse.getOpType());
                        log.info("result:{}", result);
                        log.info("msg:{}", bulkItemResponse.getFailureMessage());
                    } catch (JsonProcessingException ex) {
                        log.error("bulkUpdate exception", ex);
                    }

                }
            }
            return "success";
        }).join();
    }

}
