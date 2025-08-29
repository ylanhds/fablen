package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数值精度（整数位/小数位）校验注解
 * 使用位置：类的字段
 * @Author：zhangbaosheng
 * @Package：annotations
 * @Project：validation-lib
 * @name：Decimal
 * @Date：2025/8/9 13:50
 * @Filename：Decimal
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Decimal {
    /**
     * 整数部分最大位数
     * 默认值为Integer.MAX_VALUE，表示不限制整数位数
     * @return 整数部分最大位数
     */
    int integerPart() default Integer.MAX_VALUE;

    /**
     * 小数部分最大位数
     * 默认值为Integer.MAX_VALUE，表示不限制小数位数
     * @return 小数部分最大位数
     */
    int fractionPart() default Integer.MAX_VALUE;

    /**
     * 校验失败时的错误信息
     * 默认值为"数值精度不符合要求"
     * @return 错误信息
     */
    String message() default "数值精度不符合要求";
}
