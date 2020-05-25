package com.ymm.ebatis.provider;

import com.ymm.ebatis.domain.ScriptField;

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
