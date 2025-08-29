// FileConstraint.java
package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 文件校验注解
 * 使用位置：类的字段
 *
 * @Author：zhangbaosheng
 * @Package：annotations
 * @Project：validation-lib
 * @name：FileConstraint
 * @Date：2025/8/9 13:50
 * @Filename：FileConstraint
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FileConstraint {
    /**
     * 文件最大大小限制（KB）
     * 默认值为Long.MAX_VALUE，表示不限制文件大小
     * @return 最大大小（KB）
     */
    long maxSizeKB() default Long.MAX_VALUE;

    /**
     * 允许的文件类型（MIME类型）
     * 默认值为空数组，表示不限制文件类型
     * @return 允许的MIME类型数组
     */
    String[] allowedTypes() default {};

    /**
     * 校验失败时的错误信息
     * 默认值为"文件不符合上传要求"
     * @return 错误信息
     */
    String message() default "文件不符合上传要求";
}
