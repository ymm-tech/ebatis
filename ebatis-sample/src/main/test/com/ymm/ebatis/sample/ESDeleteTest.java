package com.ymm.ebatis.sample;

import com.ymm.ebatis.core.common.ObjectMapperHolder;
import com.ymm.ebatis.sample.mapper.RecentOrderDeleteMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.rest.RestStatus;
import org.junit.Before;
import org.junit.Test;

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
    @SneakyThrows
    public void deleteRecentOrder() {
        RestStatus restStatus = recentOrderDeleteMapper.deleteRecentOrder(10124512292666L);
        log.info("recentOrder:{}", restStatus);
    }

    @Test
    @SneakyThrows
    public void deleteRecentOrderDeleteResponse() {
        DeleteResponse deleteResponse = recentOrderDeleteMapper.deleteRecentOrderDeleteResponse(10124512292666L);
        String result = ObjectMapperHolder.objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(deleteResponse);
        log.info("delete response:{}", result);
    }

    @Test
    @SneakyThrows
    public void deleteRecentOrderBoolean() {
        Boolean bool = recentOrderDeleteMapper.deleteRecentOrderBoolean(10124512292666L);
        log.info("delete result:{}", bool);
    }

}
