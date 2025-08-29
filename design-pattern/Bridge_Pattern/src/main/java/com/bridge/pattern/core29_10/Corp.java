package com.bridge.pattern.core29_10;

import com.bridge.pattern.core29_7.Product;

public  abstract class Corp {
    //定义一个抽象产品
    private Product product;
    //构造函数,由子类传递具体产品进来
    public Corp(Product product){
        this.product=product;
    }
    //赚钱
    public void makeMoney(){
        this.product.beProducted();
        this.product.beSelled();
    }
}
