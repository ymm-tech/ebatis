package io.manbang.ebatis.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字段描述映射
 *
 * @author duoliang.zhang
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface Field {
    /**
     * 字段名称，如果es中存储的字段名成与实体的属性名称不一致的话，可以用这个属性来指定映射关系
     *
     * @return 字段名
     */
    String value() default "";

    /**
     * {@link #value()} 别名
     *
     * @return 字段名称
     */
    String name() default "";

    /**
     * 是否忽略空值；当作为es的查询条件时，如果属性为空指针，是否忽略此属性作为查询条件，默认是忽略
     *
     * @return 忽略，返回<code>true</code>，否则，返回<code>false</code>
     */
    boolean ignoreNull() default true;
}
