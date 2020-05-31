package com.ymm.ebatis.core.provider;

import com.ymm.ebatis.core.domain.Script;

/**
 * @author 章多亮
 * @since 2019/12/30 9:51
 */
@FunctionalInterface
public interface ScriptProvider extends Provider {

    /**
     * 获取入参脚本
     *
     * @return 脚本
     */
    Script getScript();
}
