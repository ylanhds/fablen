package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 金额范围校验注解
 * min/max 单位为元
 * 使用位置：类的字段
 * @Author：zhangbaosheng
 * @Package：annotations
 * @Project：validation-lib
 * @name：Price
 * @Date：2025/8/9 13:50
 * @Filename：Price
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Price {
    /**
     * 金额最小值（单位：元）
     * 默认值为0
     * @return 最小金额
     */
    double min() default 0;

    /**
     * 金额最大值（单位：元）
     * 默认值为Double.MAX_VALUE
     * @return 最大金额
     */
    double max() default Double.MAX_VALUE;

    /**
     * 校验失败时的错误信息
     * 默认值为"价格不在有效范围"
     * @return 错误信息
     */
    String message() default "价格不在有效范围";
}
