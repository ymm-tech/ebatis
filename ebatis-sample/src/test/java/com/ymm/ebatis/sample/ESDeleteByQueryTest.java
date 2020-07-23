package com.ymm.ebatis.sample;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ymm.ebatis.sample.condition.RecentOrderScriptCondition;
import com.ymm.ebatis.sample.mapper.RecentOrderDeleteByQueryMapper;
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
public class ESDeleteByQueryTest extends ESAbstractTest {
    private RecentOrderDeleteByQueryMapper recentOrderDeleteByQueryMapper;

    @Before
    public void startup() {
        recentOrderDeleteByQueryMapper = createEsMapper(RecentOrderDeleteByQueryMapper.class);
    }

    @Test
    public void getRecentOrderLong() throws JsonProcessingException {
        RecentOrderScriptCondition sroc = new RecentOrderScriptCondition();
        sroc.setCargoId(10124512292666L);
        BulkByScrollResponse bulkByScrollResponse = recentOrderDeleteByQueryMapper.deleteByQueryRecentOrder(sroc);
        String result = getJsonResult(bulkByScrollResponse);
        log.info("result:{}", result);
    }

    @Test
    public void deleteByQueryRecentOrderStatus() throws JsonProcessingException {
        RecentOrderScriptCondition sroc = new RecentOrderScriptCondition();
        sroc.setCargoId(10124512292666L);
        BulkByScrollTask.Status status = recentOrderDeleteByQueryMapper.deleteByQueryRecentOrderStatus(sroc);
        String result = getJsonResult(status);
        log.info("result:{}", result);
    }

    @Test
    public void deleteByQueryRecentOrderFuture() {
        RecentOrderScriptCondition sroc = new RecentOrderScriptCondition();
        sroc.setCargoId(10124512292666L);
        recentOrderDeleteByQueryMapper.deleteByQueryRecentOrderFuture(sroc).whenComplete((response, ex) -> {
            try {
                String result = getJsonResult(response);
                log.info("result:{}", result);
            } catch (JsonProcessingException e) {
                log.error("delete by query exception", e);
            }
        }).join();

    }
}
