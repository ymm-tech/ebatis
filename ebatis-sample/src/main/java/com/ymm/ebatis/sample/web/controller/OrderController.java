package com.ymm.ebatis.sample.web.controller;

import com.ymm.ebatis.core.domain.Page;
import com.ymm.ebatis.core.domain.Pageable;
import com.ymm.ebatis.sample.mapper.OrderMapper;
import com.ymm.ebatis.sample.model.Order;
import com.ymm.ebatis.sample.model.OrderCondition;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.TimeUnit;

/**
 * @author 章多亮
 * @since 2020/6/1 18:18
 */
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderMapper orderMapper;

    public OrderController(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @PostMapping
    public Page<Order> search(@RequestBody OrderCondition condition, Pageable pageable) {
        return orderMapper.search(condition, pageable);
    }

    @GetMapping("/{id}")
    public DeferredResult<Order> findById(@PathVariable String id) {
        DeferredResult<Order> deferredResult = new DeferredResult<>(TimeUnit.SECONDS.toMillis(30));

        orderMapper.findById(id)
                .whenComplete((order, throwable) -> {
                    if (throwable == null) {
                        deferredResult.setResult(order);
                    } else {
                        deferredResult.setErrorResult(throwable);
                    }
                });

        return deferredResult;
    }
}
