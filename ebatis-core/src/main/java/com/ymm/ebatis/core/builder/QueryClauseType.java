package com.ymm.ebatis.core.builder;

import com.ymm.ebatis.core.annotation.Filter;
import com.ymm.ebatis.core.annotation.Must;
import com.ymm.ebatis.core.annotation.MustNot;
import com.ymm.ebatis.core.annotation.Should;
import com.ymm.ebatis.core.meta.FieldMeta;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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
                String minimumShouldMatch = StringUtils.trimToNull(should.minimumShouldMatch());
                if (minimumShouldMatch != null) {
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

            QueryBuilderFactory queryBuilderFactory = meta.getQueryBuilderFactory();
            //terms语句特殊处理，接收集合入参
            if (meta.isTermsQuery()) {
                QueryBuilder builder = queryBuilderFactory.create(meta, value);
                if (builder != null) {
                    builders.add(builder);
                }
            } else if (meta.isArray()) {
                Optional.ofNullable(value)
                        .map(Object[].class::cast)
                        .map(Arrays::stream)
                        .orElseGet(Stream::empty)
                        .map(v -> queryBuilderFactory.create(meta, v))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toCollection(() -> builders));
            } else if (meta.isCollection()) {
                Optional.ofNullable(value)
                        .map(Collection.class::cast)
                        .map(Collection::stream)
                        .orElseGet(Stream::empty)
                        .map(v -> queryBuilderFactory.create(meta, v))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toCollection(() -> builders));
            } else {
                QueryBuilder builder = queryBuilderFactory.create(meta, value);
                if (builder != null) {
                    builders.add(builder);
                }
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
