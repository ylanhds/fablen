package com.bridge.pattern.core29_11;

import com.bridge.pattern.core29_10.Corp;
import com.bridge.pattern.core29_8.House;

public class HouseCorp extends Corp {

    //定义一个house产品进来
    public HouseCorp(House house) {
        super(house);
    }
    public void makeMoney(){
        super.makeMoney();
        System.out.println("房地产赚钱");
    }

}
