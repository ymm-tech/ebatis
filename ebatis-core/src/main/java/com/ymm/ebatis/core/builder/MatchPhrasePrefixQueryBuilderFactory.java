package com.ymm.ebatis.core.builder;

import com.ymm.ebatis.core.annotation.MatchPhrasePrefix;
import com.ymm.ebatis.core.meta.ConditionMeta;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.MatchPhrasePrefixQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * @author 章多亮
 * @since 2020/1/7 10:27
 */
public class MatchPhrasePrefixQueryBuilderFactory extends AbstractQueryBuilderFactory<MatchPhrasePrefixQueryBuilder, MatchPhrasePrefix> {
    public static final MatchPhrasePrefixQueryBuilderFactory INSTANCE = new MatchPhrasePrefixQueryBuilderFactory();

    private MatchPhrasePrefixQueryBuilderFactory() {
    }

    @Override
    protected void setOptionalMeta(MatchPhrasePrefixQueryBuilder builder, MatchPhrasePrefix matchPhrasePrefix) {
        builder.maxExpansions(matchPhrasePrefix.maxExpansions())
                .slop(matchPhrasePrefix.slop());

        if (StringUtils.isNotBlank(matchPhrasePrefix.analyzer())) {
            builder.analyzer(matchPhrasePrefix.analyzer());
        }
    }

    @Override
    protected MatchPhrasePrefixQueryBuilder doCreate(ConditionMeta<?> conditionMeta, Object condition) {
        return QueryBuilders.matchPhrasePrefixQuery(conditionMeta.getName(), condition);
    }
}
