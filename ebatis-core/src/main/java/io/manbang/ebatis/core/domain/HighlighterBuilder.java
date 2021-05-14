package io.manbang.ebatis.core.domain;

import java.util.List;

/**
 * @author weilong.hu
 * @since 2021/5/13 17:56
 */
public interface HighlighterBuilder extends Highlighter<HighlighterBuilder> {
    HighlighterBuilder tagsSchema(String tagsSchema);

    HighlighterBuilder encoder(String encoder);

    HighlighterBuilder useExplicitFieldOrder(boolean useExplicitFieldOrder);

    HighlighterBuilder addFields(HighlighterField... fields);

    String tagsSchema();

    String encoder();

    boolean useExplicitFieldOrder();

    List<HighlighterField> fields();
}
