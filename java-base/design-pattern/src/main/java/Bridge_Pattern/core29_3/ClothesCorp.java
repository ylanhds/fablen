package Bridge_Pattern.core29_3;

import Bridge_Pattern.core29_1.Corp;

public class ClothesCorp extends Corp {

    @Override
    protected void produce() {
        System.out.println("生产服装");
    }

    @Override
    protected void sell() {
        System.out.println("服装出售");
    }
    public void makeMoney(){
        super.makeMoney();
        System.out.println("赚钱");
    }
}
