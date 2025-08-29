package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 姓名长度校验注解
 * 使用位置：类的字段
 *
 * @Author：zhangbaosheng
 * @Package：annotations
 * @Project：validation-lib
 * @name：Name
 * @Date：2025/8/9 13:50
 * @Filename：Name
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Name {
    /**
     * 姓名最小长度
     * 默认值为1
     * @return 最小长度
     */
    int minLength() default 1;

    /**
     * 姓名最大长度
     * 默认值为50
     * @return 最大长度
     */
    int maxLength() default 50;

    /**
     * 校验失败时的错误信息
     * 默认值为"姓名长度不合法"
     * @return 错误信息
     */
    String message() default "姓名长度不合法";
}
