package io.manbang.ebatis.sample.mapper;

import io.manbang.ebatis.core.domain.Pageable;
import io.manbang.ebatis.core.domain.Range;
import io.manbang.ebatis.core.proxy.MapperProxyFactory;
import io.manbang.ebatis.sample.model.AggGroupByStatusCondition;
import io.manbang.ebatis.sample.model.ManufacturerCondition;
import io.manbang.ebatis.sample.model.ModelCondition;
import io.manbang.ebatis.sample.model.RequirementCondition;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
public class RequirementMapperTest {
    private static RequirementMapper requirementMapper;

    @BeforeClass
    public static void setup() {
        requirementMapper = MapperProxyFactory.getMapperProxy(RequirementMapper.class);
    }

    @Test
    public void timeRangeSearch() {
        val condition = RequirementCondition.builder()
                .batchNo("x")
                .createTime(Range.lt(LocalDateTime.now()))
                .deadline(Range.le(LocalDate.now()))
                .build();
        val pageable = Pageable.first(100);
        val docs = requirementMapper.search(condition, pageable);
        docs.forEach(doc -> log.info("{}", doc));

    }

    @Test
    public void search() {
        val condition = RequirementCondition.builder()
                .id(8L)
                .batchNo("chip-001")
                .model(ModelCondition.builder().termName("abc#001").build())
                .manufacturer(ManufacturerCondition.builder().id(1L).name("德州仪器").build())
                .build();
        val pageable = Pageable.first(100);
        val docs = requirementMapper.search(condition, pageable);
        docs.forEach(doc -> log.info("{}", doc));
    }

    @Test
    public void groupByStatus() {
        val condition = new AggGroupByStatusCondition();
        condition.setManufacturerId(100051L);

        val aggregations = requirementMapper.groupByStatus(condition);

        ParsedStringTerms groupByStatus = aggregations.get(AggGroupByStatusCondition.GROUP_BY_STATUS);
        val statusBuckets = groupByStatus.getBuckets();
        for (Terms.Bucket bucket : statusBuckets) {
            val key = bucket.getKeyAsString();
            val count = bucket.getDocCount();
            log.info("StatusBucket: {} = {}", key, count);
        }

        ParsedStringTerms groupByCreateTime = aggregations.get(AggGroupByStatusCondition.GROUP_BY_CREATE_TIME);
        val timeBuckets = groupByCreateTime.getBuckets();
        for (Terms.Bucket bucket : timeBuckets) {
            val key = bucket.getKeyAsString();
            val count = bucket.getDocCount();
            log.info("TimeBucket: {} = {}", key, count);
        }
        Assert.assertNotNull(aggregations);
    }

}