package io.manbang.ebatis.sample.mapper;

import io.manbang.ebatis.core.annotation.Index;
import io.manbang.ebatis.core.annotation.Search;
import io.manbang.ebatis.core.annotation.Update;
import io.manbang.ebatis.core.domain.Page;
import io.manbang.ebatis.core.domain.Pageable;
import io.manbang.ebatis.sample.condition.SummaryCondition;
import io.manbang.ebatis.sample.entity.SummaryDoc;
import io.manbang.ebatis.spring.annotation.EasyMapper;
import org.elasticsearch.index.VersionType;

@EasyMapper(indices = "chip_summary", clusterRouter = "summary")
public interface SummaryMapper {
    @Index(versionType = VersionType.EXTERNAL)
    String index(SummaryDoc summary);

    @Update(versionType = VersionType.EXTERNAL, docAsUpsert = true)
    void update(SummaryDoc summary);

    @Search
    Page<SummaryDoc> search(SummaryCondition condition, Pageable pageable);
}
