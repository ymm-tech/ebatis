package com.ymm.ebatis.core.common;

import com.ymm.ebatis.core.annotation.AnnotationConstants;
import com.ymm.ebatis.core.annotation.Filter;
import com.ymm.ebatis.core.annotation.Must;
import com.ymm.ebatis.core.annotation.MustNot;
import com.ymm.ebatis.core.annotation.Should;
import com.ymm.ebatis.core.exception.ConditionBeanInfoException;
import com.ymm.ebatis.core.exception.ReadMethodInvokeException;
import com.ymm.ebatis.core.exception.ReadMethodNotFoundException;
import com.ymm.ebatis.core.meta.ConditionMeta;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.support.ActiveShardCount;
import org.elasticsearch.common.unit.TimeValue;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * @author 章多亮
 * @since 2019/12/18 16:33
 */
@Slf4j
public class DslUtils {
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

    private DslUtils() {
        throw new UnsupportedOperationException();
    }

    public static boolean isBasicClass(Class<?> clazz) {
        return BASIC_CLASSES.contains(clazz);
    }

    public static boolean isBasicField(Field field) {
        return isBasicClass(field.getType());
    }

    public static Object dateToMillis(Object value) {
        if (value instanceof Date) {
            return ((Date) value).getTime();
        } else if (value instanceof Calendar) {
            return ((Calendar) value).getTimeInMillis();
        } else {
            return value;
        }
    }

    /**
     * 判定指定对象是否为集合或者数组对象
     *
     * @param value 待判定的对象
     * @return 如果是，数据或者集合类型实例，返回<code>true</code>，否则，返回<code>false</code>
     */
    public static boolean isArrayOrCollection(Object value) {
        Objects.requireNonNull(value, "无法判断空值对象是否为数组或者集合实例");
        Class<?> clazz = value.getClass();
        return Collection.class.isAssignableFrom(clazz) || clazz.isArray();
    }

    public static boolean isArrayOrCollection(Class<?> clazz) {
        Objects.requireNonNull(clazz, "无法判断空值类是否为数组或者集合类型");
        return Collection.class.isAssignableFrom(clazz) || clazz.isArray();
    }

    public static BeanInfo getBeanInfo(Class<?> conditionClass) {
        try {
            return Introspector.getBeanInfo(conditionClass);
        } catch (IntrospectionException e) {
            throw new ConditionBeanInfoException();
        }
    }

    public static Object getFieldValue(Method method, Object condition) {
        try {
            return method.invoke(condition);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("获取条件属性异常", e);
            throw new ReadMethodInvokeException();
        }
    }


    public static Class<? extends Annotation> groupByQueryClauseType(ConditionMeta<? extends AnnotatedElement> conditionMeta) {
        if (conditionMeta.isAnnotationPresent(Must.class)) {
            return Must.class;
        } else if (conditionMeta.isAnnotationPresent(MustNot.class)) {
            return MustNot.class;
        } else if (conditionMeta.isAnnotationPresent(Filter.class)) {
            return Filter.class;
        } else if (conditionMeta.isAnnotationPresent(Should.class)) {
            return Should.class;
        } else {
            return Must.class;
        }
    }


    public static Method getReadMethod(Field field) {
        BeanInfo beanInfo = getBeanInfo(field.getDeclaringClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

        for (PropertyDescriptor descriptor : propertyDescriptors) {
            if (descriptor.getName().equals(field.getName())) {
                return descriptor.getReadMethod();
            }
        }

        throw new ReadMethodNotFoundException(field.toString());
    }


    public static Map<Class<?>, Method> getNestedAnnotationMethods(Class<?> aClass) {
        if (Annotation.class.isAssignableFrom(aClass)) {
            Method[] methods = aClass.getDeclaredMethods();
            Map<Class<?>, Method> nestedAnnotationMethods = new HashMap<>(methods.length);
            for (Method method : methods) {
                if (isAnnotationArray(method)) {
                    nestedAnnotationMethods.put(method.getReturnType().getComponentType(), method);
                }
            }
            return Collections.unmodifiableMap(nestedAnnotationMethods);
        } else {
            return Collections.emptyMap();
        }
    }


    public static <A> A getNestedAnnotation(Object instance, Method method) {
        if (instance == null || method == null) {
            return null;
        }
        try {
            return (A) DslUtils.getFirstElement((Object[]) method.invoke(instance));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException();
        }
    }


    private static boolean isAnnotationArray(Method method) {
        Class<?> returnType = method.getReturnType();
        return returnType.isArray() && returnType.getComponentType().isAnnotation();
    }

    public static boolean isNestedQueryClause(Annotation queryClauseAnnotation) {
        if (queryClauseAnnotation == null) {
            return false;
        }
        return getNestedMethod(queryClauseAnnotation).map(m -> {
            try {
                return (Boolean) m.invoke(queryClauseAnnotation);
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.error("无法访问nested方法: {}", queryClauseAnnotation, e);
                return Boolean.FALSE;
            }
        }).orElse(Boolean.FALSE);
    }


    private static Optional<Method> getNestedMethod(Annotation annotation) {
        try {
            return Optional.of(annotation.annotationType().getDeclaredMethod("nested"));
        } catch (Exception e) {
            // Ignore
            return Optional.empty();
        }
    }

    public static TimeValue getScrollKeepAlive(long keepAlive) {
        return keepAlive <= 0 ? null : TimeValue.timeValueMillis(keepAlive);
    }

    public static ActiveShardCount getActiveShardCount(int waitForActiveShards) {
        return AnnotationConstants.ACTIVE_SHARD_COUNT_DEFAULT == waitForActiveShards ? ActiveShardCount.DEFAULT : ActiveShardCount.from(waitForActiveShards);
    }

    public static <E> Optional<E> getFirstElement(E[] array) {
        return array == null || array.length == 0 ? Optional.empty() : Optional.of(array[0]);
    }

    public static <E> E getFirstElementRequired(E[] array) {
        if (array == null || array.length == 0) {
            throw new IndexOutOfBoundsException("数组至少需要一个元素");
        } else {
            return array[0];
        }
    }

    public static String getRouting(String... routing) {
        if (ArrayUtils.isEmpty(routing)) {
            return null;
        }
        if (routing.length == 1) {
            if (StringUtils.isBlank(routing[0])) {
                return null;
            } else {
                return routing[0];
            }
        } else {
            return StringUtils.join(routing, ",");
        }
    }
}
