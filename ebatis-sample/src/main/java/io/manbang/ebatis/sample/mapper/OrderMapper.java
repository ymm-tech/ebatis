package io.manbang.ebatis.sample.mapper;

import io.manbang.ebatis.core.annotation.Get;
import io.manbang.ebatis.core.annotation.Search;
import io.manbang.ebatis.core.domain.Page;
import io.manbang.ebatis.core.domain.Pageable;
import io.manbang.ebatis.sample.model.Order;
import io.manbang.ebatis.sample.model.OrderCondition;
import io.manbang.ebatis.spring.annotation.EasyMapper;

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
