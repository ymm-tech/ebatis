package com.ymm.ebatis.sample.mapper;

import com.ymm.ebatis.core.annotation.Get;
import com.ymm.ebatis.core.annotation.Search;
import com.ymm.ebatis.core.domain.Page;
import com.ymm.ebatis.core.domain.Pageable;
import com.ymm.ebatis.sample.model.Order;
import com.ymm.ebatis.sample.model.OrderCondition;
import com.ymm.ebatis.spring.annotation.EasyMapper;

import java.util.concurrent.CompletableFuture;

/**
 * @author 章多亮
 * @since 2020/6/1 18:20
 */
//@Repository
@EasyMapper(indices = "kibana_sample_data_ecommerce")
public interface OrderMapper {
    @Get
    CompletableFuture<Order> findById(String id);

    @Search
    Page<Order> search(OrderCondition condition, Pageable pageable);
}
