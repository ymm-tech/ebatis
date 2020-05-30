package com.ymm.ebatis.search;

import com.ymm.ebatis.annotation.Mapper;
import com.ymm.ebatis.annotation.QueryType;
import com.ymm.ebatis.annotation.Search;
import com.ymm.ebatis.cluster.ClusterRouter;
import com.ymm.ebatis.domain.Pageable;
import com.ymm.ebatis.proxy.MapperProxyFactory;
import org.junit.Test;

@Mapper(indices = "cargo")
interface CargoMapper {
    @Search(queryType = QueryType.EXISTS)
    void exists(Boolean searchable);

    @Search(queryType = QueryType.MATCH_ALL)
    void matchAll(Pageable pageable);
}

public class SearchTest {
    private CargoMapper mapper = MapperProxyFactory.getMapperProxy(CargoMapper.class, ClusterRouter.localhost());

    @Test
    public void exists() {
        mapper.exists(null);
    }

    @Test
    public void matchAll() {
        mapper.matchAll(Pageable.of(2, 20));
    }
}