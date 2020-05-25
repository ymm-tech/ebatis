package com.ymm.ebatis.builder;

import com.ymm.ebatis.annotation.MatchPhrase;
import com.ymm.ebatis.meta.ConditionMeta;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * @author 章多亮
 * @since 2020/1/7 10:20
 */
public class MatchPhraseQueryBuilderFactory extends AbstractQueryBuilderFactory<MatchPhraseQueryBuilder, MatchPhrase> {
    public static final MatchPhraseQueryBuilderFactory INSTANCE = new MatchPhraseQueryBuilderFactory();

    private MatchPhraseQueryBuilderFactory() {
    }

    @Override
    protected void setOptionalMeta(MatchPhraseQueryBuilder builder, MatchPhrase matchPhrase) {
        builder.zeroTermsQuery(matchPhrase.zeroTermsQuery())
                .slop(matchPhrase.slop());

        if (StringUtils.isNotBlank(matchPhrase.analyzer())) {
            builder.analyzer(matchPhrase.analyzer());
        }
    }

    @Override
    protected MatchPhraseQueryBuilder doCreate(ConditionMeta<?> conditionMeta, Object condition) {
        return QueryBuilders.matchPhraseQuery(conditionMeta.getName(), condition);
    }
}
