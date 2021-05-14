package io.manbang.ebatis.core.domain;

/**
 * @author weilong.hu
 * @since 2021/5/13 17:56
 */
public interface HighlighterBuilder extends Highlighter<HighlighterBuilder> {
    HighlighterBuilder tagsSchema(String schemaName);

    HighlighterBuilder encoder(String encoder);

    HighlighterBuilder useExplicitFieldOrder(boolean useExplicitFieldOrder);

    HighlighterBuilder field(HighlighterField field);
}
