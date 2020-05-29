package com.ymm.ebatis.executor;

import com.ymm.ebatis.meta.RequestType;
import org.junit.Test;

/**
 * @author 章多亮
 * @since 2020/5/22 16:23
 */
public class SearchTest {
    @Test
    public void search() {
        RequestExecutor executor = RequestType.SEARCH.getRequestExecutor();
    }
}
