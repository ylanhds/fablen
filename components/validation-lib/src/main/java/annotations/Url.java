// Url.java
package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * URL格式校验注解
 * 使用位置：类的字段
 *
 * @Author：zhangbaosheng
 * @Package：annotations
 * @Project：validation-lib
 * @name：Url
 * @Date：2025/8/9 13:50
 * @Filename：Url
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Url {
    /**
     * 校验失败时的错误信息
     * 默认值为"URL格式不正确"
     * @return 错误信息
     */
    String message() default "URL格式不正确";
}
