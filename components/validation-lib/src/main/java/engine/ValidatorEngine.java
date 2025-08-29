package engine;

import annotations.*;
import exceptions.ValidationException;
import utils.FieldValidator;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 校验引擎
 * 通过反射扫描对象字段及自定义注解，并调用对应的工具方法进行校验
 *
 * @Author：zhangbaosheng
 * @Package：engine
 * @Project：validation-lib
 * @name：ValidatorEngine
 * @Date：2025/8/9 13:51
 * @Filename：ValidatorEngine
 */
public class ValidatorEngine {

    /**
     * 对目标对象进行校验
     * 通过反射机制遍历对象的所有字段，检查字段上的注解并执行相应的校验逻辑
     *
     * @param target 需要校验的对象
     * @throws ValidationException 当校验失败时抛出，包含所有校验错误信息
     */
    public static void validate(Object target) {
        // 存储所有校验错误信息
        List<String> errors = new ArrayList<>();

        // 遍历对象所有声明的字段（包括私有字段）
        for (Field field : target.getClass().getDeclaredFields()) {
            // 设置字段可访问，以便获取私有字段的值
            field.setAccessible(true);
            Object value;
            try {
                // 获取字段的当前值
                value = field.get(target);
            } catch (IllegalAccessException e) {
                // 如果无法访问字段，跳过该字段的校验
                continue;
            }

            // 邮箱校验 - 检查字段是否有@Email注解
            if (field.isAnnotationPresent(Email.class)) {
                // 调用工具类方法进行邮箱格式校验
                if (!FieldValidator.isValidEmail((String) value)) {
                    // 校验失败，添加错误信息
                    errors.add(field.getAnnotation(Email.class).message());
                }
            }

            // 手机号校验 - 检查字段是否有@Phone注解
            if (field.isAnnotationPresent(Phone.class)) {
                // 调用工具类方法进行手机号格式校验
                if (!FieldValidator.isValidPhone((String) value)) {
                    // 校验失败，添加错误信息
                    errors.add(field.getAnnotation(Phone.class).message());
                }
            }

            // 身份证号码校验 - 检查字段是否有@IdCard注解
            if (field.isAnnotationPresent(IdCard.class)) {
                // 调用工具类方法进行身份证号码格式校验
                if (!FieldValidator.isValidIdCard((String) value)) {
                    // 校验失败，添加错误信息
                    errors.add(field.getAnnotation(IdCard.class).message());
                }
            }

            // 金额范围校验 - 检查字段是否有@Price注解
            if (field.isAnnotationPresent(Price.class)) {
                // 获取注解配置信息
                Price conf = field.getAnnotation(Price.class);
                // 调用工具类方法进行金额范围校验
                if (!FieldValidator.isValidPrice((BigDecimal) value, conf.min(), conf.max())) {
                    // 校验失败，添加错误信息
                    errors.add(conf.message());
                }
            }

            // 数量范围校验 - 检查字段是否有@Quantity注解
            if (field.isAnnotationPresent(Quantity.class)) {
                // 获取注解配置信息
                Quantity conf = field.getAnnotation(Quantity.class);
                // 调用工具类方法进行数量范围校验
                if (!FieldValidator.isValidQuantity((Integer) value, conf.min(), conf.max())) {
                    // 校验失败，添加错误信息
                    errors.add(conf.message());
                }
            }

            // 邮政编码校验 - 检查字段是否有@PostalCode注解
            if (field.isAnnotationPresent(PostalCode.class) &&
                    !FieldValidator.isValidPostalCode((String) value)) {
                // 调用工具类方法进行邮政编码格式校验
                errors.add(field.getAnnotation(PostalCode.class).message());
            }

            // 姓名长度校验 - 检查字段是否有@Name注解
            if (field.isAnnotationPresent(Name.class)) {
                // 获取注解配置信息
                Name conf = field.getAnnotation(Name.class);
                // 调用工具类方法进行姓名长度校验
                if (!FieldValidator.isValidName((String) value, conf.minLength(), conf.maxLength())) {
                    // 校验失败，添加错误信息
                    errors.add(conf.message());
                }
            }

            // 地址长度校验 - 检查字段是否有@Address注解
            if (field.isAnnotationPresent(Address.class)) {
                // 获取注解配置信息
                Address conf = field.getAnnotation(Address.class);
                // 调用工具类方法进行地址长度校验
                if (!FieldValidator.isValidAddress((String) value, conf.minLength(), conf.maxLength())) {
                    // 校验失败，添加错误信息
                    errors.add(conf.message());
                }
            }

            // 数值精度校验 - 检查字段是否有@Decimal注解
            if (field.isAnnotationPresent(Decimal.class)) {
                // 获取注解配置信息
                Decimal conf = field.getAnnotation(Decimal.class);
                // 调用工具类方法进行数值精度校验
                if (!FieldValidator.isValidDecimal((BigDecimal) value, conf.integerPart(), conf.fractionPart())) {
                    // 校验失败，添加错误信息
                    errors.add(conf.message());
                }
            }

            // 日期时间校验 - 检查字段是否有@DateTime注解
            if (field.isAnnotationPresent(DateTime.class)) {
                // 获取注解配置信息
                DateTime conf = field.getAnnotation(DateTime.class);
                // 调用工具类方法进行日期时间格式和范围校验
                if (!FieldValidator.isValidDateTime((String) value, conf.pattern(), conf.min(), conf.max())) {
                    // 校验失败，添加错误信息
                    errors.add(conf.message());
                }
            }

            // URL格式校验 - 检查字段是否有@Url注解
            if (field.isAnnotationPresent(Url.class) &&
                    !FieldValidator.isValidUrl((String) value)) {
                // 调用工具类方法进行URL格式校验
                errors.add(field.getAnnotation(Url.class).message());
            }

            // 文件校验 - 检查字段是否有@FileConstraint注解
            if (field.isAnnotationPresent(FileConstraint.class)) {
                // 获取注解配置信息
                FileConstraint conf = field.getAnnotation(FileConstraint.class);
                // 检查字段值是否为FileData类型
                if (value instanceof FileData) {
                    // 转换为FileData对象
                    FileData fd = (FileData) value;
                    // 调用工具类方法进行文件校验（大小和类型）
                    if (!FieldValidator.isValidFile(fd.getContent(), conf.maxSizeKB(), conf.allowedTypes(), fd.getMimeType())) {
                        // 校验失败，添加错误信息
                        errors.add(conf.message());
                    }
                } else {
                    // 字段值不是FileData类型，添加错误信息
                    errors.add(conf.message());
                }
            }

            // 密码复杂度校验 - 检查字段是否有@Password注解
            if (field.isAnnotationPresent(Password.class)) {
                // 获取注解配置信息
                Password conf = field.getAnnotation(Password.class);
                // 调用工具类方法进行密码复杂度校验
                if (!FieldValidator.isValidPassword(
                        (String) value,
                        conf.requireUppercase(),
                        conf.requireLowercase(),
                        conf.requireDigit(),
                        conf.requireSpecial(),
                        conf.minLength(),
                        conf.maxLength())) {
                    // 校验失败，添加错误信息
                    errors.add(conf.message());
                }
            }

            // 枚举值校验 - 检查字段是否有@EnumValue注解
            if (field.isAnnotationPresent(EnumValue.class)) {
                // 获取注解配置信息
                EnumValue conf = field.getAnnotation(EnumValue.class);
                // 调用工具类方法进行枚举值校验
                if (!FieldValidator.isValidEnumValue(value, conf.enumClass())) {
                    // 校验失败，添加错误信息
                    errors.add(conf.message());
                }
            }
        }

        // 如果存在校验错误，则抛出异常
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
