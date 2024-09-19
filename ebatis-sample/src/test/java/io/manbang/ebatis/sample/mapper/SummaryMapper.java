package io.manbang.ebatis.sample.mapper;

import io.manbang.ebatis.core.annotation.Index;
import io.manbang.ebatis.core.annotation.Search;
import io.manbang.ebatis.core.annotation.Update;
import io.manbang.ebatis.core.domain.Page;
import io.manbang.ebatis.core.domain.Pageable;
import io.manbang.ebatis.sample.condition.SummaryCondition;
import io.manbang.ebatis.sample.entity.Summary;
import io.manbang.ebatis.spring.annotation.EasyMapper;
import org.elasticsearch.index.VersionType;

@EasyMapper(indices = "chip_summary")
public interface SummaryMapper {
    @Index(versionType = VersionType.EXTERNAL)
    String index(Summary summary);

    @Update(versionType = VersionType.EXTERNAL, docAsUpsert = true)
    void update(Summary summary);

    @Search
    Page<Summary> search(SummaryCondition condition, Pageable pageable);
}
