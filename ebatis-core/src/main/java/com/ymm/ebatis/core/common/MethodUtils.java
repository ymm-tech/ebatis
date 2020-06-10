package com.ymm.ebatis.core.common;

import com.ymm.ebatis.core.exception.MethodInvokeException;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 方法的实用类
 *
 * @author 章多亮
 * @since 2020/01/11 19:04:18
 */
public class MethodUtils {

    private static final String EQUALS_METHOD_NAME = "equals";
    private static final String HASH_CODE_METHOD_NAME = "hashCode";
    private static final String TO_STRING_METHOD_NAME = "toString";

    private MethodUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * 判断指定方法是否是 {@link Object}的方法, Copy from Spring#ReflectionUtils
     *
     * @param method 方法
     * @return 如果是，返回<code>true</code>
     */
    public static boolean isObjectMethod(Method method) {
        if (method == null) {
            return false;
        }

        try {
            Object.class.getDeclaredMethod(method.getName(), method.getParameterTypes());
            return true;
        } catch (Exception ignore) {
            return false;
        }

    }

    /**
     * 判断指定的方法是否是指定类或者接口的重写方法
     *
     * @param method    方法
     * @param baseClass 基类或者接口
     * @return 如果该方法是重写 baseClass 中声明的方法的话，返回<code>true</code>
     */
    public static boolean isOverriddenMethod(Method method, Class<?> baseClass) {
        Class<?> declaringClass = method.getDeclaringClass();

        // 如果声明方法的类就是baseClass本身，或者声明类没有继承或者实现baseClass，那就不存在重写的可能性
        if (declaringClass == baseClass || !baseClass.isAssignableFrom(declaringClass)) {
            return false;
        }

        return Stream.of(baseClass.getDeclaredMethods()).anyMatch(m -> isSameSignature(method, m));
    }

    @SuppressWarnings("unchecked")
    public static <E> E invoke(Method method, Object instance, Object... args) {
        try {
            return (E) method.invoke(instance, args);
        } catch (Exception e) {
            throw new MethodInvokeException(e);
        }
    }

    /**
     * 判断两个方法的签名是否相同
     *
     * @param m1 方法1
     * @param m2 方法2
     * @return 如果两个方法的签名一致，返回<code>true</code>
     */
    public static boolean isSameSignature(Method m1, Method m2) {
        // 返回值，可以兼容子类
        // 方法名称是否相同
        // 方法签名是否相同
        return m2.getReturnType().isAssignableFrom(m1.getReturnType())
                && m1.getName().equals(m2.getName())
                && Arrays.equals(m1.getParameterTypes(), m2.getParameterTypes());
    }

    /**
     * Determine whether the given method is an "equals" method. Copy from Spring#ReflectionUtils
     *
     * @see java.lang.Object#equals(Object)
     */
    public static boolean isEqualsMethod(Method method) {
        if (method == null || !Objects.equals(method.getName(), EQUALS_METHOD_NAME)) {
            return false;
        }
        Class<?>[] paramTypes = method.getParameterTypes();
        return (paramTypes.length == 1 && paramTypes[0] == Object.class);
    }

    /**
     * Determine whether the given method is a "hashCode" method. Copy from Spring#ReflectionUtils
     *
     * @see java.lang.Object#hashCode()
     */
    public static boolean isHashCodeMethod(Method method) {
        return (method != null && Objects.equals(method.getName(), HASH_CODE_METHOD_NAME) && method.getParameterTypes().length == 0);
    }

    /**
     * Determine whether the given method is a "toString" method. Copy from Spring#ReflectionUtils
     *
     * @see java.lang.Object#toString()
     */
    public static boolean isToStringMethod(Method method) {
        return (method != null && Objects.equals(method.getName(), TO_STRING_METHOD_NAME) && method.getParameterTypes().length == 0);
    }
}
