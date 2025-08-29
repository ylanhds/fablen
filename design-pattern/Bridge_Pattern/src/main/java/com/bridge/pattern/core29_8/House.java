package com.bridge.pattern.core29_8;

import com.bridge.pattern.core29_7.Product;

public class House extends Product {

    @Override
    public void beProducted() {
        System.out.println("生产的豆腐渣房子");
    }

    @Override
    public void beSelled() {
        System.out.println("豆腐渣房子卖出去了");
    }
}
