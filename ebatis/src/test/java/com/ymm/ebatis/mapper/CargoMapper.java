package com.ymm.ebatis.mapper;

import com.ymm.ebatis.annotation.Mapper;
import com.ymm.ebatis.annotation.QueryType;
import com.ymm.ebatis.annotation.Search;
import com.ymm.ebatis.domain.Page;
import com.ymm.ebatis.domain.Pageable;
import com.ymm.ebatis.response.ResponseExtractor;

import java.util.concurrent.CompletableFuture;

/**
 * @author 章多亮
 * @since 2020/5/25 17:16
 */
@Mapper(indices = "cargo")
public interface CargoMapper {

    @Search(queryType = QueryType.BOOL)
    CompletableFuture<Page<Cargo>> searchAsync(CargoCondition condition, Pageable pageable);

    @Search
    Page<Cargo> search(CargoCondition condition, Pageable pageable);

    @Search
    Page<Cargo> search(CargoCondition condition, Pageable pageable, ResponseExtractor<Cargo> responseExtractor);
}
