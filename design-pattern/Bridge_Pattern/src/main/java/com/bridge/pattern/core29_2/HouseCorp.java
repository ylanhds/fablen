package com.bridge.pattern.core29_2;

import com.bridge.pattern.core29_1.Corp;

public class HouseCorp extends Corp {

    //房地产盖房子
    @Override
    protected void produce() {
        System.out.println("房地产盖房子");
    }

    //房地产卖房子
    @Override
    protected void sell() {
        System.out.println("房地产卖房子");
    }
    //赚钱计算利润
    public void makeMoney(){
        super.makeMoney();
        System.out.println("赚钱了");
    }
}
