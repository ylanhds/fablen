package flyweight.pattern.core28_3;

import flyweight.pattern.core28_1.SignInfo;
import flyweight.pattern.core28_2.SignInfoFactory;

public class Client {

    public static void main(String[] args) {
        //从工厂中获取得到一个对象
        SignInfo signInfo = SignInfoFactory.getSignInfo();
        //进行其他业务处理
    }

}
