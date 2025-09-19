package io.manbang.ebatis.core.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.manbang.ebatis.core.annotation.Ignore;

/**
 * Jackson对象序列化容器，此举是为了避免死锁问题，每个线程绑定一个 {@link ObjectMapper}
 *
 * @author 章多亮
 * @since 2020/6/1 15:34
 */
public enum ObjectMapperHolder {
    INSTANCE;
    private static final ObjectMapper mapper;

    static {
        mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .annotationIntrospector(new JacksonAnnotationIntrospector() {
                    private static final long serialVersionUID = -274762453278264130L;

                    @Override
                    public boolean hasIgnoreMarker(AnnotatedMember m) {
                        return super.hasIgnoreMarker(m) || m.hasAnnotation(Ignore.class);
                    }
                })
                .build();
    }

    public static ObjectMapper objectMapper() {
        return mapper;
    }
}
