package io.manbang.ebatis.sample.mapper;

import io.manbang.ebatis.core.annotation.Index;
import io.manbang.ebatis.core.annotation.Update;
import io.manbang.ebatis.sample.entity.Summary;
import io.manbang.ebatis.spring.annotation.EasyMapper;
import org.elasticsearch.index.VersionType;

@EasyMapper(indices = "chip_summary")
public interface SummaryMapper {
    @Index(versionType = VersionType.EXTERNAL)
    String index(Summary summary);

    @Update(versionType = VersionType.EXTERNAL, docAsUpsert = true)
    void update(Summary summary);
}
