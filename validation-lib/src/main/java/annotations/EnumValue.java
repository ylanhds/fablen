// EnumValue.java
package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 枚举定义范围校验注解
 * 使用位置：类的字段
 *
 * @Author：zhangbaosheng
 * @Package：annotations
 * @Project：validation-lib
 * @name：EnumValue
 * @Date：2025/8/9 13:50
 * @Filename：EnumValue
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EnumValue {
    /**
     * 指定枚举类
     * 必须指定一个继承自Enum的类，用于校验字段值是否在枚举范围内
     * @return 枚举类的Class对象
     */
    Class<? extends Enum<?>> enumClass();

    /**
     * 校验失败时的错误信息
     * 默认值为"值不在枚举定义范围内"
     * @return 错误信息
     */
    String message() default "值不在枚举定义范围内";
}
