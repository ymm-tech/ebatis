package io.manbang.ebatis.sample.mapper;

import io.manbang.ebatis.core.annotation.Search;
import io.manbang.ebatis.core.domain.Page;
import io.manbang.ebatis.core.domain.Pageable;
import io.manbang.ebatis.sample.model.ProductCondition;
import io.manbang.ebatis.sample.model.ProductDoc;
import io.manbang.ebatis.spring.annotation.EasyMapper;

@EasyMapper(indices = "chip_product")
public interface ProductMapper {
    @Search
    Page<ProductDoc> searchProducts(ProductCondition condition, Pageable pageable);
}
