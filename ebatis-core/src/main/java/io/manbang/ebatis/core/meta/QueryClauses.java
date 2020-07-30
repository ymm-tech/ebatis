package io.manbang.ebatis.core.meta;

import io.manbang.ebatis.core.annotation.Ignore;
import io.manbang.ebatis.core.annotation.Must;
import io.manbang.ebatis.core.builder.QueryClauseType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 章多亮
 * @since 2020/5/27 19:12
 */
public class QueryClauses {
    private QueryClauses() {
        throw new UnsupportedOperationException();
    }

    public static Class<? extends Annotation> getQueryClauseClass(AnnotatedMeta<?> meta) {
        for (QueryClauseType queryClauseType : QueryClauseType.values()) {
            Class<? extends Annotation> queryClauseClass = queryClauseType.getQueryClauseClass();
            if (meta.isAnnotationPresent(queryClauseClass)) {
                return queryClauseClass;
            }
        }

        // 没有注解的认为是Must
        return Must.class;
    }

    public static Map<Class<? extends Annotation>, List<FieldMeta>> groupByQueryClause(Class<?> queryClass) {
        Map<Class<? extends Annotation>, List<FieldMeta>> metas = Arrays.stream(queryClass.getFields())
                .filter(QueryClauses::filterField)
                .map(FieldMeta::of)
                .collect(Collectors.groupingBy(FieldMeta::getQueryClauseAnnotationClass, Collectors.toList()));
        return Collections.unmodifiableMap(metas);
    }

    public static boolean filterField(Field field) {
        int modifiers = field.getModifiers();
        // 必须是私有字段
        return Modifier.isPrivate(modifiers)
                && // 非静态字段
                !Modifier.isStatic(modifiers)
                && // 非临时字段
                !Modifier.isTransient(modifiers)
                &&// 没有加上 Ignore注解
                !field.isAnnotationPresent(Ignore.class);
    }
}
