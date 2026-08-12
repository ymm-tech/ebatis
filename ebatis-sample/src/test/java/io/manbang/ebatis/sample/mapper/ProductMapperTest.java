package io.manbang.ebatis.sample.mapper;

import com.fasterxml.jackson.databind.json.JsonMapper;
import io.manbang.ebatis.core.domain.Pageable;
import io.manbang.ebatis.core.proxy.MapperProxyFactory;
import io.manbang.ebatis.sample.model.ModelCondition;
import io.manbang.ebatis.sample.model.ProductCondition;
import io.manbang.ebatis.sample.model.ProductDoc;
import io.manbang.ebatis.sample.model.Wildcard;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

@Slf4j
public class ProductMapperTest {

    private static ProductMapper mapper;
    private static JsonMapper jsonMapper;

    @BeforeClass
    public static void setUp() {
        mapper = MapperProxyFactory.getMapperProxy(ProductMapper.class);
        jsonMapper = JsonMapper.builder().build();
    }

    @Test
    public void searchProducts() {
        val condition = ProductCondition.builder()
                .model(ModelCondition.builder()
                        .fuzzyName("AD5934YR")
                        .wildcardName(Wildcard.fullLike("yr"))
                        .termName("STBC08PMR")
                        .build())
                .build();
        val pageable = Pageable.first(10);
        val products = mapper.searchProducts(condition, pageable);

        Assert.assertFalse(products.isEmpty());

        products.forEach(this::printProduct);
    }

    @SneakyThrows
    private void printProduct(ProductDoc product) {
        log.info(jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(product));
    }
}