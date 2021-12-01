package io.manbang.ebatis.core.domain;

import io.manbang.ebatis.core.exception.EbatisException;
import io.manbang.ebatis.core.meta.ClassMeta;
import io.manbang.ebatis.core.meta.ConditionMeta;
import lombok.Data;
import org.elasticsearch.common.ParseField;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
abstract class AbstractScript implements Script {
    private static final String DEFAULT_LANG = "painless";

    static {
        try {
            final Field sourceParseField = org.elasticsearch.script.Script.class.getDeclaredField("SOURCE_PARSE_FIELD");
            sourceParseField.setAccessible(true);
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(sourceParseField, sourceParseField.getModifiers() & ~Modifier.FINAL);
            sourceParseField.set(null, new ParseField("inline"));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new EbatisException(e);
        }
    }

    private final String idOrCode;
    private Map<String, Object> params;
    private String lang;
    private Map<String, String> options;

    protected AbstractScript(String idOrCode, Object params) {
        this.idOrCode = idOrCode;
        this.params = toMap(params);
        this.lang = DEFAULT_LANG;
        this.options = Collections.emptyMap();
    }

    @Override
    public String getLang() {
        return lang;
    }

    @Override
    public Map<String, String> getOptions() {
        return options;
    }

    @Override
    public void setParams(Object params) {
        this.params = toMap(params);
    }

    @Override
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    private Map<String, Object> toMap(Object obj) {
        if (Objects.isNull(obj)) {
            return Collections.emptyMap();
        }

        return beanToMap(obj);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> beanToMap(Object obj) {
        if (obj instanceof Map) {
            return (Map<String, Object>) obj;
        }

        ClassMeta meta = ClassMeta.of(obj.getClass());

        return meta.getFieldMetas()
                .stream()
                .collect(Collectors.toMap(ConditionMeta::getName, e -> e.getValue(obj)));
    }
}
