package com.ymm.ebatis.core.common;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.support.ActiveShardCount;

/**
 * @author weilong.hu
 * @since 2020/6/28 19:36
 */
public class ActiveShardCountUtils {
    private static final String ACTIVE_SHARD_COUNT_DEFAULT = "-2";
    private static final String ALL_ACTIVE_SHARDS = "-1";

    public static ActiveShardCount getActiveShardCount(String waitForActiveShards) {
        if (StringUtils.isBlank(waitForActiveShards)) {
            return ActiveShardCount.DEFAULT;
        }
        if (ACTIVE_SHARD_COUNT_DEFAULT.equals(waitForActiveShards)) {
            return ActiveShardCount.DEFAULT;
        }
        if (ALL_ACTIVE_SHARDS.equals(waitForActiveShards)) {
            return ActiveShardCount.ALL;
        }
        return ActiveShardCount.parseString(waitForActiveShards);
    }

}
