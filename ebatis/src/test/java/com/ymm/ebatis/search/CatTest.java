package com.ymm.ebatis.search;

import com.ymm.ebatis.annotation.Cat;
import com.ymm.ebatis.annotation.CatType;
import com.ymm.ebatis.annotation.Mapper;
import com.ymm.ebatis.cluster.ClusterRouter;
import com.ymm.ebatis.proxy.MapperProxyFactory;
import org.awaitility.Awaitility;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

@Mapper(indices = "")
interface CargoClusterMapper {
    @Cat(catType = CatType.COUNT)
    void catCount();
}

public class CatTest {
    private CargoClusterMapper mapper = MapperProxyFactory.getMapperProxy(CargoClusterMapper.class, ClusterRouter.localhost());

    @Test
    public void catCount() {
        mapper.catCount();
        Awaitility.await().forever().untilTrue(new AtomicBoolean(false));
    }
}
