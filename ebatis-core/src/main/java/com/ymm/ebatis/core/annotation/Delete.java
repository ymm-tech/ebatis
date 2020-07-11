package com.ymm.ebatis.core.annotation;

import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.index.VersionType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author duoliang.zhang
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Delete {
    String pipeline() default "";

    /**
     * 执行删除时，要求的分片副本数量，默认-2使用es集群默认配置，<code>all</code>表示全副本+主分片，其他数字
     * <table>
     *     <thead>
     *         <tr>
     *             <th>
     *                 值
     *             </th>
     *             <th>
     *                 意义
     *             </th>
     *         </tr>
     *     </thead>
     *     <tbody>
     *         <tr>
     *             <td>1</td>
     *             <td>只需要主分片可用</td>
     *         </tr>
     *         <tr>
     *             <td>all</td>
     *             <td>主分片和所有副本都要可用</td>
     *         </tr>
     *         <tr>
     *             <td>其他正数（0 - number_of_replicas + 1）</td>
     *             <td></td>
     *         </tr>
     *     </tbody>
     * </table>
     *
     * @return 分片数量
     */
    String waitForActiveShards() default "-2";

    /**
     * 链接master节点的超时间
     *
     * @return 超时时间，默认30s
     */
    String masterTimeout() default "30s";

    /**
     * 超时时间，单位(s/m/h/d)
     *
     * @return 时间
     */
    String timeout() default "1m";

    /**
     * 文档版本控制类型
     *
     * @return 版本类型
     */
    VersionType versionType() default VersionType.INTERNAL;

    /**
     * 获取刷新策略
     *
     * @return 刷新策略
     */
    WriteRequest.RefreshPolicy refreshPolicy() default WriteRequest.RefreshPolicy.NONE;
}
