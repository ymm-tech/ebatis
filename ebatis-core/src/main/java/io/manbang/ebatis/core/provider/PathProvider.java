package io.manbang.ebatis.core.provider;

import org.apache.lucene.search.join.ScoreMode;

/**
 * @author weilong.hu
 * @since 2020/12/4 11:47
 */
@FunctionalInterface
public interface PathProvider extends Provider {
    /**
     * nested path
     *
     * @return nested path
     */
    String getPath();

    /**
     * ScoreMode
     *
     * @return ScoreMode
     */
    default ScoreMode getScoreMode() {
        return ScoreMode.None;
    }
}
