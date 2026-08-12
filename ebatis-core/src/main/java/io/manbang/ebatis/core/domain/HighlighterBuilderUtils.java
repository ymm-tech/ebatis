package io.manbang.ebatis.core.domain;

import io.manbang.ebatis.core.builder.QueryBuilderFactory;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;

/**
 * @author weilong.hu
 * @since 2021/5/14 14:19
 */
public class HighlighterBuilderUtils {
    public static HighlightBuilder toHighlighterBuilder(HighlighterBuilder highlighterBuilder) {
        final HighlightBuilder highlight = SearchSourceBuilder.highlight();
        highlighterBuilder.fields().forEach(field -> highlight.field(toHighlightField(field)));
        final HighlightBuilder highlightBuilder = highlight.encoder(highlighterBuilder.encoder()).useExplicitFieldOrder(highlighterBuilder.useExplicitFieldOrder())
                .preTags(highlighterBuilder.preTags()).postTags(highlighterBuilder.postTags()).fragmentSize(highlighterBuilder.fragmentSize()).numOfFragments(highlighterBuilder.numOfFragments()).highlighterType(highlighterBuilder.highlighterType())
                .fragmenter(highlighterBuilder.fragmenter()).highlightQuery(QueryBuilderFactory.bool().create(null,
                        highlighterBuilder.highlightCondition())).order(highlighterBuilder.order())
                .highlightFilter(highlighterBuilder.highlightFilter()).forceSource(highlighterBuilder.forceSource()).boundaryMaxScan(highlighterBuilder.boundaryMaxScan())
                .boundaryChars(highlighterBuilder.boundaryChars()).boundaryScannerLocale(highlighterBuilder.boundaryScannerLocale()).noMatchSize(highlighterBuilder.noMatchSize()).phraseLimit(highlighterBuilder.phraseLimit())
                .options(highlighterBuilder.options()).requireFieldMatch(highlighterBuilder.requireFieldMatch());
        if (StringUtils.isNotBlank(highlighterBuilder.tagsSchema())) {
            highlight.tagsSchema(highlighterBuilder.tagsSchema());
        }
        if (StringUtils.isNotBlank(highlighterBuilder.boundaryScannerType())) {
            highlight.boundaryScannerType(highlighterBuilder.boundaryScannerType());
        }
        return highlightBuilder;
    }

    public static HighlightBuilder.Field toHighlightField(HighlighterField field) {
        final HighlightBuilder.Field f = new HighlightBuilder.Field(field.name()).fragmentOffset(field.fragmentOffset()).matchedFields(field.matchedFields())
                .preTags(field.preTags()).postTags(field.postTags()).fragmentSize(field.fragmentSize()).numOfFragments(field.numOfFragments()).highlighterType(field.highlighterType())
                .fragmenter(field.fragmenter()).highlightQuery(QueryBuilderFactory.bool().create(null, field.highlightCondition())).order(field.order())
                .highlightFilter(field.highlightFilter()).forceSource(field.forceSource()).boundaryMaxScan(field.boundaryMaxScan())
                .boundaryChars(field.boundaryChars()).boundaryScannerLocale(field.boundaryScannerLocale()).noMatchSize(field.noMatchSize()).phraseLimit(field.phraseLimit())
                .options(field.options()).requireFieldMatch(field.requireFieldMatch());
        if (StringUtils.isNotBlank(field.boundaryScannerType())) {
            f.boundaryScannerType(field.boundaryScannerType());
        }
        return f;
    }
}
