package com.ymm.ebatis.sample;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ymm.ebatis.sample.condition.RecentOrderScriptCondition;
import com.ymm.ebatis.sample.mapper.RecentOrderUpdateByQueryMapper;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.BulkByScrollTask;
import org.junit.Before;
import org.junit.Test;

/**
 * @author weilong.hu
 * @since 2020/7/2 17:01
 */
@Slf4j
public class ESUpdateByQueryTest extends ESAbstractTest {
    private RecentOrderUpdateByQueryMapper recentOrderUpdateByQueryMapper;

    @Before
    public void startup() {
        recentOrderUpdateByQueryMapper = createEsMapper(RecentOrderUpdateByQueryMapper.class);
    }

    @Test
    public void getRecentOrderLong() throws JsonProcessingException {
        RecentOrderScriptCondition sroc = new RecentOrderScriptCondition();
        sroc.setCargoId(10124512292666L);
        BulkByScrollResponse bulkByScrollResponse = recentOrderUpdateByQueryMapper.updateByQueryRecentOrder(sroc);
        String result = getJsonResult(bulkByScrollResponse);
        log.info("result:{}", result);
    }

    @Test
    public void updateByQueryRecentOrderStatus() throws JsonProcessingException {
        RecentOrderScriptCondition sroc = new RecentOrderScriptCondition();
        sroc.setCargoId(10124512292666L);
        BulkByScrollTask.Status status = recentOrderUpdateByQueryMapper.updateByQueryRecentOrderStatus(sroc);
        String result = getJsonResult(status);
        log.info("result:{}", result);
    }

    @Test
    public void updateByQueryRecentOrderFuture() {
        RecentOrderScriptCondition sroc = new RecentOrderScriptCondition();
        sroc.setCargoId(10124512292666L);
        recentOrderUpdateByQueryMapper.updateByQueryRecentOrderFuture(sroc).whenComplete((response, ex) -> {
            try {
                String result = getJsonResult(response);
                log.info("result:{}", result);
            } catch (JsonProcessingException e) {
                log.error("update by query exception", e);
            }
        }).join();

    }
}
