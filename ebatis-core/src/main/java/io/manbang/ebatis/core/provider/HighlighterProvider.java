package io.manbang.ebatis.core.provider;

import io.manbang.ebatis.core.domain.HighlighterBuilder;

/**
 * @author weilong.hu
 * @since 2021/5/13 17:55
 */
@FunctionalInterface
public interface HighlighterProvider extends Provider {
    HighlighterBuilder highlighterBuilder();
}
