package com.factory.pattern;

/**
 * 手机产品
 */
public class Phone implements Product {
    @Override
    public void work() {
        System.out.println("手机生产开始工作.......");
    }
}
