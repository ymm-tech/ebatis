package io.manbang.ebatis.core.domain;

import io.manbang.ebatis.core.builder.QueryBuilderFactory;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author weilong.hu
 * @since 2021/5/13 16:22
 */
public class SimpleHighlighter extends AbstractHighlighter<HighlighterBuilder> implements HighlighterBuilder {
    private final List<HighlighterField> fields;
    private String schemaName;
    private String encoder;
    private boolean useExplicitFieldOrder = false;

    public SimpleHighlighter() {
        fields = new ArrayList<>();
    }

    @Override
    public HighlighterBuilder tagsSchema(String schemaName) {
        this.schemaName = schemaName;
        return this;
    }

    @Override
    public HighlighterBuilder encoder(String encoder) {
        this.encoder = encoder;
        return this;
    }

    @Override
    public HighlighterBuilder useExplicitFieldOrder(boolean useExplicitFieldOrder) {
        this.useExplicitFieldOrder = useExplicitFieldOrder;
        return this;
    }

    @Override
    public HighlighterBuilder field(HighlighterField field) {
        fields.add(field);
        return this;
    }

    @Override
    public <B> B toBuilder() {
        final HighlightBuilder highlight = SearchSourceBuilder.highlight();
        fields.forEach(field -> highlight.field((HighlightBuilder.Field) field.toBuilder()));
        final HighlightBuilder highlightBuilder = highlight.encoder(encoder).useExplicitFieldOrder(useExplicitFieldOrder)
                .preTags(preTags).postTags(postTags).fragmentSize(fragmentSize).numOfFragments(numOfFragments).highlighterType(highlighterType)
                .fragmenter(fragmenter).highlightQuery(QueryBuilderFactory.bool().create(null, highlightCondition)).order(order)
                .highlightFilter(highlightFilter).forceSource(forceSource).boundaryScannerType(boundaryScannerType).boundaryMaxScan(boundaryMaxScan)
                .boundaryChars(boundaryChars).boundaryScannerLocale(boundaryScannerLocale).noMatchSize(noMatchSize).phraseLimit(phraseLimit)
                .options(options).requireFieldMatch(requireFieldMatch);
        if (StringUtils.isNotBlank(schemaName)) {
            highlight.tagsSchema(schemaName);
        }
        return (B) highlightBuilder;
    }
}
