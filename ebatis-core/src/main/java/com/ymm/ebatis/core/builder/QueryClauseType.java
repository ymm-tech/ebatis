package com.ymm.ebatis.core.builder;

import com.ymm.ebatis.core.annotation.AnnotationConstants;
import com.ymm.ebatis.core.annotation.Filter;
import com.ymm.ebatis.core.annotation.Must;
import com.ymm.ebatis.core.annotation.MustNot;
import com.ymm.ebatis.core.annotation.QueryType;
import com.ymm.ebatis.core.annotation.Should;
import com.ymm.ebatis.core.common.DslUtils;
import com.ymm.ebatis.core.exception.QueryFieldNullException;
import com.ymm.ebatis.core.meta.ConditionMeta;
import com.ymm.ebatis.core.meta.FieldConditionMeta;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 查询组合语句类型
 *
 * @author 章多亮
 */
public enum QueryClauseType {
    /**
     * 必须满足条件
     */
    MUST(Must.class) {
        @Override
        public QueryType getQueryType(Annotation annotation) {
            return ((Must) annotation).queryType();
        }

        @Override
        public void addQueryBuilder(BoolQueryBuilder builder, List<FieldConditionMeta> fields, Object instance) {
            combineQueryBuilder(builder::must, fields, instance);
        }
    },
    /**
     * 必须不满足条件
     */
    MUST_NOT(MustNot.class) {
        @Override
        public QueryType getQueryType(Annotation annotation) {
            return ((MustNot) annotation).queryType();
        }

        @Override
        public void addQueryBuilder(BoolQueryBuilder builder, List<FieldConditionMeta> fields, Object instance) {
            combineQueryBuilder(builder::mustNot, fields, instance);
        }
    },
    /**
     * 可选条件
     */
    SHOULD(Should.class) {
        @Override
        public QueryType getQueryType(Annotation annotation) {
            return ((Should) annotation).queryType();
        }

        @Override
        public void addQueryBuilder(BoolQueryBuilder builder, List<FieldConditionMeta> fields, Object instance) {
            // 有多个Should条件，则只处理其中一个Should，处理多个没有意义，覆盖掉了，而且多个层级是可以分别设置自己的Should#minimumShouldMatch值的
            for (ConditionMeta<? extends AnnotatedElement> field : fields) {
                Should should = field.getAnnotationRequired(Should.class);
                if (!Objects.equals(AnnotationConstants.NO_SET, should.minimumShouldMatch())) {
                    builder.minimumShouldMatch(should.minimumShouldMatch());
                    break;
                }
            }

            combineQueryBuilder(builder::should, fields, instance);
        }
    },
    /**
     * 过滤条件
     */
    FILTER(Filter.class) {
        @Override
        public QueryType getQueryType(Annotation annotation) {
            return ((Filter) annotation).queryType();
        }

        @Override
        public void addQueryBuilder(BoolQueryBuilder builder, List<FieldConditionMeta> fields, Object instance) {
            combineQueryBuilder(builder::filter, fields, instance);
        }
    };
    private static final Map<Class<? extends Annotation>, QueryClauseType> QUERY_CLAUSE_TYPES;

    static {
        QUERY_CLAUSE_TYPES = Stream.of(values()).collect(Collectors.toMap(QueryClauseType::getQueryClauseClass, t -> t));
    }

    private final Map<Class<?>, Method> nestedAnnotationMethods;
    private final Class<? extends Annotation> queryClauseClass;

    QueryClauseType(Class<? extends Annotation> queryClauseClass) {
        this.queryClauseClass = queryClauseClass;
        this.nestedAnnotationMethods = DslUtils.getNestedAnnotationMethods(queryClauseClass);
    }

    public static QueryClauseType valueOf(Class<? extends Annotation> queryClauseTypeClass) {
        return QUERY_CLAUSE_TYPES.get(queryClauseTypeClass);
    }

    private static QueryBuilderElement createQueryElementBuilder(FieldConditionMeta field, Object instance) {
        Object value = field.getValue(instance);
        if (value == null) {
            if (field.isExistsField()) {
                return new QueryBuilderElement(field, null);
            }
            if (!field.isIgnoreNull()) {
                throw new QueryFieldNullException("查询字段不能为空：" + field);
            }
            return null;
        }


        return new QueryBuilderElement(field, value);
    }

    private static void combineQueryBuilder(Function<QueryBuilder, BoolQueryBuilder> combinator, List<FieldConditionMeta> fields, Object instance) {
        fields.stream().map(field -> createQueryElementBuilder(field, instance)).filter(Objects::nonNull).forEach(element -> Arrays.stream(element.toQueryBuilder()).forEach(combinator::apply));
    }


    public Class<? extends Annotation> getQueryClauseClass() {
        return queryClauseClass;
    }

    public Map<Class<?>, Method> getNestedAnnotationMethods() {
        return nestedAnnotationMethods;
    }

    public <A extends Annotation> A getNestedAnnotation(Annotation annotation, Class<A> annotationClass) {
        return AnnotationUtils.getAnnotation(annotation, annotationClass);
    }

    public Method getNestedAnnotationMethod(Class<?> aClass) {
        return nestedAnnotationMethods.get(aClass);
    }

    public abstract QueryType getQueryType(Annotation annotation);

    public abstract void addQueryBuilder(BoolQueryBuilder builder, List<FieldConditionMeta> fields, Object instance);
}
