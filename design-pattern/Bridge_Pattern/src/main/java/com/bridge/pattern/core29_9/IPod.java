package com.bridge.pattern.core29_9;

import com.bridge.pattern.core29_7.Product;

public class IPod extends Product {

    @Override
    public void beProducted() {
        System.out.println("IPod");
    }

    @Override
    public void beSelled() {
        System.out.println("IPod卖出去了");
    }
}
