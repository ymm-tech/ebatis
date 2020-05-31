package com.ymm.ebatis.core.meta;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ymm.ebatis.core.annotation.Field;
import com.ymm.ebatis.core.domain.Range;
import com.ymm.ebatis.core.domain.Script;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.AnnotatedElement;
import java.util.Collection;

/**
 * @author 章多亮
 * @since 2020/5/28 16:49
 */
abstract class AbstractConditionMeta<E extends AnnotatedElement> implements ConditionMeta {
    private final Class<?> type;
    private final boolean range;
    private final boolean script;
    private final boolean array;
    private final boolean collection;
    private final boolean arrayOrCollection;
    private final String name;

    protected AbstractConditionMeta(E element, Class<?> type) {
        this.type = type;
        this.array = type.isArray();
        this.collection = Collection.class.isAssignableFrom(type);
        this.range = Range.class.isAssignableFrom(type);
        this.script = Script.class.isAssignableFrom(type);

        this.arrayOrCollection = array || collection;

        this.name = getName(element);
    }

    protected String getName(E element) {
        String n;

        Field fieldAnnotation = element.getAnnotation(Field.class);
        if (fieldAnnotation != null) {
            n = fieldAnnotation.name();
            if (StringUtils.isNotBlank(n)) {
                return n;
            }

            n = fieldAnnotation.value();
            if (StringUtils.isNotBlank(n)) {
                return n;
            }
        }

        JsonProperty jsonProperty = element.getAnnotation(JsonProperty.class);
        if (jsonProperty != null) {
            n = jsonProperty.value();
            if (StringUtils.isNotBlank(n)) {
                return n;
            }
        }

        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class<?> getType() {
        return type;
    }


    @Override
    public boolean isScript() {
        return script;
    }

    @Override
    public boolean isRange() {
        return range;
    }

    @Override
    public boolean isArray() {
        return array;
    }

    @Override
    public boolean isCollection() {
        return collection;
    }

    @Override
    public boolean isArrayOrCollection() {
        return arrayOrCollection;
    }
}
