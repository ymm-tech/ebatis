package io.manbang.ebatis.core.builder;

import io.manbang.ebatis.core.annotation.Filter;
import io.manbang.ebatis.core.annotation.Must;
import io.manbang.ebatis.core.annotation.MustNot;
import io.manbang.ebatis.core.annotation.Should;
import io.manbang.ebatis.core.meta.FieldMeta;
import lombok.Getter;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
@Getter
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
        List<QueryBuilder> builders = buildQueryBuilders(fields, instance);
        builders.forEach(combiner::combine);
    }

    private static List<QueryBuilder> buildQueryBuilders(List<FieldMeta> fields, Object instance) {
        val builders = new ArrayList<QueryBuilder>(fields.size());

        for (FieldMeta meta : fields) {
            // Terms 查询的处理方式，跟其他的不一样，需要单独有限处理，其他条件如果遇到数组或者集合，都是一个个分开处理
            if (meta.isTermsQuery()) {
                builderTermsQuery(meta, instance).ifPresent(builders::add);
            } else if (meta.isIdsQuery()) {
                buildIdsQuery(meta, instance).ifPresent(builders::add);
            } else if (meta.isArray()) {
                builders.addAll(buildArrayQuery(meta, instance));
            } else if (meta.isCollection()) {
                builders.addAll(buildCollectionQuery(meta, instance));
            } else {
                buildNormalQuery(meta, instance).ifPresent(builders::add);
            }
        }

        return builders;
    }

    private static Optional<QueryBuilder> buildIdsQuery(FieldMeta meta, Object instance) {
        QueryBuilderFactory queryBuilderFactory = QueryBuilderFactory.ids();
        Object condition = meta.getValue(instance);
        return Optional.ofNullable(queryBuilderFactory.create(meta, condition));
    }

    private static Optional<QueryBuilder> builderTermsQuery(FieldMeta meta, Object instance) {
        QueryBuilderFactory queryBuilderFactory = QueryBuilderFactory.terms();
        Object condition = meta.getValue(instance);
        return Optional.ofNullable(queryBuilderFactory.create(meta, condition));
    }

    private static Optional<QueryBuilder> buildNormalQuery(FieldMeta meta, Object instance) {
        QueryBuilderFactory queryBuilderFactory = meta.getQueryBuilderFactory();
        Object condition = meta.getValue(instance);
        return Optional.ofNullable(queryBuilderFactory.create(meta, condition));
    }

    private static List<QueryBuilder> buildArrayQuery(FieldMeta meta, Object instance) {
        QueryBuilderFactory queryBuilderFactory = meta.getQueryBuilderFactory();
        Object condition = meta.getValue(instance);
        return Optional.ofNullable(condition)
                .map(Object[].class::cast)
                .map(Arrays::stream)
                .orElseGet(Stream::empty)
                .map(v -> queryBuilderFactory.create(meta, v))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static List<QueryBuilder> buildCollectionQuery(FieldMeta meta, Object instance) {
        QueryBuilderFactory queryBuilderFactory = meta.getQueryBuilderFactory();
        Object condition = meta.getValue(instance);
        return Optional.ofNullable(condition)
                .map(x -> (Collection<?>) x)
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .map(v -> queryBuilderFactory.create(meta, v))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public abstract void addQueryBuilder(BoolQueryBuilder builder, List<FieldMeta> fields, Object instance);

    public boolean isMustNot() {
        return MUST_NOT == this;
    }

    @FunctionalInterface
    private interface QueryClauseCombiner {
        void combine(QueryBuilder queryBuilder);
    }
}
