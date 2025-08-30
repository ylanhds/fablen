package com.fablen.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;

@TableName("product")  // 指定数据库表名
public class Product implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)  // 主键注解
    private Long id;

    @TableField("name")  // 字段映射
    private String name;

    // 逻辑删除字段（可选）
    @TableLogic
    @TableField("deleted")
    private Integer deleted;

    // 乐观锁版本字段（可选）
    @Version
    @TableField("version")
    private Integer version;

    // 构造函数
    public Product() {}

    // Getter 和 Setter 方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
