package io.manbang.ebatis.sample.mapper;

import io.manbang.ebatis.core.proxy.MapperProxyFactory;
import io.manbang.ebatis.sample.model.DictionaryCondition;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

@Slf4j
public class DictionaryMapperTest {

    private static DictionaryMapper dictionaryMapper;

    @BeforeClass
    public static void setUp() throws Exception {
        dictionaryMapper = MapperProxyFactory.getMapperProxy(DictionaryMapper.class);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void search() {
        val condition = DictionaryCondition.builder()
                .id(1L)
                .build();
        val docs = dictionaryMapper.search(condition);
        docs.forEach(doc -> log.info("{}", doc));
    }
}