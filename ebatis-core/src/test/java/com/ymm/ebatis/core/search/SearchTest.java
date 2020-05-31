package com.ymm.ebatis.core.search;


import com.ymm.ebatis.core.cluster.ClusterRouter;
import com.ymm.ebatis.core.proxy.MapperProxyFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

public class SearchTest {
    private OrderMapper orderMapper;

    @Before
    public void setup() {
        orderMapper = MapperProxyFactory.getMapperProxy(OrderMapper.class, ClusterRouter.localhost());
    }


    @Test
    public void get() {
        Optional<Order> order = orderMapper.findById(584677L);

        Assert.assertTrue(order.isPresent());
    }
}
