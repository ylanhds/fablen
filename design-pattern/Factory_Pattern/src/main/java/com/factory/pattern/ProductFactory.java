package com.factory.pattern;

/**
 * 工厂方法
 */
public class ProductFactory {

    //返回的是生产的产品
    public static Product getProduct(String name) {
        if ("phone".equals(name)) {
            return new Phone();
        } else if ("computer".equals(name)) {
            return new Computer();
        }
        return null;

    }

}
