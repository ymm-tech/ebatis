package io.manbang.ebatis.core.builder.compatibility;

import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;

import java.io.IOException;

/**
 * @author weilong.hu
 * @since 2021/07/15 14:55
 */
public class CompatibleMatchQueryBuilder extends MatchQueryBuilder {
    public CompatibleMatchQueryBuilder(String fieldName, Object value) {
        super(fieldName, value);
    }

    public CompatibleMatchQueryBuilder(StreamInput in) throws IOException {
        super(in);
    }

    @Override
    public void doXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject(NAME);
        builder.startObject(fieldName());

        builder.field(QUERY_FIELD.getPreferredName(), value());
        builder.field(OPERATOR_FIELD.getPreferredName(), operator().toString());
        if (analyzer() != null) {
            builder.field(ANALYZER_FIELD.getPreferredName(), analyzer());
        }
        if (fuzziness() != null) {
            fuzziness().toXContent(builder, params);
        }
        builder.field(PREFIX_LENGTH_FIELD.getPreferredName(), prefixLength());
        builder.field(MAX_EXPANSIONS_FIELD.getPreferredName(), maxExpansions());
        if (minimumShouldMatch() != null) {
            builder.field(MINIMUM_SHOULD_MATCH_FIELD.getPreferredName(), minimumShouldMatch());
        }
        if (fuzzyRewrite() != null) {
            builder.field(FUZZY_REWRITE_FIELD.getPreferredName(), fuzzyRewrite());
        }
        // LUCENE 4 UPGRADE we need to document this & test this
        builder.field(FUZZY_TRANSPOSITIONS_FIELD.getPreferredName(), fuzzyTranspositions());
        builder.field(LENIENT_FIELD.getPreferredName(), lenient());
        builder.field(ZERO_TERMS_QUERY_FIELD.getPreferredName(), zeroTermsQuery().toString());
        if (cutoffFrequency() != null) {
            builder.field(CUTOFF_FREQUENCY_FIELD.getPreferredName(), cutoffFrequency());
        }
        printBoostAndQueryName(builder);
        builder.endObject();
        builder.endObject();
    }
}
