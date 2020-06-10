package com.ymm.ebatis.core.generic;

import com.ymm.ebatis.core.search.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author 章多亮
 * @since 2020/6/9 11:56
 */
@Slf4j
public class GenericTypeTest {

    @Test
    public void forMethod() {
        Class<OrderMapper> clazz = OrderMapper.class;

        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            MethodGenericType methodGenericType = GenericType.forMethod(method);
            GenericType genericType = methodGenericType.returnType();
            if (genericType.isWrapped()) {
                genericType.resolveGeneric(0).ifPresent(c -> System.out.printf("method: %20s, %s\n", method.getName(), c));
            }
        }
        Assert.assertNotNull(clazz);
    }

    public interface ResponseConverterProvider {
        <M, P extends Serializable> ResponseConverter<M, Comparable<P>> getConverter();

        <P extends Serializable & Cloneable> ResponseConverter<?, Comparable<P>> getConverter1();

        <P extends Serializable & Cloneable> ResponseConverter<?, Comparable<P[]>[]> getConverter2();
    }

    public interface ResponseConverter<M, P> {
        M toModel(P protocol);

        M toProtocol(P protocol);

        int compare();
    }

    static class StringResponseConverter implements ResponseConverter<List<String>, Map<String, SearchResponse[]>> {
        @Override
        public List<String> toModel(Map<String, SearchResponse[]> protocol) {
            return null;
        }

        @Override
        public List<String> toProtocol(Map<String, SearchResponse[]> protocol) {
            return null;
        }

        @Override
        public int compare() {
            return 0;
        }
    }
}