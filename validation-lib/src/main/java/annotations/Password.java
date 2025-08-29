// Password.java
package annotations;

import java.lang.annotation.*;

/**
 * 密码复杂度校验注解
 * 使用位置：类的字段
 *
 * @Author：zhangbaosheng
 * @Package：annotations
 * @Project：validation-lib
 * @name：Password
 * @Date：2025/8/9 13:50
 * @Filename：Password
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Password {
    /**
     * 是否需要包含大写字母
     * 默认值为true
     * @return 是否需要大写字母
     */
    boolean requireUppercase() default true;

    /**
     * 是否需要包含小写字母
     * 默认值为true
     * @return 是否需要小写字母
     */
    boolean requireLowercase() default true;

    /**
     * 是否需要包含数字
     * 默认值为true
     * @return 是否需要数字
     */
    boolean requireDigit() default true;

    /**
     * 是否需要包含特殊字符
     * 默认值为true
     * @return 是否需要特殊字符
     */
    boolean requireSpecial() default true;

    /**
     * 密码最小长度
     * 默认值为8
     * @return 最小长度
     */
    int minLength() default 8;

    /**
     * 密码最大长度
     * 默认值为64
     * @return 最大长度
     */
    int maxLength() default 64;

    /**
     * 校验失败时的错误信息
     * 默认值为"密码复杂度不符合要求"
     * @return 错误信息
     */
    String message() default "密码复杂度不符合要求";
}
