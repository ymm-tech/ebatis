package com.ymm.ebatis.meta;

import com.ymm.ebatis.executor.MethodExecutor;
import com.ymm.ebatis.request.RequestFactory;
import com.ymm.ebatis.request.RequestFactoryType;
import com.ymm.ebatis.response.ResponseExtractor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

/**
 * 接口方法元数据定义，通过方法的接口签名可以得到很多元数据信息，比如可以通过注解确定此接口是什么类型的请求，以此确定请求工厂；
 * 方法签名只支持引用类型条件参数、Pageable和ResponseExtractor，顺序可以随意定义；同时还可以根据方法的返回值，来确定ES的返回字段，已经请求的同步异步方式等
 *
 * @author 章多亮
 * @since 2019/12/17 14:42
 */
public interface MethodMeta extends AnnotatedMeta<Method> {
    /**
     * 根据方法实例生成方法元数据
     *
     * @param method 方法实例
     * @return 方法元数据
     */
    static MethodMeta from(Method method) {
        return CachedMethodMeta.of(method);
    }

    /**
     * 获取方法应的构建器
     *
     * @return dsl构建器
     */
    RequestFactoryType getRequestFactoryType();

    default RequestFactory getRequestFactory() {
        return getRequestFactoryType().getRequestFactory();
    }


    /**
     * 获取参数元数据列表
     *
     * @return 参数列表
     */
    ParameterMeta[] getParameterMetas();

    /**
     * 获取方法执行器，每个接口的定义都会对应到一个方法执行器上面
     *
     * @return 方法执行器
     */
    default MethodExecutor getExecutor() {
        return null;
    }

    /**
     * 获取方法的对应的业务请求类型
     *
     * @return 请求类型枚举
     */
    default RequestType getRequestType() {
        return RequestType.SEARCH;
    }

    /**
     * 获取方法的返回结果类型，不同的结果类型，会影响到ES响应的抽提和封装方式
     *
     * @return 结果类型
     */
    default ResultType getResultType() {
        return ResultType.OTHER;
    }

    /**
     * 获取方法的ES响应抽提器，可以通过方法声明直接传入，也可以不传入，系统会根据方法的返回值判断利用什么抽提器执行结果转换
     *
     * @param args 请求实参
     * @return 方法抽提器
     */
    default Optional<ResponseExtractor<?>> getResponseExtractor(Object[] args) {
        return Optional.empty();
    }

    /**
     * 获取注解元素
     *
     * @return 方法
     * @see #getMethod()
     */
    @Override
    default Method getElement() {
        return getMethod();
    }

    /**
     * 后去描述的方法
     *
     * @return 方法
     */
    Method getMethod();

    /**
     * 获取方法名称
     *
     * @return 方法名称
     */
    default String getName() {
        return getMethod().getName();
    }

    /**
     * 获取方法的返回值
     *
     * @return 返回值类型
     */
    default Class<?> getReturnType() {
        return getMethod().getReturnType();
    }


    /**
     * 获取参数列表
     *
     * @return 参数列表
     */
    default Parameter[] getParameters() {
        return getMethod().getParameters();
    }

    /**
     * 获取方法的第一个形参
     *
     * @return 形参
     */
    default Parameter getFirstParameter() {
        return getMethod().getParameters()[0];
    }

    /**
     * 获取方法的最后一个形参
     *
     * @return 形参
     */
    default Parameter getLastParameter() {
        return getMethod().getParameters()[getMethod().getParameterCount() - 1];
    }


    /**
     * 获取方法返回值指定类型注解
     *
     * @param annotationClass 注解类型
     * @param <A>             具体注解类型
     * @return 返回值注解实例
     */
    default <A extends Annotation> Optional<A> getReturnAnnotation(Class<A> annotationClass) {
        return Optional.ofNullable(getMethod().getAnnotatedReturnType().getAnnotation(annotationClass));
    }

    /**
     * 获取声明此方法的类的注解
     *
     * @param annotationClass 注解类型
     * @param <A>             具体的注解类型
     * @return 注解实例
     */
    default <A extends Annotation> Optional<A> getDeclaringClassAnnotation(Class<A> annotationClass) {
        return Optional.ofNullable(getMethod().getDeclaringClass().getAnnotation(annotationClass));
    }
}
