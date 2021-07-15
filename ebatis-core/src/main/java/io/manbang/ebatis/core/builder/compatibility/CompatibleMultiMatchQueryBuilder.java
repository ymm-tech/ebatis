package io.manbang.ebatis.core.builder.compatibility;

import org.elasticsearch.common.ParseField;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

/**
 * @author weilong.hu
 * @since 2021/07/15 15:28
 */
public class CompatibleMultiMatchQueryBuilder extends MultiMatchQueryBuilder {
    private static final ParseField SLOP_FIELD = new ParseField("slop");
    private static final ParseField ZERO_TERMS_QUERY_FIELD = new ParseField("zero_terms_query");
    private static final ParseField LENIENT_FIELD = new ParseField("lenient");
    private static final ParseField CUTOFF_FREQUENCY_FIELD = new ParseField("cutoff_frequency");
    private static final ParseField TIE_BREAKER_FIELD = new ParseField("tie_breaker");
    private static final ParseField USE_DIS_MAX_FIELD = new ParseField("use_dis_max");
    private static final ParseField FUZZY_REWRITE_FIELD = new ParseField("fuzzy_rewrite");
    private static final ParseField MINIMUM_SHOULD_MATCH_FIELD = new ParseField("minimum_should_match");
    private static final ParseField OPERATOR_FIELD = new ParseField("operator");
    private static final ParseField MAX_EXPANSIONS_FIELD = new ParseField("max_expansions");
    private static final ParseField PREFIX_LENGTH_FIELD = new ParseField("prefix_length");
    private static final ParseField ANALYZER_FIELD = new ParseField("analyzer");
    private static final ParseField TYPE_FIELD = new ParseField("type");
    private static final ParseField QUERY_FIELD = new ParseField("query");
    private static final ParseField FIELDS_FIELD = new ParseField("fields");

    public CompatibleMultiMatchQueryBuilder(Object value, String... fields) {
        super(value, fields);
    }

    public CompatibleMultiMatchQueryBuilder(StreamInput in) throws IOException {
        super(in);
    }

    @Override
    public void doXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject(NAME);
        builder.field(QUERY_FIELD.getPreferredName(), value());
        builder.startArray(FIELDS_FIELD.getPreferredName());
        for (Map.Entry<String, Float> fieldEntry : this.fields().entrySet()) {
            builder.value(fieldEntry.getKey() + "^" + fieldEntry.getValue());
        }
        builder.endArray();
        builder.field(TYPE_FIELD.getPreferredName(), type().toString().toLowerCase(Locale.ENGLISH));
        builder.field(OPERATOR_FIELD.getPreferredName(), operator().toString());
        if (analyzer() != null) {
            builder.field(ANALYZER_FIELD.getPreferredName(), analyzer());
        }
        builder.field(SLOP_FIELD.getPreferredName(), slop());
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
        if (useDisMax() != null) {
            builder.field(USE_DIS_MAX_FIELD.getPreferredName(), useDisMax());
        }
        if (tieBreaker() != null) {
            builder.field(TIE_BREAKER_FIELD.getPreferredName(), tieBreaker());
        }
        if (lenient()) {
            builder.field(LENIENT_FIELD.getPreferredName(), lenient());
        }
        if (cutoffFrequency() != null) {
            builder.field(CUTOFF_FREQUENCY_FIELD.getPreferredName(), cutoffFrequency());
        }
        builder.field(ZERO_TERMS_QUERY_FIELD.getPreferredName(), zeroTermsQuery().toString());
        printBoostAndQueryName(builder);
        builder.endObject();
    }
}
