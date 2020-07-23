package com.ymm.ebatis.sample;

import com.ymm.ebatis.sample.condition.RecentOrderScriptCondition;
import com.ymm.ebatis.sample.mapper.RecentOrderDeleteByQueryMapper;
import lombok.SneakyThrows;
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
    @SneakyThrows
    public void getRecentOrderLong() {
        RecentOrderScriptCondition sroc = new RecentOrderScriptCondition();
        sroc.setCargoId(10124512292666L);
        BulkByScrollResponse bulkByScrollResponse = recentOrderDeleteByQueryMapper.deleteByQueryRecentOrder(sroc);
        String result = getJsonResult(bulkByScrollResponse);
        log.info("result:{}", result);
    }

    @Test
    @SneakyThrows
    public void DeleteByQueryRecentOrderStatus() {
        RecentOrderScriptCondition sroc = new RecentOrderScriptCondition();
        sroc.setCargoId(10124512292666L);
        BulkByScrollTask.Status status = recentOrderDeleteByQueryMapper.deleteByQueryRecentOrderStatus(sroc);
        String result = getJsonResult(status);
        log.info("result:{}", result);
    }

    @Test
    @SneakyThrows
    public void DeleteByQueryRecentOrderFuture() {
        RecentOrderScriptCondition sroc = new RecentOrderScriptCondition();
        sroc.setCargoId(10124512292666L);
        recentOrderDeleteByQueryMapper.deleteByQueryRecentOrderFuture(sroc).whenComplete((response, ex) -> {
            String result = getJsonResult(response);
            log.info("result:{}", result);
        }).join();

    }
}
