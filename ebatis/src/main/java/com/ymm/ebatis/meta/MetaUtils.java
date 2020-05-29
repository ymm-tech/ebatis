package com.ymm.ebatis.meta;

import com.ymm.ebatis.annotation.Filter;
import com.ymm.ebatis.annotation.Must;
import com.ymm.ebatis.annotation.MustNot;
import com.ymm.ebatis.annotation.Should;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author 章多亮
 * @since 2020/5/27 19:21
 */
public class MetaUtils {
    private static final Set<Class<?>> BASIC_CLASSES = new HashSet<>();
    private static final Class<?>[] QUERY_CLAUSE_ANNOTATION_CLASSES = {Must.class, MustNot.class, Should.class, Filter.class};

    static {
        BASIC_CLASSES.add(Boolean.TYPE);
        BASIC_CLASSES.add(Boolean.class);
        BASIC_CLASSES.add(Byte.TYPE);
        BASIC_CLASSES.add(Byte.class);
        BASIC_CLASSES.add(Short.TYPE);
        BASIC_CLASSES.add(Short.class);
        BASIC_CLASSES.add(Integer.TYPE);
        BASIC_CLASSES.add(Integer.class);
        BASIC_CLASSES.add(Long.TYPE);
        BASIC_CLASSES.add(Long.class);
        BASIC_CLASSES.add(Float.TYPE);
        BASIC_CLASSES.add(Float.class);
        BASIC_CLASSES.add(Double.TYPE);
        BASIC_CLASSES.add(Double.class);
        BASIC_CLASSES.add(String.class);

        BASIC_CLASSES.add(Date.class);
        BASIC_CLASSES.add(BigDecimal.class);
        BASIC_CLASSES.add(Calendar.class);
    }

    private MetaUtils() {
        throw new UnsupportedOperationException();
    }

    public static boolean isBasic(Class<?> type) {
        return BASIC_CLASSES.contains(type);
    }

    public static void registerBasicClass(Class<?> clazz) {
        BASIC_CLASSES.add(clazz);
    }

}
