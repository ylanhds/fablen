package exceptions;

import java.util.List;

/**
 * 自定义校验异常
 * 保存所有错误信息，便于统一处理
 *
 * @Author：zhangbaosheng
 * @Package：exceptions
 * @Project：validation-lib
 * @name：ValidationException
 * @Date：2025/8/9 13:51
 * @Filename：ValidationException
 */
public class ValidationException extends RuntimeException {
    // 存储所有校验错误信息的列表
    private final List<String> errors;

    /**
     * 构造函数
     *
     * @param errors 校验错误信息列表
     */
    public ValidationException(List<String> errors) {
        // 调用父类构造函数，设置异常信息
        super("字段校验失败");
        // 初始化错误信息列表
        this.errors = errors;
    }

    /**
     * 获取校验错误信息列表
     *
     * @return 包含所有校验错误信息的列表
     */
    public List<String> getErrors() {
        return errors;
    }
}
