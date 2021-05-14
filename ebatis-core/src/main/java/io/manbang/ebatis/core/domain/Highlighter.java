package io.manbang.ebatis.core.domain;

import java.util.Map;

/**
 * @author weilong.hu
 * @since 2021/5/13 15:37
 */
public interface Highlighter<T extends Highlighter<T>> {
    static HighlighterBuilder highlighter() {
        return new SimpleHighlighter();
    }

    static HighlighterField field(String name) {
        return new SimpleHighlighterField(name);
    }

    T preTags(String... preTags);

    T postTags(String... postTags);

    T fragmentSize(Integer fragmentSize);

    T numOfFragments(Integer numOfFragments);

    T highlighterType(String highlighterType);

    T fragmenter(String fragmenter);

    T highlightCondition(Object highlightCondition);

    T order(String order);

    T highlightFilter(Boolean highlightFilter);

    T boundaryScannerType(String boundaryScannerType);

    T boundaryMaxScan(Integer boundaryMaxScan);

    T boundaryChars(char[] boundaryChars);

    T boundaryScannerLocale(String boundaryScannerLocale);

    T options(Map<String, Object> options);

    T requireFieldMatch(Boolean requireFieldMatch);

    T noMatchSize(Integer noMatchSize);

    T phraseLimit(Integer phraseLimit);

    T forceSource(Boolean forceSource);

    <B> B toBuilder();

}
