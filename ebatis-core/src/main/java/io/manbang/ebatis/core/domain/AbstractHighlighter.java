package io.manbang.ebatis.core.domain;

import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;

import java.util.Map;

/**
 * @author weilong.hu
 * @since 2021/5/13 15:51
 */
public abstract class AbstractHighlighter<T extends Highlighter<T>> implements Highlighter<T> {
    protected String[] preTags;

    protected String[] postTags;

    protected Integer fragmentSize;

    protected Integer numOfFragments;

    protected String highlighterType;

    protected String fragmenter;

    protected Object highlightCondition;

    protected String order = "none";

    protected Boolean highlightFilter;

    protected Boolean forceSource;

    protected HighlightBuilder.BoundaryScannerType boundaryScannerType;

    protected Integer boundaryMaxScan;

    protected char[] boundaryChars;

    protected String boundaryScannerLocale;

    protected Integer noMatchSize;

    protected Integer phraseLimit;

    protected Map<String, Object> options;

    protected Boolean requireFieldMatch;

    @Override
    public T preTags(String... preTags) {
        this.preTags = preTags;
        return (T) this;
    }

    @Override
    public T postTags(String... postTags) {
        this.postTags = postTags;
        return (T) this;
    }

    @Override
    public T fragmentSize(Integer fragmentSize) {
        this.fragmentSize = fragmentSize;
        return (T) this;
    }

    @Override
    public T numOfFragments(Integer numOfFragments) {
        this.numOfFragments = numOfFragments;
        return (T) this;
    }

    @Override
    public T highlighterType(String highlighterType) {
        this.highlighterType = highlighterType;
        return (T) this;
    }

    @Override
    public T fragmenter(String fragmenter) {
        this.fragmenter = fragmenter;
        return (T) this;
    }

    @Override
    public T highlightCondition(Object highlightCondition) {
        this.highlightCondition = highlightCondition;
        return (T) this;
    }

    @Override
    public T order(String order) {
        this.order = order;
        return (T) this;
    }

    @Override
    public T highlightFilter(Boolean highlightFilter) {
        this.highlightFilter = highlightFilter;
        return (T) this;
    }

    @Override
    public T boundaryScannerType(String boundaryScannerType) {
        this.boundaryScannerType = HighlightBuilder.BoundaryScannerType.fromString(boundaryScannerType);
        ;
        return (T) this;
    }

    @Override
    public T boundaryMaxScan(Integer boundaryMaxScan) {
        this.boundaryMaxScan = boundaryMaxScan;
        return (T) this;
    }

    @Override
    public T boundaryChars(char[] boundaryChars) {
        this.boundaryChars = boundaryChars;
        return (T) this;
    }

    @Override
    public T boundaryScannerLocale(String boundaryScannerLocale) {
        this.boundaryScannerLocale = boundaryScannerLocale;
        return (T) this;
    }

    @Override
    public T options(Map<String, Object> options) {
        this.options = options;
        return (T) this;
    }

    @Override
    public T requireFieldMatch(Boolean requireFieldMatch) {
        this.requireFieldMatch = requireFieldMatch;
        return (T) this;
    }

    @Override
    public T noMatchSize(Integer noMatchSize) {
        this.noMatchSize = noMatchSize;
        return (T) this;
    }

    @Override
    public T phraseLimit(Integer phraseLimit) {
        this.phraseLimit = phraseLimit;
        return (T) this;
    }

    @Override
    public T forceSource(Boolean forceSource) {
        this.forceSource = forceSource;
        return (T) this;
    }

}
