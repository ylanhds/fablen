package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 邮箱格式校验注解
 * 使用位置：类的字段
 *
 * @Author：zhangbaosheng
 * @Package：annotations
 * @Project：validation-lib
 * @name：Email
 * @Date：2025/8/9 13:50
 * @Filename：Email
 */


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Email {
    /**
     * 校验失败时的错误信息
     * 默认值为"邮箱格式不正确"
     * @return 错误信息
     */
    String message() default "邮箱格式不正确";
}
