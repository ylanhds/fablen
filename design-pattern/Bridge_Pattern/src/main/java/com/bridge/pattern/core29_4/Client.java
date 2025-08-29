package com.bridge.pattern.core29_4;

import com.bridge.pattern.core29_2.HouseCorp;
import com.bridge.pattern.core29_3.ClothesCorp;

public class Client {

    public static void main(String[] args) {
        System.out.println("房地产公司");
        //
        HouseCorp houseCorp = new HouseCorp();
        houseCorp.makeMoney();
        System.out.println("\n");
        System.out.println("服装公司");
        ClothesCorp clothesCorp = new ClothesCorp();
        clothesCorp.makeMoney();
    }

}
