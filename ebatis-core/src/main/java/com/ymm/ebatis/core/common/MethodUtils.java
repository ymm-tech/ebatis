package com.ymm.ebatis.core.common;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * 方法的实用类
 *
 * @author 章多亮
 * @since 2020/01/11 19:04:18
 */
public class MethodUtils {
    private MethodUtils() {
        throw new UnsupportedOperationException();
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

    /**
     * 判断两个方法的签名是否相同
     *
     * @param m1 方法1
     * @param m2 方法2
     * @return 如果两个方法的签名一致，返回<code>true</code>
     */
    public static boolean isSameSignature(Method m1, Method m2) {
        return m2.getReturnType().isAssignableFrom(m1.getReturnType()) // 返回值，可以兼容子类
                && m1.getName().equals(m2.getName()) // 方法名称是否相同
                && Arrays.equals(m1.getParameterTypes(), m2.getParameterTypes()); // 方法签名是否相同
    }
}
