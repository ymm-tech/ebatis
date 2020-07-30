package io.manbang.ebatis.core.domain;

/**
 * 脚本字段
 *
 * @author 章多亮
 */
public interface ScriptField {
    /**
     * 创建脚本字段
     *
     * @param name   字段名称
     * @param script 脚本
     * @return 脚本字段
     */
    static ScriptField of(String name, Script script) {
        return new SimpleScriptField(name, script);
    }

    /**
     * 获取字段名称
     *
     * @return 名称
     */
    String getName();

    /**
     * 获取脚本
     *
     * @return 脚本
     */
    Script getScript();

    /**
     * 判断是否忽略失败
     *
     * @return 忽略失败，返回<code>true</code>
     */
    boolean isIgnoreFailure();

    /**
     * 设置是否忽略失败
     *
     * @param ignoreFailure 失败与否
     */
    void setIgnoreFailure(boolean ignoreFailure);
}
