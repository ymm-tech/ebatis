package io.manbang.ebatis.core.domain;

/**
 * @author weilong.hu
 * @since 2021/5/13 17:57
 */
public interface HighlighterField extends Highlighter<HighlighterField> {
    HighlighterField fragmentOffset(int fragmentOffset);

    HighlighterField matchedFields(String... matchedFields);
}
