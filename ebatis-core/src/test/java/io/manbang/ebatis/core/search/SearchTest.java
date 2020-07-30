package io.manbang.ebatis.core.search;


import io.manbang.ebatis.core.domain.Page;
import io.manbang.ebatis.core.domain.Pageable;
import io.manbang.ebatis.core.domain.Range;
import io.manbang.ebatis.core.domain.ScrollResponse;
import io.manbang.ebatis.core.proxy.MapperProxyFactory;
import io.manbang.ebatis.core.response.ResponseExtractor;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.search.ClearScrollResponse;
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

    @Test
    public void searchScroll() {
        OrderCondition condition = new OrderCondition();
        condition.setCategory("Clothing");

        Pageable first = Pageable.first(20);
        ScrollResponse<Order> response;
        do {
            response = orderMapper.searchScroll(condition, first);
            condition.setScrollId(response.getScrollId());
            response.forEach(order -> log.info("{}", order));
        } while (!response.isEmpty());

        Assert.assertTrue(orderMapper.clearSearchScroll(response.getScrollId(), new ResponseExtractor<Boolean>() {
            @Override
            public Boolean extractData(ActionResponse response) {
                ClearScrollResponse scrollResponse = narrow(response, ClearScrollResponse.class);
                return scrollResponse.isSucceeded();
            }
        }));

        Assert.assertTrue(response.isEmpty());
    }
}
