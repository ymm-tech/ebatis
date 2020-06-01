package com.ymm.ebatis.core.response;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Jackson对象序列化容器，此举是为了避免思索问题，每个现成绑定一个 {@link ObjectMapper}
 *
 * @author 章多亮
 * @since 2020/6/1 15:34
 */
public class ObjectMapperHolder {
    private static final ThreadLocal<ObjectMapper> OBJECT_MAPPERS;

    static {
        OBJECT_MAPPERS = ThreadLocal.withInitial(() -> {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            return mapper;
        });
    }

    private ObjectMapperHolder() {
        throw new UnsupportedOperationException();
    }

    public static ObjectMapper objectMapper() {
        return OBJECT_MAPPERS.get();
    }

    public static void remove() {
        OBJECT_MAPPERS.remove();
    }
}
