package io.manbang.ebatis.core.proxy;

import io.manbang.ebatis.core.cluster.ClusterRouter;
import io.manbang.ebatis.core.exception.EbatisException;
import io.manbang.ebatis.core.mapper.IndexApi;
import org.apache.commons.lang3.concurrent.LazyInitializer;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author weilong.hu
 * @since 2021/07/05 15:26
 */
enum MapperType {
    /**
     * index相关操作
     */
    INDEX(IndexApi.class) {
        @Override
        public <T> T instance(LazyInitializer<ClusterRouter> clusterRouter) {
            return (T) IndexApiImpl.index(clusterRouter);
        }
    };

    MapperType(Class<?> clazz) {
        this.clazz = clazz;
    }

    private final Class<?> clazz;

    public abstract <T> T instance(LazyInitializer<ClusterRouter> clusterRouter);

    public Class<?> getClazz() {
        return clazz;
    }

    private static final Map<Class<?>, MapperType> OPERATE_TYPES = Arrays.stream(values()).collect(Collectors.toMap(MapperType::getClazz, o -> o));

    static MapperType type(Class<?> clazz) {
        return Optional.ofNullable(OPERATE_TYPES.get(clazz)).orElseThrow(() -> new EbatisException(String.format("Unknown interface,name:%s", clazz.getName())));
    }
}
