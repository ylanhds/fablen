package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数量格式校验注解
 * 使用位置：类的字段
 *
 * @Author：zhangbaosheng
 * @Package：annotations
 * @Project：validation-lib
 * @name：Quantity
 * @Date：2025/8/9 13:50
 * @Filename：Quantity
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Quantity {
    /**
     * 数量最小值
     * 默认值为0
     * @return 最小值
     */
    int min() default 0;

    /**
     * 数量最大值
     * 默认值为Integer.MAX_VALUE
     * @return 最大值
     */
    int max() default Integer.MAX_VALUE;

    /**
     * 校验失败时的错误信息
     * 默认值为"数量不在有效范围"
     * @return 错误信息
     */
    String message() default "数量不在有效范围";
}
