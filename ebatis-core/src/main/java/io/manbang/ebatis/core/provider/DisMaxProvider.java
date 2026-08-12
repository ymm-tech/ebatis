package io.manbang.ebatis.core.provider;

import org.elasticsearch.index.query.DisMaxQueryBuilder;

/**
 * @author weilong.hu
 * @since 2021/5/24 9:43
 */
public interface DisMaxProvider extends Provider {
    /**
     * 默认tie_Breaker值
     */
    float DEFAULT_TIE_BREAKER = 0.0f;

    Object[] conditions();

    /**
     * @return tie_Breaker
     * @see DisMaxQueryBuilder#tieBreaker(float)
     */
    default float tieBreaker() {
        return DEFAULT_TIE_BREAKER;
    }
}
