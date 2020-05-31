package com.ymm.ebatis.core.search;

import com.ymm.ebatis.core.annotation.Get;
import com.ymm.ebatis.core.annotation.Mapper;

import java.util.Optional;

@Mapper(indices = "kibana_sample_data_ecommerce")
public interface OrderMapper {
    @Get
    Optional<Order> findById(String id);
}
