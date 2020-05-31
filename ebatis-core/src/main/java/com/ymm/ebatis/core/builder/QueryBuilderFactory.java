package com.ymm.ebatis.core.builder;

import com.ymm.ebatis.core.meta.ConditionMeta;
import org.elasticsearch.index.query.QueryBuilder;

/**
 * @author duoliang.zhang
 */
public interface QueryBuilderFactory {
    static QueryBuilderFactory auto() {
        return AutoQueryBuilderFactory.INSTANCE;
    }

    static QueryBuilderFactory bool() {
        return BoolQueryBuilderFactory.INSTANCE;
    }

    static QueryBuilderFactory constantScore() {
        return ConstantScoreQueryBuilderFactory.INSTANCE;
    }

    static QueryBuilderFactory exists() {
        return ExistsQueryBuilderFactory.INSTANCE;
    }

    static QueryBuilderFactory functionScore() {
        return FunctionScoreQueryBuilderFactory.INSTANCE;
    }

    static QueryBuilderFactory fuzzy() {
        return FuzzyQueryBuilderFactory.INSTANCE;
    }

    static QueryBuilderFactory geoDistance() {
        return GeoDistanceQueryBuilderFactory.INSTANCE;
    }

    static QueryBuilderFactory geoShape() {
        return GeoShapeQueryBuilderFactory.INSTANCE;
    }

    static QueryBuilderFactory ids() {
        return IdsQueryBuilderFactory.INSTANCE;
    }

    static QueryBuilderFactory matchAll() {
        return MatchAllQueryBuilderFactory.INSTANCE;
    }

    static QueryBuilderFactory matchPhrasePrefix() {
        return MatchPhrasePrefixQueryBuilderFactory.INSTANCE;
    }

    static QueryBuilderFactory matchPhrase() {
        return MatchPhraseQueryBuilderFactory.INSTANCE;
    }

    static QueryBuilderFactory match() {
        return MatchQueryBuilderFactory.INSTANCE;
    }

    static QueryBuilderFactory multiMatch() {
        return MultiMatchQueryBuilderFactory.INSTANCE;
    }

    static QueryBuilderFactory term() {
        return TermQueryBuilderFactory.INSTANCE;
    }

    static QueryBuilderFactory terms() {
        return TermsQueryBuilderFactory.INSTANCE;
    }

    static QueryBuilderFactory wildCard() {
        return WildCardQueryBuilderFactory.INSTANCE;
    }

    static QueryBuilderFactory field() {
        return FieldQueryBuilderFactory.INSTANCE;
    }

    static QueryBuilderFactory boosting() {
        return BoostingQueryBuilderFactory.INSTANCE;
    }

    /**
     * 创建查询构建器
     *
     * @param meta      查询条件
     * @param condition 条件
     * @return 查询构建器
     */
    QueryBuilder create(ConditionMeta meta, Object condition);
}
