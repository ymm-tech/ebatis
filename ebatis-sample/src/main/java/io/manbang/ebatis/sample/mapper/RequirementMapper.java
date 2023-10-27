package io.manbang.ebatis.sample.mapper;

import io.manbang.ebatis.core.annotation.Search;
import io.manbang.ebatis.core.domain.Page;
import io.manbang.ebatis.core.domain.Pageable;
import io.manbang.ebatis.sample.model.RequirementCondition;
import io.manbang.ebatis.sample.model.RequirementDoc;
import io.manbang.ebatis.spring.annotation.EasyMapper;

@EasyMapper(indices = "chip_requirement")
public interface RequirementMapper {

    @Search
    Page<RequirementDoc> search(RequirementCondition condition, Pageable pageable);
}
