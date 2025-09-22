package io.manbang.ebatis.sample.mapper;

import io.manbang.ebatis.core.annotation.Agg;
import io.manbang.ebatis.core.annotation.Get;
import io.manbang.ebatis.core.annotation.Search;
import io.manbang.ebatis.core.domain.MetaSource;
import io.manbang.ebatis.core.domain.Page;
import io.manbang.ebatis.core.domain.Pageable;
import io.manbang.ebatis.sample.model.AggGroupByStatusCondition;
import io.manbang.ebatis.sample.model.RequirementCondition;
import io.manbang.ebatis.sample.model.RequirementDoc;
import io.manbang.ebatis.spring.annotation.EasyMapper;
import org.elasticsearch.search.aggregations.Aggregations;

@EasyMapper(indices = "chip_requirement", clusterRouter = "chip")
public interface RequirementMapper {

    @Get
    MetaSource findById(Long id);

    @Search
    Page<RequirementDoc> search(RequirementCondition condition, Pageable pageable);

    @Agg(aggOnly = true)
    Aggregations groupByStatus(AggGroupByStatusCondition condition);
}
