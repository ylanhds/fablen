package Bridge_Pattern.core29_5;

import Bridge_Pattern.core29_1.Corp;

public class IPodCorp extends Corp {

    @Override
    protected void produce() {
        System.out.println("生产ipod");
    }

    @Override
    protected void sell() {
        System.out.println("销售ipod");
    }
    public void makeMoney(){
        super.makeMoney();;
        System.out.println("赚钱");
    }
}
