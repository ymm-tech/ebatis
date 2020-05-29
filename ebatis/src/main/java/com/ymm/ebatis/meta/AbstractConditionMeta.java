package com.ymm.ebatis.meta;

import com.ymm.ebatis.domain.Range;
import com.ymm.ebatis.domain.Script;

import java.util.Collection;

/**
 * @author 章多亮
 * @since 2020/5/28 16:49
 */
public abstract class AbstractConditionMeta implements ConditionMeta {
    private final Class<?> type;
    private final boolean range;
    private final boolean script;
    private final boolean array;
    private final boolean collection;
    private final boolean arrayOrCollection;


    protected AbstractConditionMeta(Class<?> type) {
        this.type = type;
        this.array = type.isArray();
        this.collection = Collection.class.isAssignableFrom(type);
        this.range = Range.class.isAssignableFrom(type);
        this.script = Script.class.isAssignableFrom(type);

        this.arrayOrCollection = array || collection;

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
