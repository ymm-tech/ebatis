package io.manbang.ebatis.core.domain;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * @author weilong.hu
 * @since 2021/5/13 16:22
 */
class SimpleHighlighter extends AbstractHighlighter<HighlighterBuilder> implements HighlighterBuilder {
    private final List<HighlighterField> fields;
    private String tagsSchema;
    private String encoder;
    private boolean useExplicitFieldOrder = false;

    SimpleHighlighter() {
        fields = new ArrayList<>();
    }

    @Override
    public HighlighterBuilder tagsSchema(String tagsSchema) {
        this.tagsSchema = tagsSchema;
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
    public HighlighterBuilder fields(HighlighterField... fields) {
        this.fields.addAll(Lists.newArrayList(fields));
        return this;
    }

    @Override
    public String tagsSchema() {
        return tagsSchema;
    }

    @Override
    public String encoder() {
        return encoder;
    }

    @Override
    public boolean useExplicitFieldOrder() {
        return useExplicitFieldOrder;
    }

    @Override
    public List<HighlighterField> fields() {
        return fields;
    }
}
