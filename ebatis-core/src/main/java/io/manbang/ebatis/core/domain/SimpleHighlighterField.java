package io.manbang.ebatis.core.domain;

/**
 * @author weilong.hu
 * @since 2021/5/13 17:47
 */
class SimpleHighlighterField extends AbstractHighlighter<HighlighterField> implements HighlighterField {
    private String name;
    private int fragmentOffset = -1;
    private String[] matchedFields;

    SimpleHighlighterField(String name) {
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
    public int fragmentOffset() {
        return fragmentOffset;
    }

    @Override
    public String[] matchedFields() {
        return matchedFields;
    }

    @Override
    public String name() {
        return name;
    }
}
