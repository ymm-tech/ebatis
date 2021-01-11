package io.manbang.ebatis.core.domain;

import org.elasticsearch.search.sort.ScriptSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortMode;

/**
 * 排序对象
 *
 * @author duoliang.zhang
 */
public interface Sort {
    /**
     * 创建指定字段升序排序对象
     *
     * @param name 排序字段名
     * @return 排序对象
     */
    static FieldSort fieldAsc(String name) {
        return new DefaultFieldSort(name, SortDirection.ASC);
    }

    /**
     * 创建指定字段降序排序对象
     *
     * @param name 排序字段名
     * @return 排序对象
     */
    static FieldSort fieldDesc(String name) {
        return new DefaultFieldSort(name, SortDirection.DESC);
    }

    /**
     * 创建指定字段升序排序对象
     *
     * @param name 排序字段名
     * @return 排序对象
     */
    static GeoDistanceSort geoDistanceAsc(String name) {
        return new DefaultGeoDistanceSort(name, SortDirection.ASC);
    }

    /**
     * 创建指定字段降序排序对象
     *
     * @param name 排序字段名
     * @return 排序对象
     */
    static GeoDistanceSort geoDistanceDesc(String name) {
        return new DefaultGeoDistanceSort(name, SortDirection.DESC);
    }

    /**
     * 创建数值类型升序排序脚本
     *
     * @param script 脚本
     * @return 排序脚本
     */
    static ScriptSort scriptNumberAsc(Script script) {
        return new DefaultScriptSort(script, ScriptSortBuilder.ScriptSortType.NUMBER, SortDirection.ASC);
    }

    /**
     * 创建数值类型降序排序脚本
     *
     * @param script 脚本
     * @return 排序脚本
     */
    static ScriptSort scriptNumberDesc(Script script) {
        return new DefaultScriptSort(script, ScriptSortBuilder.ScriptSortType.NUMBER, SortDirection.DESC);
    }

    /**
     * 创建字符串类型升序排序脚本
     *
     * @param script 脚本
     * @return 排序脚本
     */
    static ScriptSort scriptStringAsc(Script script) {
        return new DefaultScriptSort(script, ScriptSortBuilder.ScriptSortType.STRING, SortDirection.ASC);
    }

    /**
     * 创建字符串类型降序排序脚本
     *
     * @param script 脚本
     * @return 排序脚本
     */
    static ScriptSort scriptStringDesc(Script script) {
        return new DefaultScriptSort(script, ScriptSortBuilder.ScriptSortType.STRING, SortDirection.DESC);
    }

    /**
     * 获取排序字段名称
     *
     * @return 字段名称
     */
    String name();

    /**
     * 获取排序模式
     *
     * @return 排序模式
     */
    SortMode sortMode();

    /**
     * 设置排序模式
     *
     * @param sortMode 排序模式
     * @return 排序对象
     */
    Sort sortMode(SortMode sortMode);

    /**
     * 获取排序方向
     *
     * @return 排序方向
     */
    SortDirection direction();

    /**
     * 设置nested objects
     *
     * @param nestedSort nested
     * @return 排序对象
     */
    Sort nested(NestedSort nestedSort);

    /**
     * 转换成排序
     *
     * @return 排序构建器
     */
    SortBuilder<?> toSortBuilder();
}
