package Bridge_Pattern.core29_9;

import Bridge_Pattern.core29_7.Product;

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
