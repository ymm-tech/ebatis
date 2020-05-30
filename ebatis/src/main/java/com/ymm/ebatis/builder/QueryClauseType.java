package com.ymm.ebatis.builder;

import com.ymm.ebatis.annotation.AnnotationConstants;
import com.ymm.ebatis.annotation.Filter;
import com.ymm.ebatis.annotation.Must;
import com.ymm.ebatis.annotation.MustNot;
import com.ymm.ebatis.annotation.Should;
import com.ymm.ebatis.meta.FieldMeta;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
        public void addQueryBuilder(BoolQueryBuilder builder, List<FieldMeta> fields, Object instance) {
            combineQueryBuilder(builder::must, fields, instance);
        }
    },
    /**
     * 必须不满足条件
     */
    MUST_NOT(MustNot.class) {
        @Override
        public void addQueryBuilder(BoolQueryBuilder builder, List<FieldMeta> fields, Object instance) {
            combineQueryBuilder(builder::mustNot, fields, instance);
        }
    },
    /**
     * 可选条件
     */
    SHOULD(Should.class) {
        @Override
        public void addQueryBuilder(BoolQueryBuilder builder, List<FieldMeta> fields, Object instance) {
            // 有多个Should条件，则只处理其中一个Should，处理多个没有意义，覆盖掉了，而且多个层级是可以分别设置自己的Should#minimumShouldMatch值的
            for (FieldMeta field : fields) {
                Should should = field.getAnnotation(Should.class);
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
        public void addQueryBuilder(BoolQueryBuilder builder, List<FieldMeta> fields, Object instance) {
            combineQueryBuilder(builder::filter, fields, instance);
        }
    };
    private static final Map<Class<? extends Annotation>, QueryClauseType> QUERY_CLAUSE_TYPES;

    static {
        QUERY_CLAUSE_TYPES = Stream.of(values()).collect(Collectors.toMap(QueryClauseType::getQueryClauseClass, t -> t));
    }

    private final Class<? extends Annotation> queryClauseClass;

    QueryClauseType(Class<? extends Annotation> queryClauseClass) {
        this.queryClauseClass = queryClauseClass;
    }

    public static QueryClauseType valueOf(Class<? extends Annotation> queryClauseTypeClass) {
        return QUERY_CLAUSE_TYPES.get(queryClauseTypeClass);
    }

    private static void combineQueryBuilder(QueryClauseCombiner combiner, List<FieldMeta> fields, Object instance) {
        List<QueryBuilder> builders = new LinkedList<>();

        for (FieldMeta meta : fields) {
            Object value = meta.getValue(instance);

            if (!meta.isBasic()) {
                // 动态组合语句，特殊处理，就是个坑
                if (meta.isArray()) {
                    Arrays.stream((Object[]) value).map(v -> QueryBuilderFactory.auto().create(meta, value)).collect(Collectors.toCollection(() -> builders));
                } else if (meta.isCollection()) {
                    ((Collection<?>) value).stream().map(v -> QueryBuilderFactory.auto().create(meta, value)).collect(Collectors.toCollection(() -> builders));
                }
            }

            QueryBuilder builder = meta.getQueryBuilderFactory().create(meta, value);
            if (builder != null) {
                builders.add(builder);
            }
        }
        builders.forEach(combiner::combine);
    }


    public Class<? extends Annotation> getQueryClauseClass() {
        return queryClauseClass;
    }

    public abstract void addQueryBuilder(BoolQueryBuilder builder, List<FieldMeta> fields, Object instance);

    @FunctionalInterface
    private interface QueryClauseCombiner {
        void combine(QueryBuilder queryBuilder);
    }
}
