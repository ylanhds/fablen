package com.factory.pattern;

/**
 * 电脑产品
 */
public class Computer implements Product {

    @Override
    public void work() {
        System.out.println("电脑生产开始工作.......");
    }
}
