package io.manbang.ebatis.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 查询删除注解
 *
 * @author 章多亮
 * @since 2019/12/28 14:50:02
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DeleteByQuery {
    int maxDocs() default -1;

    int batchSize() default 1000;

    String conflicts() default "abort";

    int slices() default 1;

    String timeout() default "1m";

    boolean refresh() default false;

    int maxRetries() default 11;

    String waitForActiveShards() default "-2";

    boolean shouldStoreResult() default false;

    /**
     * @return 毫秒
     */
    String scrollKeepAlive() default "0";

    float requestsPerSecond() default Float.POSITIVE_INFINITY;

    /**
     * 第一次重试尝试之前需要等待的初始时间
     * <table>
     *     <thead>
     *     <tr>
     *               <th>单位</th>
     *               <th>描述</th>
     *           </tr>
     *     </thead>
     *     <tbody>
     *         <tr>
     *             <td>nanos</td>
     *             <td>纳秒</td>
     *         </tr>
     *         <tr>
     *             <td>micros</td>
     *             <td>微秒</td>
     *         </tr>
     *         <tr>
     *             <td>ms</td>
     *             <td>毫秒</td>
     *         </tr>
     *         <tr>
     *             <td>s</td>
     *             <td>秒</td>
     *         </tr>
     *         <tr>
     *              <td>m</td>
     *              <td>分</td>
     *          </tr>
     *          <tr>
     *              <td>h</td>
     *              <td>小时</td>
     *           </tr>
     *           <tr>
     *               <td>d</td>
     *               <td>天</td>
     *           </tr>
     *     </tbody>
     * </table>
     *
     * @return 等待时间
     */
    String retryBackoffInitialTime() default "500ms";
}
