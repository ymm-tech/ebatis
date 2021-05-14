package io.manbang.ebatis.core.domain;

import java.util.Map;

/**
 * @author weilong.hu
 * @since 2021/5/13 15:51
 */
abstract class AbstractHighlighter<T extends Highlighter<T>> implements Highlighter<T> {
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

    protected String boundaryScannerType;

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
        this.boundaryScannerType = boundaryScannerType;
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

    @Override
    public String[] preTags() {
        return preTags;
    }

    @Override
    public String[] postTags() {
        return postTags;
    }

    @Override
    public Integer fragmentSize() {
        return fragmentSize;
    }

    @Override
    public Integer numOfFragments() {
        return numOfFragments;
    }

    @Override
    public String highlighterType() {
        return highlighterType;
    }

    @Override
    public String fragmenter() {
        return fragmenter;
    }

    @Override
    public Object highlightCondition() {
        return highlightCondition;
    }

    @Override
    public String order() {
        return order;
    }

    @Override
    public Boolean highlightFilter() {
        return highlightFilter;
    }

    @Override
    public Boolean forceSource() {
        return forceSource;
    }

    @Override
    public String boundaryScannerType() {
        return boundaryScannerType;
    }

    @Override
    public Integer boundaryMaxScan() {
        return boundaryMaxScan;
    }

    @Override
    public char[] boundaryChars() {
        return boundaryChars;
    }

    @Override
    public String boundaryScannerLocale() {
        return boundaryScannerLocale;
    }

    @Override
    public Integer noMatchSize() {
        return noMatchSize;
    }

    @Override
    public Integer phraseLimit() {
        return phraseLimit;
    }

    @Override
    public Map<String, Object> options() {
        return options;
    }

    @Override
    public Boolean requireFieldMatch() {
        return requireFieldMatch;
    }
}
