# Validation Lib

一个基于Java注解的数据校验框架，提供了多种常用的数据校验功能。

```plantuml
validation-lib/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/
    │   │   ├── annotations/     // 所有校验注解
    │   │   ├── engine/          // 校验引擎
    │   │   ├── exceptions/      // 异常类
    │   │   └── utils/           // 工具方法
    │   └── resources/
    └── test/
        └── java/
            └── ValidationTest.java
```

## 项目概述

这是一个轻量级的数据验证库，通过在类字段上添加相应的注解，可以实现对数据的自动校验。该项目旨在提供一个简单易用、可扩展的验证解决方案。

## 功能特性

### 基础数据校验

- [@Name](file://D:\project\IdeaProjects\validation-lib\src\main\java\annotations\Name.java#L18-L26):
  姓名长度校验（支持最小/最大长度设置）
- [@Email](file://D:\project\IdeaProjects\validation-lib\src\main\java\annotations\Email.java#L20-L24): 邮箱格式校验
- [@Phone](file://D:\project\IdeaProjects\validation-lib\src\main\java\annotations\Phone.java#L18-L22): 手机号码格式校验
- [@IdCard](file://D:\project\IdeaProjects\validation-lib\src\main\java\annotations\IdCard.java#L17-L21): 身份证号码校验
- [@PostalCode](file://D:\project\IdeaProjects\validation-lib\src\main\java\annotations\PostalCode.java#L18-L22): 邮政编码校验
- [@Address](file://D:\project\IdeaProjects\validation-lib\src\main\java\annotations\Address.java#L17-L23):
  地址长度校验（支持最小/最大长度设置）

### 业务数据校验

- [@Price](file://D:\project\IdeaProjects\validation-lib\src\main\java\annotations\Price.java#L18-L24):
  价格范围校验（支持最小/最大值设置）
- [@Quantity](file://D:\project\IdeaProjects\validation-lib\src\main\java\annotations\Quantity.java#L18-L26):
  数量范围校验（支持最小/最大值设置）

### 日期时间校验

- [@DateTime](file://D:\project\IdeaProjects\validation-lib\src\main\java\annotations\DateTime.java#L19-L29):
  日期时间格式校验，支持：
    - 自定义日期时间格式（如：yyyy-MM-dd HH:mm:ss）
    - 日期范围限制（最小/最大日期）
    - 时分秒校验

### 文件数据支持

- [FileData](file://D:\project\IdeaProjects\validation-lib\src\main\java\engine\FileData.java#L12-L47):
  文件数据封装类，支持文件内容和MIME类型存储

## 安装与使用

### 添加依赖

将项目编译为jar包并在您的项目中引入。

### 基本使用方法

1. 在需要校验的类字段上添加相应注解：

```java
public class UserDTO {
    @Name(minLength = 2, maxLength = 20)
    private String name;
    @Email
    private String email;

    @Phone
    private String phone;

    @Price(min = 0.01, max = 9999.99)
    private BigDecimal price;

    @DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;

// getters and setters...
}
```

2. 调用验证引擎进行校验：在spring中使用的方式

```java
//方式1：手动调用
@Service
public class UserService {

    public void createUser(UserDTO user) {
        try {
            ValidatorEngine.validate(user);
            // 处理业务逻辑
        } catch (ValidationException e) {
            // 处理验证异常
            throw new IllegalArgumentException(e.getErrors().toString());
        }
    }
}
```



```java
//方式2：通过AOP实现自动验证：
@Aspect
@Component
public class ValidationAspect {

    @Around("@annotation(Validated)")
    public Object validate(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg != null) {
                ValidatorEngine.validate(arg);
            }
        }
        return joinPoint.proceed();
    }
}

@RestController
public class UserController {

    @PostMapping("/users")
    @Validated
    public ResponseEntity<String> createUser(@RequestBody UserDTO user) {
            // 参数会自动验证
        return ResponseEntity.ok("User created");
    }
}
```

## 扩展性

框架采用注解+引擎的设计模式，易于扩展新的校验规则：

1. 创建新的校验注解
2. 在验证引擎中添加相应的校验逻辑
3. 重新编译并使用

## 示例

项目中包含完整的测试示例 [ValidationTest.java](file://D:\project\IdeaProjects\validation-lib\src\test\java\ValidationTest.java)
，展示了所有校验注解的使用方法。

## 许可证

[待添加具体许可证信息]

## 贡献

欢迎提交Issue和Pull Request来改进这个项目。


