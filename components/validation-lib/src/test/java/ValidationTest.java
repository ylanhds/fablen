import annotations.*;
import engine.FileData;
import engine.ValidatorEngine;
import exceptions.ValidationException;

import java.math.BigDecimal;
import java.util.Arrays;

public class ValidationTest {

    enum Gender {MALE, FEMALE}

    static class UserDTO {
        @Email(message = "邮箱格式不正确")
        private String email;

        @Phone(message = "手机号格式不正确")
        private String phone;

        @IdCard(message = "身份证号码格式不正确")
        private String idCard;

        @Price(min = 0.01, max = 999999.99, message = "价格必须在0.01-999999.99之间")
        private BigDecimal price;

        @Quantity(min = 1, max = 100, message = "数量必须在1-100之间")
        private Integer qty;

        @PostalCode(message = "邮政编码格式不正确")
        private String post;

        @Name(minLength = 2, maxLength = 10, message = "姓名长度必须在2-10个字符之间")
        private String name;

        @Address(minLength = 5, maxLength = 50, message = "地址长度必须在5-50个字符之间")
        private String address;

        @Decimal(integerPart = 5, fractionPart = 2, message = "数值最多5位整数和2位小数")
        private BigDecimal numberField;

        @DateTime(pattern = "yyyy-MM-dd", min = "2020-01-01", max = "2030-12-31",
                message = "日期必须在2020-01-01到2030-12-31之间")
        private String regDate;

        @Url(message = "URL格式不正确")
        private String website;

        @FileConstraint(maxSizeKB = 10, allowedTypes = {"image/png"},
                message = "文件必须是PNG格式且大小不超过10KB")
        private FileData avatar;

        @Password(minLength = 8, requireSpecial = false,
                message = "密码至少8位，需包含大小写字母和数字")
        private String password;

        @EnumValue(enumClass = Gender.class, message = "性别值必须是MALE或FEMALE")
        private Gender gender;

        @DateTime(pattern = "yyyy-MM-dd HH:mm:ss", message = "创建时间格式不正确")
        private String createTime;

        // Getters and Setters
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public Integer getQty() {
            return qty;
        }

        public void setQty(Integer qty) {
            this.qty = qty;
        }

        public String getPost() {
            return post;
        }

        public void setPost(String post) {
            this.post = post;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public BigDecimal getNumberField() {
            return numberField;
        }

        public void setNumberField(BigDecimal numberField) {
            this.numberField = numberField;
        }

        public String getRegDate() {
            return regDate;
        }

        public void setRegDate(String regDate) {
            this.regDate = regDate;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public FileData getAvatar() {
            return avatar;
        }

        public void setAvatar(FileData avatar) {
            this.avatar = avatar;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Gender getGender() {
            return gender;
        }

        public void setGender(Gender gender) {
            this.gender = gender;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== 验证框架完整测试 ===\n");

        // 测试1: 正常情况测试
        System.out.println("1. 正常情况测试:");
        testValidData();

        // 测试2: 异常情况测试
        System.out.println("\n2. 异常情况测试:");
        testInvalidData();

        // 测试3: 边界值测试
        System.out.println("\n3. 边界值测试:");
        testBoundaryValues();
    }

    /**
     * 测试所有数据都符合要求的情况
     */
    private static void testValidData() {
        UserDTO user = new UserDTO();

        // 设置所有有效数据
        user.setEmail("test@example.com");
        user.setPhone("13812345678");
        user.setIdCard("110101199003072118");
        user.setPrice(new BigDecimal("999.99"));
        user.setQty(50);
        user.setPost("100000");
        user.setName("张三");
        user.setAddress("北京市朝阳区某某街道123号");
        user.setNumberField(new BigDecimal("12345.67"));
        user.setRegDate("2025-06-15");
        user.setCreateTime("2025-06-15 14:30:25");
        user.setWebsite("https://www.example.com");
        user.setAvatar(new FileData(new byte[5 * 1024], "image/png")); // 5KB PNG文件
        user.setPassword("MyPass123");
        user.setGender(Gender.MALE);

        try {
            ValidatorEngine.validate(user);
            System.out.println("  ✓ 所有数据验证通过");
        } catch (ValidationException e) {
            System.out.println("  ✗ 验证失败:");
            e.getErrors().forEach(error -> System.out.println("    " + error));
        }
    }

    /**
     * 测试包含无效数据的情况
     */
    private static void testInvalidData() {
        UserDTO user = new UserDTO();

        // 设置无效数据
        user.setEmail("invalid-email");
        user.setPhone("12345");
        user.setIdCard("12345");
        user.setPrice(new BigDecimal("-10.00"));
        user.setQty(150);
        user.setPost("1234");
        user.setName("A");
        user.setAddress("短");
        user.setNumberField(new BigDecimal("1234567.8901"));
        user.setRegDate("2019-01-01");
        user.setCreateTime("invalid-datetime");
        user.setWebsite("not-a-url");
        user.setAvatar(new FileData(new byte[15 * 1024], "image/jpeg")); // 15KB JPEG文件
        user.setPassword("123");
        user.setGender(null);

        try {
            ValidatorEngine.validate(user);
            System.out.println("  ✗ 应该验证失败但没有失败");
        } catch (ValidationException e) {
            System.out.println("  ✓ 正确捕获到验证错误，错误数量: " + e.getErrors().size());
            System.out.println("    错误详情:");
            e.getErrors().forEach(error -> System.out.println("      - " + error));
        }
    }

    /**
     * 测试边界值情况
     */
    private static void testBoundaryValues() {
        System.out.println("  3.1 测试价格边界值:");
        testPriceBoundary();

        System.out.println("  3.2 测试数量边界值:");
        testQuantityBoundary();

        System.out.println("  3.3 测试名称长度边界值:");
        testNameLengthBoundary();
    }

    /**
     * 测试价格边界值
     */
    private static void testPriceBoundary() {
        UserDTO user = createBasicValidUser();
        user.setPrice(new BigDecimal("0.00")); // 边界值，小于最小值0.01

        try {
            ValidatorEngine.validate(user);
            System.out.println("    ✗ 价格边界值测试失败");
        } catch (ValidationException e) {
            System.out.println("    ✓ 正确识别价格边界值错误");
        }
    }

    /**
     * 测试数量边界值
     */
    private static void testQuantityBoundary() {
        UserDTO user = createBasicValidUser();
        user.setQty(101); // 边界值，大于最大值100

        try {
            ValidatorEngine.validate(user);
            System.out.println("    ✗ 数量边界值测试失败");
        } catch (ValidationException e) {
            System.out.println("    ✓ 正确识别数量边界值错误");
        }
    }

    /**
     * 测试名称长度边界值
     */
    private static void testNameLengthBoundary() {
        UserDTO user = createBasicValidUser();
        user.setName("A"); // 长度为1，小于最小长度2

        try {
            ValidatorEngine.validate(user);
            System.out.println("    ✗ 名称长度边界值测试失败");
        } catch (ValidationException e) {
            System.out.println("    ✓ 正确识别名称长度边界值错误");
        }
    }

    /**
     * 创建一个基本的有效用户对象
     * @return 基本有效的UserDTO对象
     */
    private static UserDTO createBasicValidUser() {
        UserDTO user = new UserDTO();
        user.setEmail("test@example.com");
        user.setPhone("13812345678");
        user.setIdCard("110101199003072118");
        user.setPrice(new BigDecimal("999.99"));
        user.setQty(50);
        user.setPost("100000");
        user.setName("张三");
        user.setAddress("北京市朝阳区某某街道123号");
        user.setNumberField(new BigDecimal("12345.67"));
        user.setRegDate("2025-06-15");
        user.setCreateTime("2025-06-15 14:30:25");
        user.setWebsite("https://www.example.com");
        user.setAvatar(new FileData(new byte[5 * 1024], "image/png"));
        user.setPassword("MyPass123");
        user.setGender(Gender.MALE);
        return user;
    }
}
