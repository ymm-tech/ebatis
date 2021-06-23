package io.manbang.ebatis.core.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import io.manbang.ebatis.core.annotation.Ignore;

import java.util.Objects;

/**
 * Jackson对象序列化容器，此举是为了避免死锁问题，每个线程绑定一个 {@link ObjectMapper}
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
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {
                @Override
                public boolean hasIgnoreMarker(AnnotatedMember m) {
                    Ignore ann = _findAnnotation(m, Ignore.class);
                    return Objects.nonNull(ann) || super.hasIgnoreMarker(m);
                }
            });
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
