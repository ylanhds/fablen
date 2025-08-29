package Bridge_Pattern.core29_12;

import Bridge_Pattern.core29_10.Corp;
import Bridge_Pattern.core29_7.Product;

public class ShanZhaiCorp extends Corp {
    public ShanZhaiCorp(Product product){
        super(product);
    }
    public void makeMoney(){
        super.makeMoney();
        System.out.println("赚钱");
    }
}
