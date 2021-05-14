package io.manbang.ebatis.core.domain;

import io.manbang.ebatis.core.builder.QueryBuilderFactory;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;

/**
 * @author weilong.hu
 * @since 2021/5/13 17:47
 */
public class SimpleHighlighterField extends AbstractHighlighter<HighlighterField> implements HighlighterField {
    private String name;
    private int fragmentOffset = -1;
    private String[] matchedFields;

    public SimpleHighlighterField(String name) {
        this.name = name;
    }

    @Override
    public HighlighterField fragmentOffset(int fragmentOffset) {
        this.fragmentOffset = fragmentOffset;
        return this;
    }

    @Override
    public HighlighterField matchedFields(String... matchedFields) {
        this.matchedFields = matchedFields;
        return this;
    }

    @Override
    public <B> B toBuilder() {
        return (B) new HighlightBuilder.Field(name).fragmentOffset(fragmentOffset).matchedFields(matchedFields)
                .preTags(preTags).postTags(postTags).fragmentSize(fragmentSize).numOfFragments(numOfFragments).highlighterType(highlighterType)
                .fragmenter(fragmenter).highlightQuery(QueryBuilderFactory.bool().create(null, highlightCondition)).order(order)
                .highlightFilter(highlightFilter).forceSource(forceSource).boundaryScannerType(boundaryScannerType).boundaryMaxScan(boundaryMaxScan)
                .boundaryChars(boundaryChars).boundaryScannerLocale(boundaryScannerLocale).noMatchSize(noMatchSize).phraseLimit(phraseLimit)
                .options(options).requireFieldMatch(requireFieldMatch);
    }
}
