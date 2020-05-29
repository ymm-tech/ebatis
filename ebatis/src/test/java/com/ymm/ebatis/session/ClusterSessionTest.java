package com.ymm.ebatis.session;

import com.ymm.ebatis.cluster.Cluster;
import com.ymm.ebatis.cluster.ClusterRouter;
import com.ymm.ebatis.domain.Pageable;
import com.ymm.ebatis.mapper.CargoCondition;
import com.ymm.ebatis.mapper.CargoMapper;
import com.ymm.ebatis.proxy.MapperProxyFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author 章多亮
 * @since 2020/5/25 17:15
 */
@Slf4j
public class ClusterSessionTest {
    @Test
    public void search() {
        ClusterRouter router = ClusterRouter.single(Cluster.simple("127.0.0.1", 9200));

        CargoMapper cargoMapper = MapperProxyFactory.getMapperProxy(CargoMapper.class, router);

        CargoCondition condition = new CargoCondition();
        condition.setId(1L);
        condition.setLabels(new String[]{"满运宝"});
        condition.setChannels(new Integer[]{1, 2, 3});

        Pageable pageable = Pageable.of(0, 20);

        cargoMapper.search(condition, pageable)
                .stream()
                .map(Object::toString)
                .forEach(log::info);
    }
}
