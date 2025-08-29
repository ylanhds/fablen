package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 地址长度校验注解
 * 使用位置：类的字段
 *
 * @Author：zhangbaosheng
 * @Package：annotations
 * @Project：validation-lib
 * @name：Address
 * @Date：2025/8/9 13:51
 * @Filename：Address
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Address {
    /**
     * 地址最小长度
     * 默认值为5
     *
     * @return 最小长度
     */
    int minLength() default 5;

    /**
     * 地址最大长度
     * 默认值为200
     *
     * @return 最大长度
     */
    int maxLength() default 200;

    /**
     * 校验失败时的错误信息
     * 默认值为"地址长度不合法"
     *
     * @return 错误信息
     */
    String message() default "地址长度不合法";
}
