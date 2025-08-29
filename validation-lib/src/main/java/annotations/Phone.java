package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 手机号码格式校验注解
 * 使用位置：类的字段
 *
 * @Author：zhangbaosheng
 * @Package：PACKAGE_NAME
 * @Project：validation-lib
 * @name：Phone
 * @Date：2025/8/9 13:48
 * @Filename：Phone
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Phone {
    /**
     * 校验失败时的错误信息
     * 默认值为"手机号格式不正确"
     * @return 错误信息
     */
    String message() default "手机号格式不正确";
}
