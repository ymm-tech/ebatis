package com.ymm.ebatis.executor;

import com.ymm.ebatis.annotation.Filter;
import com.ymm.ebatis.annotation.Must;
import com.ymm.ebatis.annotation.MustNot;
import com.ymm.ebatis.annotation.Should;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.BooleanMemberValue;
import javassist.bytecode.annotation.StringMemberValue;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 此枚举类主要用于创建方法合成类，将方法入参的注解转移到对应的合成类属性上面
 *
 * @author 章多亮
 * @since 2019/12/22
 */
public enum QueryClauseAnnotation {
    /**
     * must语句
     */
    MUST(Must.class) {
        @Override
        public javassist.bytecode.annotation.Annotation createJavassistAnnotation(Annotation annotation, ConstPool constPool) {
            Must must = (Must) annotation;
            javassist.bytecode.annotation.Annotation a = new javassist.bytecode.annotation.Annotation(Must.class.getName(), constPool);
            a.addMemberValue(ATTR_NESTED, new BooleanMemberValue(must.nested(), constPool));
            return a;
        }
    },
    /**
     * must_not语句
     */
    MUST_NOT(MustNot.class) {
        @Override
        public javassist.bytecode.annotation.Annotation createJavassistAnnotation(Annotation annotation, ConstPool constPool) {
            MustNot mustNot = (MustNot) annotation;
            javassist.bytecode.annotation.Annotation a = new javassist.bytecode.annotation.Annotation(MustNot.class.getName(), constPool);
            a.addMemberValue(ATTR_NESTED, new BooleanMemberValue(mustNot.nested(), constPool));
            return a;
        }
    },
    /**
     * filter语句
     */
    FILTER(Filter.class) {
        @Override
        public javassist.bytecode.annotation.Annotation createJavassistAnnotation(Annotation annotation, ConstPool constPool) {
            Filter filter = (Filter) annotation;
            javassist.bytecode.annotation.Annotation a = new javassist.bytecode.annotation.Annotation(Filter.class.getName(), constPool);
            a.addMemberValue(ATTR_NESTED, new BooleanMemberValue(filter.nested(), constPool));
            return a;
        }
    },
    /**
     * should语句
     */
    SHOULD(Should.class) {
        @Override
        public javassist.bytecode.annotation.Annotation createJavassistAnnotation(Annotation annotation, ConstPool constPool) {
            Should should = (Should) annotation;
            javassist.bytecode.annotation.Annotation a = new javassist.bytecode.annotation.Annotation(Should.class.getName(), constPool);
            a.addMemberValue(ATTR_NESTED, new BooleanMemberValue(should.nested(), constPool));
            a.addMemberValue(ATTR_MINIMUM_SHOULD_MATCH, new StringMemberValue(should.minimumShouldMatch(), constPool));
            return a;
        }
    };
    private static final String ATTR_NESTED = "nested";
    private static final String ATTR_MINIMUM_SHOULD_MATCH = "minimumShouldMatch";
    private static final Map<Class<?>, QueryClauseAnnotation> QUERY_CLAUSES;

    static {
        QUERY_CLAUSES = Stream.of(values()).collect(Collectors.toMap(QueryClauseAnnotation::getQueryClauseClass, a -> a));
    }

    private final Class<? extends Annotation> queryClauseClass;

    QueryClauseAnnotation(Class<? extends Annotation> queryClauseClass) {
        this.queryClauseClass = queryClauseClass;
    }

    public static Optional<QueryClauseAnnotation> valueOf(Class<? extends Annotation> queryClauseClass) {
        return Optional.ofNullable(QUERY_CLAUSES.get(queryClauseClass));
    }

    public Class<? extends Annotation> getQueryClauseClass() {
        return queryClauseClass;
    }

    public abstract javassist.bytecode.annotation.Annotation createJavassistAnnotation(Annotation annotation, ConstPool constPool);
}
