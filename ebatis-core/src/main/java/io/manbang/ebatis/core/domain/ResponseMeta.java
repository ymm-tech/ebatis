package io.manbang.ebatis.core.domain;

import java.util.Map;

/**
 * @author duoliang.zhang
 * @since 2019/12/26 17:46:39
 */
public interface ResponseMeta {
    /**
     * 获取文档总数
     *
     * @return 文档总数
     */
    long getTotal();

    /**
     * 获取文档偏移量
     *
     * @return 文档偏移量
     */
    int getFrom();

    /**
     * 获取分页大小
     *
     * @return 分页大小
     */
    int getSize();

    /**
     * 获取执行操作的索引
     *
     * @return 索引
     */
    String getIndex();

    /**
     * 获取操作的类型
     *
     * @return 类型
     */
    String getType();

    /**
     * 获取执行时间
     *
     * @return 时间
     */
    int getTook();

    /**
     * 获取文档Id
     *
     * @return Id
     */
    String getId();

    /**
     * 判断执行是否超时
     *
     * @return 超时，返回<code>true</code>
     */
    boolean isTimeout();

    /**
     * 判读执行是否成功
     *
     * @return 成功，返回<code>true</code>
     */
    boolean isSuccess();

    /**
     * 如果发生异常，错误原因是个啥
     *
     * @return 错误原因
     */
    String getCause();

    /**
     * 获取score
     *
     * @return score
     */
    float getScore();

    /**
     * 获取version
     *
     * @return version
     */
    long getVersion();

    /**
     * doc最后一次修改得序列号
     *
     * @return 序列号
     */
    long getSeqNo();

    /**
     * 获取 PrimaryTerm
     *
     * @return PrimaryTerm
     */
    long getPrimaryTerm();

    /**
     * 获取文档字符串
     *
     * @return 文档字符串
     */
    String getSourceAsString();

    /**
     * 获取map形式文档
     *
     * @return map形式文档
     */
    Map<String, Object> getSourceAsMap();

    /**
     * 获取排序数组
     *
     * @return 排序数组
     */
    Object[] getSortValues();

    /**
     * 获取原始排序数组
     *
     * @return 原始排序数组
     */
    Object[] getRawSortValues();

    /**
     * 获取集群别名
     *
     * @return 集群别名
     */
    String getClusterAlias();

    /**
     * 获取匹配查询
     *
     * @return 匹配查询
     */
    String[] getMatchedQueries();
}
