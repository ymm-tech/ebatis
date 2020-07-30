package io.manbang.ebatis.core.provider;

import io.manbang.ebatis.core.domain.ScriptField;

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
