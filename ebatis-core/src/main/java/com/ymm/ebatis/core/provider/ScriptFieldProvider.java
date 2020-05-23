package com.ymm.ebatis.core.provider;

import com.ymm.ebatis.core.domain.ScriptField;

/**
 * @author 章多亮
 */
public interface ScriptFieldProvider extends Provider {
    /**
     * 获取脚本字段
     *
     * @return 脚本字段
     */
    ScriptField[] getScriptFields();
}
