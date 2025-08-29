package com.bridge.pattern.core29_13;


import com.bridge.pattern.core29_11.HouseCorp;
import com.bridge.pattern.core29_12.ShanZhaiCorp;
import com.bridge.pattern.core29_8.House;
import com.bridge.pattern.core29_9.IPod;

public class Client {
    public static void main(String[] args) {
        House house = new House();
        System.out.println("房地");
        //
        HouseCorp houseCorp = new HouseCorp(house);
        houseCorp.makeMoney();

        ShanZhaiCorp shanZhaiCorp =  new ShanZhaiCorp(new IPod());
        shanZhaiCorp.makeMoney();


    }
}
