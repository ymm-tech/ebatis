package io.manbang.ebatis.sample;

import io.manbang.ebatis.core.domain.Pageable;
import io.manbang.ebatis.core.proxy.MapperProxyFactory;
import io.manbang.ebatis.sample.condition.SummaryCondition;
import io.manbang.ebatis.sample.entity.Scene;
import io.manbang.ebatis.sample.entity.SummaryDoc;
import io.manbang.ebatis.sample.entity.Type;
import io.manbang.ebatis.sample.mapper.SummaryMapper;
import lombok.val;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDateTime;

public class SummaryMapperTest {
    private static SummaryMapper summaryMapper;

    @BeforeClass
    public static void setUp() {
        summaryMapper = MapperProxyFactory.getMapperProxy(SummaryMapper.class);
    }

    @Test
    public void testIndex() {
        val summary = new SummaryDoc();
        summary.setId(1L);
        summary.setName("test");
        summary.setScene(Scene.QUOTATION);
        summary.setType(Type.SUM);
        summary.setValue(100L);
        summary.setVersion(4);
        val now = LocalDateTime.now();
        summary.setCreateTime(now);
        summary.setUpdateTime(now);

        String id = summaryMapper.index(summary);
        System.out.println(id);
    }

    @Test
    public void testUpdate() {
        val summary = new SummaryDoc();
        summary.setId(1L);
        summary.setName("START");
        summary.setScene(Scene.QUOTATION);
        summary.setType(Type.SUM);
        summary.setValue(100L);
        summary.setVersion(3);
        val model = new SummaryDoc.Model();
        model.setId(1L);
        model.setName("STM518,EOL-317");
        summary.setModel(model);
        val now = LocalDateTime.now();
        summary.setCreateTime(now);
        summary.setUpdateTime(now);

        summaryMapper.update(summary);
    }

    @Test
    public void testSearch() {
        val condition = new SummaryCondition();
        condition.setName("te");

        summaryMapper.search(condition, Pageable.first(10)).forEach(System.out::println);

        condition.setName("te gd");
        condition.setScene(Scene.QUOTATION);
        summaryMapper.search(condition, Pageable.first(10)).forEach(System.out::println);

        condition.setName(null);
        condition.setScene(Scene.ORDER);
        summaryMapper.search(condition, Pageable.first(10)).forEach(System.out::println);
    }

    @Test
    public void testNestedSearch() {
        val condition = new SummaryCondition();
        val model = new SummaryCondition.ModelCondition();
        model.setName("eol");
        condition.setModel(model);

        summaryMapper.search(condition, Pageable.first(10)).forEach(System.out::println);
    }
}