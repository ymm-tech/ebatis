package com.ymm.ebatis.core.domain;

import java.util.Collections;
import java.util.Map;

/**
 * 脚本
 *
 * @author duoliang.zhang
 */
public interface Script {
    /**
     * 创建有参内联脚本
     *
     * @param content 脚本内容
     * @param params  参数
     * @return 脚本
     */
    static Script inline(String content, Object params) {
        return new InlineScript(content, params);
    }

    /**
     * 创建无惨内联脚本
     *
     * @param content 脚本内容
     * @return 脚本
     */
    static Script inline(String content) {
        return new InlineScript(content, Collections.emptyMap());
    }

    /**
     * 创建存储脚本
     *
     * @param id     脚本Id
     * @param params 参数
     * @return 脚本
     */
    static Script stored(String id, Object params) {
        return new StoredScript(id, params);
    }

    /**
     * 创建无参脚本
     *
     * @param id 脚本Id
     * @return 脚本
     */
    static Script stored(String id) {
        return new StoredScript(id, Collections.emptyMap());
    }

    /**
     * 获取可选参数
     *
     * @return 可选参数
     */
    Map<String, String> getOptions();

    /**
     * 设置可选参数
     *
     * @param options 可选参数
     */
    void setOptions(Map<String, String> options);

    /**
     * 这个参数是个Vo对象，会自动转成，作为Es的脚本入参
     *
     * @param params 参数
     */
    void setParams(Object params);

    /**
     * 设置Map参数
     *
     * @param params 参数
     */
    void setParams(Map<String, Object> params);

    /**
     * 获取脚本预研，默认是<code>painless</code>
     *
     * @return 脚本语言
     */
    default String getLang() {
        return "painless";
    }

    /**
     * 设置脚本语言
     *
     * @param lang 脚本语言
     */
    void setLang(String lang);

    /**
     * 转换成Es的脚本对象
     *
     * @return Es脚本
     */
    org.elasticsearch.script.Script toEsScript();
}
