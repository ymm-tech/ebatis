package io.manbang.ebatis.sample.mapper;

import io.manbang.ebatis.core.mapper.MappingRouter;
import io.manbang.ebatis.core.meta.MethodMeta;
import lombok.extern.slf4j.Slf4j;

/**
 * @author weilong.hu
 * @since 2021/10/20 10:03
 */
@Slf4j
public class SampleMappingRouter implements MappingRouter {
    @Override
    public String[] indices(MethodMeta meta, Object[] args) {
        log.info("meta:{},args:{}", meta, args);
        return new String[]{"recent_order_index_mappingRouter"};
    }
}
