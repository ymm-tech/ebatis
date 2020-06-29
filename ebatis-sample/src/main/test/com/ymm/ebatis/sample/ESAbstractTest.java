package com.ymm.ebatis.sample;

import com.ymm.ebatis.core.proxy.MapperProxyFactory;
import com.ymm.ebatis.sample.cluster.SampleClusterRouterProvider;

/**
 * @author weilong.hu
 * @since 2020/6/29 16:59
 */
abstract class ESAbstractTest {

    protected <R> R createEsMapper(Class<R> mapperClass) {
        return MapperProxyFactory.getMapperProxy(mapperClass, SampleClusterRouterProvider.SAMPLE_CLUSTER_NAME);
    }
}
