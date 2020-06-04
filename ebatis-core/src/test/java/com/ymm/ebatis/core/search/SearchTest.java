package com.ymm.ebatis.core.search;


import com.ymm.ebatis.core.domain.Page;
import com.ymm.ebatis.core.domain.Pageable;
import com.ymm.ebatis.core.domain.Range;
import com.ymm.ebatis.core.proxy.MapperProxyFactory;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class SearchTest {
    private static OrderMapper orderMapper;

    @BeforeClass
    public static void setup() {
        orderMapper = MapperProxyFactory.getMapperProxy(OrderMapper.class);
    }


    @Test
    public void get() {
        Optional<Order> order = orderMapper.findById("FcSRbXIBHucreWHcyrOa");
        Assert.assertTrue(order.isPresent());
    }

    @Test
    public void wildcard() {
        String category = "Men*";
        List<Order> orders = orderMapper.findByCategory(category);
        Assert.assertTrue(orders.isEmpty());
    }

    @Test
    public void parameterExists() {
        AtomicBoolean result = new AtomicBoolean();

        orderMapper.existsBy(null, Pageable.first(10))
                .whenComplete((page, throwable) -> {
                    page.forEach(o -> log.info("{}", o));

                    result.set(true);
                });

        Awaitility.await().forever().untilTrue(result);
        Assert.assertTrue(result.get());
    }


    @Test
    public void search() {
        OrderCondition condition = new OrderCondition();
        condition.setOrderId(584677L);

        CustomerCondition customerCondition = new CustomerCondition();
        customerCondition.setId(38L);
        customerCondition.setFullName("Eddie Mary");
        condition.setCustomerCondition(customerCondition);

        Page<Order> orders = orderMapper.search(condition, Pageable.first(20));
        Assert.assertFalse(orders.isEmpty());

        orders.forEach(o -> log.info("{}", o));
    }

    @Test
    public void complexSearch() {
        OrderCondition condition = new OrderCondition();
        condition.setOrderId(1L);
        condition.setManufacturers(new String[]{"Oceanavigations", "Elitelligence"});

        CustomerCondition customerCondition = new CustomerCondition();
        customerCondition.setFullName("Eddie");
        customerCondition.setFirstName("Eddie");
        customerCondition.setLastName("Underwood");
        customerCondition.setPhone("18951621100");

        condition.setCustomerCondition(customerCondition);

        ProductCondition productCondition = new ProductCondition();
        productCondition.setBasePrice(Range.of(10.0, 100.0));
        productCondition.setName("运满满");

        condition.setProductCondition(productCondition);

        Page<Order> orders = orderMapper.search(condition, Pageable.first(10));
        orders.stream().map(Objects::toString).forEach(log::info);
        Assert.assertTrue(orders.isEmpty());
    }
}
