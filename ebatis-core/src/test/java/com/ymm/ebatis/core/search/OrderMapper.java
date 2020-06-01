package com.ymm.ebatis.core.search;

import com.ymm.ebatis.core.annotation.Get;
import com.ymm.ebatis.core.annotation.Http;
import com.ymm.ebatis.core.annotation.Mapper;
import com.ymm.ebatis.core.annotation.QueryType;
import com.ymm.ebatis.core.annotation.Search;
import com.ymm.ebatis.core.domain.Page;
import com.ymm.ebatis.core.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Http(socketTimeout = 5000, connectionRequestTimeout = 10000, connectTimeout = 3000)
@Mapper(indices = "kibana_sample_data_ecommerce")
public interface OrderMapper {
    @Get
    Optional<Order> findById(String id);

    @Search(queryType = QueryType.WILDCARD)
    List<Order> findByCategory(String category);

    @Search(queryType = QueryType.EXISTS)
    CompletableFuture<Page<Order>> existsBy(String manufacturer, Pageable pageable);

    @Search
    Page<Order> search(OrderCondition condition, Pageable pageable);
}
