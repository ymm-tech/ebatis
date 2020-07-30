package io.manbang.ebatis.core.generic;

import io.manbang.ebatis.core.domain.Page;
import io.manbang.ebatis.core.exception.GenericTypeException;
import io.manbang.ebatis.core.meta.MetaUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * 泛型解析器，可以对类、方法返回值、方法入参进行泛型解析
 *
 * @author 章多亮
 * @since 2020/6/9 11:03
 */
public interface GenericType {

    /**
     * 创建泛型类型解析器
     *
     * @param type 类型
     * @return 泛型解析器
     */
    static GenericType forType(Type type) {
        return new DefaultGenericType(type);
    }

    /**
     * 创建字段泛型解析器
     *
     * @param field 字段
     * @return 泛型解析器
     */
    static GenericType forField(Field field) {
        return new DefaultGenericType(field.getGenericType());
    }

    /**
     * 创建参数泛型解析器
     *
     * @param parameter 字段
     * @return 泛型解析器
     */
    static GenericType forParameter(Parameter parameter) {
        return new DefaultGenericType(parameter.getParameterizedType());
    }

    /**
     * 创建方法泛型解析器
     *
     * @param method 方法
     * @return 泛型解析器
     */
    static MethodGenericType forMethod(Method method) {
        return new DefaultMethodGenericType(method);
    }

    /**
     * 判断当前类型是否是 {@link Page 分页}类型
     *
     * @return 如果是 {@link Page 分页} 类型，返回<code>true</code>
     * @see Page
     */
    default boolean isPage() {
        return resolveOptional().map(c -> c == Page.class).orElse(false);
    }

    /**
     * 判断当前类型是否是 {@link CompletableFuture}类型
     *
     * @return 如果是 {@link CompletableFuture} 类型，返回<code>true</code>
     */
    default boolean isCompletableFuture() {
        return resolveOptional().map(c -> c == CompletableFuture.class).orElse(false);
    }

    /**
     * 判断当前类型是否是集合或者其子类型的子类
     *
     * @return 如果是 {@link Collection 集合}类型或者其子类，返回<code>true</code>
     */
    default boolean isCollection() {
        return resolveOptional().map(Collection.class::isAssignableFrom).orElse(false);
    }

    /**
     * 判断当前类型是否是 {@link Optional 可选}类型
     *
     * @return 如果是 {@link Optional 可选}类型，返回<code>true</code>
     */
    default boolean isOptional() {
        return resolveOptional().map(c -> c == Optional.class).orElse(false);
    }

    /**
     * 判断当前类型是否是数组
     *
     * @return 如果是数组，返回<code>true</code>
     */
    default boolean isArray() {
        return resolveOptional().map(Class::isArray).orElse(false);
    }

    /**
     * 判断当前类型是的实际实体对象是否被包装过
     *
     * @return 如果是包装类型，返回<code>true</code>
     */
    default boolean isWrapped() {
        return isPage() || isCompletableFuture() || isCollection() || isOptional();
    }

    /**
     * 判断当前类型是的是基本类型
     *
     * @return 如果是基本类型，返回<code>true</code>
     */
    default boolean isBasic() {
        return resolveOptional().map(MetaUtils::isBasic).orElse(false);
    }

    /**
     * 沿着当前类的继承和实现关系，找到指定的父类或者接口的泛型解析器
     *
     * @param type 继承类或者实现接口
     * @return 泛型解析器
     */
    GenericType as(Class<?> type);

    /**
     * 解析泛型类
     *
     * @param indices 索引
     * @return 泛型实际类型
     */
    Optional<Class<?>> resolveGenericOptional(int... indices);

    /**
     * 解析泛型类，如果指定位置泛型类型不存在，抛出异常
     *
     * @param indices 索引
     * @return 泛型实际类型
     */
    default Class<?> resolveGeneric(int... indices) {
        return resolveGenericOptional(indices).orElseThrow(GenericTypeException::new);
    }

    /**
     * 只解析 {@link Class} / {@link java.lang.reflect.ParameterizedType}和 {@link java.lang.reflect.GenericArrayType}
     * 其他类型返回空
     *
     * @return 原始类型
     */
    Optional<Class<?>> resolveOptional();

    /**
     * 只解析 {@link Class} / {@link java.lang.reflect.ParameterizedType}和 {@link java.lang.reflect.GenericArrayType}，如果类型不存在，抛出异常
     *
     * @return 原始类型
     */
    default Class<?> resolve() {
        return resolveOptional().orElseThrow(GenericTypeException::new);
    }

    /**
     * 解析指定索引位置的泛型
     *
     * @param indices 泛型索引
     * @return 泛型解析器
     */
    GenericType resolveType(int... indices);

    /**
     * 作为CompletableFuture集合解析
     *
     * @return CompletableFuture泛型解析器
     */
    default GenericType asCompletableFuture() {
        return as(CompletableFuture.class);
    }

    /**
     * 作为Optional解析
     *
     * @return Optional泛型解析器
     */
    default GenericType asOptional() {
        return as(Optional.class);
    }

    /**
     * 作为Map集合解析
     *
     * @return Map集合泛型解析器
     */
    default GenericType asMap() {
        return as(Map.class);
    }

    /**
     * 作为List集合解析
     *
     * @return List集合泛型解析器
     */
    default GenericType asList() {
        return as(List.class);
    }

    /**
     * 作为Set集合解析
     *
     * @return Set集合泛型解析器
     */
    default GenericType asSet() {
        return as(Set.class);
    }

    /**
     * 判断当前的类是否可以赋值给指定类型类
     *
     * @param clazz 父类或者接口
     * @return 如果可以，返回<code>true</code>
     */
    default boolean isAssignableTo(Class<?> clazz) {
        return clazz.isAssignableFrom(resolve());
    }

    /**
     * 判断当前类是否跟指定的类相同
     *
     * @param clazz 类或者接口
     * @return 如果相同，返回<code>true</code>
     */
    default boolean is(Class<?> clazz) {
        return clazz == resolve();
    }
}
