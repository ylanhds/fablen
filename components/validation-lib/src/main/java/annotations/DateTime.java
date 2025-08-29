// DateTime.java
package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日期/时间格式校验注解
 * 使用位置：类的字段
 *
 * @Author：zhangbaosheng
 * @Package：annotations
 * @Project：validation-lib
 * @name：DateTime
 * @Date：2025/8/9 13:50
 * @Filename：DateTime
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DateTime {
    /**
     * 日期时间格式模式
     * 默认值为"yyyy-MM-dd"
     * 支持常见的日期时间格式，如：
     * - "yyyy-MM-dd" (日期格式)
     * - "yyyy-MM-dd HH:mm:ss" (完整时间格式)
     * - "HH:mm:ss" (时间格式)
     *
     * @return 日期时间格式模式
     */
    String pattern() default "yyyy-MM-dd";

    /**
     * 最小日期时间限制
     * 默认值为空字符串，表示无最小限制
     * 格式需与pattern保持一致
     *
     * @return 最小日期时间字符串
     */
    String min() default "";

    /**
     * 最大日期时间限制
     * 默认值为空字符串，表示无最大限制
     * 格式需与pattern保持一致
     *
     * @return 最大日期时间字符串
     */
    String max() default "";

    /**
     * 校验失败时的错误信息
     * 默认值为"日期/时间格式错误或超出范围"
     *
     * @return 错误信息
     */
    String message() default "日期/时间格式错误或超出范围";
}
