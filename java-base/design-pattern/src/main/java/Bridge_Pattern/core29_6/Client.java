package Bridge_Pattern.core29_6;

import Bridge_Pattern.core29_2.HouseCorp;
import Bridge_Pattern.core29_5.IPodCorp;

public class Client {

    public static void main(String[] args) {
        System.out.println("房地产公司");
        HouseCorp houseCorp = new HouseCorp();
        houseCorp.makeMoney();

        System.out.println("\n");

        System.out.println("山寨公司");
        IPodCorp iPodCorp = new IPodCorp();
        iPodCorp.makeMoney();


    }

}
