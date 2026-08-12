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
import java.util.Collections;

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
        summary.setVersion(1);

        val brand = new SummaryDoc.Brand();
        brand.setId(1L);
        brand.setName("Galaxy of Sang sum");

        val model = new SummaryDoc.Model();
        model.setId(1L);
        model.setName("STM518,EOL-317");
        model.setBrand(brand);
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

    @Test
    public void testEnumTermsSearch() {
        val condition = new SummaryCondition();
        condition.setTypes(Collections.singletonList(Type.SUM));
        summaryMapper.search(condition, Pageable.first(10)).forEach(System.out::println);
    }

    @Test
    public void testSearchByBrand() {
        val condition = new SummaryCondition();

        val branch = new SummaryCondition.Branch();
        branch.setName("Sang sum");
        val brand = new SummaryCondition.Brand();
        brand.setId(1L);
        brand.setName("Galaxy of Sang sum");
        brand.setBranch(branch);

        val model = new SummaryCondition.ModelCondition();
        model.setBrand(brand);
        condition.setModel(model);
        condition.setName("STM518");

        summaryMapper.search(condition, Pageable.first(10)).forEach(System.out::println);
    }
}