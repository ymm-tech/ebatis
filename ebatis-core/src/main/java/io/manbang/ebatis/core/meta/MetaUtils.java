package io.manbang.ebatis.core.meta;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author 章多亮
 * @since 2020/5/27 19:21
 */
public class MetaUtils {
    private static final Set<Class<?>> BASIC_CLASSES = new HashSet<>();

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


    public static <E> Optional<E> findFirstElement(E[] array) {
        return array == null || array.length == 0 ? Optional.empty() : Optional.of(array[0]);
    }

    public static <E> E getFirstElement(E[] array) {
        if (array == null || array.length == 0) {
            throw new IndexOutOfBoundsException("数组至少需要一个元素");
        } else {
            return array[0];
        }
    }

}
