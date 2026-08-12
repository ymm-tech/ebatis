package io.manbang.ebatis.sample.mapper;

import io.manbang.ebatis.core.annotation.Search;
import io.manbang.ebatis.core.domain.Page;
import io.manbang.ebatis.sample.model.DictionaryCondition;
import io.manbang.ebatis.sample.model.DictionaryDoc;
import io.manbang.ebatis.spring.annotation.EasyMapper;

@EasyMapper(indices = "chip_dictionary")
public interface DictionaryMapper {
    @Search
    Page<DictionaryDoc> search(DictionaryCondition condition);
}
