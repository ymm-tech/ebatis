package io.manbang.ebatis.sample.mapper;

import io.manbang.ebatis.core.domain.Pageable;
import io.manbang.ebatis.core.proxy.MapperProxyFactory;
import io.manbang.ebatis.sample.model.ModelCondition;
import io.manbang.ebatis.sample.model.RequirementCondition;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.BeforeClass;
import org.junit.Test;

@Slf4j
public class RequirementMapperTest {
    private static RequirementMapper requirementMapper;

    @BeforeClass
    public static void setup() {
        requirementMapper = MapperProxyFactory.getMapperProxy(RequirementMapper.class);
    }

    @Test
    public void search() {
        val condition = RequirementCondition.builder()
                .id(8L)
                .model(ModelCondition.builder().name("abc#001").build())
                .build();
        val pageable = Pageable.first(100);
        val docs = requirementMapper.search(condition, pageable);
        docs.forEach(doc -> log.info("{}", doc));
    }

}