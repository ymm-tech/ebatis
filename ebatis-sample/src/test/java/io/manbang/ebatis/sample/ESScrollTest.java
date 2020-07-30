package io.manbang.ebatis.sample;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.manbang.ebatis.core.domain.Pageable;
import io.manbang.ebatis.core.domain.ScrollResponse;
import io.manbang.ebatis.core.response.ResponseExtractor;
import io.manbang.ebatis.sample.condition.RecentOrderScrollCondition;
import io.manbang.ebatis.sample.entity.RecentOrder;
import io.manbang.ebatis.sample.mapper.RecentOrderScrollMapper;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.search.ClearScrollResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author weilong.hu
 * @since 2020/7/3 10:34
 */
@Slf4j
public class ESScrollTest extends ESAbstractTest {
    private RecentOrderScrollMapper recentOrderScrollMapper;

    @Before
    public void startup() {
        recentOrderScrollMapper = createEsMapper(RecentOrderScrollMapper.class);
    }

    @Test
    public void test() {
        Pageable first = Pageable.first(20);
        RecentOrderScrollCondition condition = new RecentOrderScrollCondition();
        ScrollResponse<RecentOrder> response;
        do {
            response = recentOrderScrollMapper.searchScroll(condition, first);
            condition.setScrollId(response.getScrollId());
            response.forEach(order -> {
                try {
                    log.info("order:{}", getJsonResult(order));
                } catch (JsonProcessingException e) {
                    log.error("order result", e);
                }
            });
        } while (!response.isEmpty());

        Assert.assertTrue(recentOrderScrollMapper.clearSearchScroll(response.getScrollId(), new ResponseExtractor<Boolean>() {
            @Override
            public Boolean extractData(ActionResponse response) {
                ClearScrollResponse scrollResponse = narrow(response, ClearScrollResponse.class);
                return scrollResponse.isSucceeded();
            }
        }));

        Assert.assertTrue(response.isEmpty());
    }
}
