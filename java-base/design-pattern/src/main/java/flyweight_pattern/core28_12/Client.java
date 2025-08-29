package flyweight_pattern.core28_12;

import flyweight_pattern.core28_1.SignInfo;
import flyweight_pattern.core28_10.SignInfoFactory;
import flyweight_pattern.core28_11.MultiThread;

public class Client {

    public static void main(String[] args) {
        //在对象池中初始化4个对象
        SignInfoFactory.getSignInfo("科目1");
        SignInfoFactory.getSignInfo("科目2");
        SignInfoFactory.getSignInfo("科目3");
        SignInfoFactory.getSignInfo("科目4");
        //获取对象
        SignInfo signInfo = SignInfoFactory.getSignInfo("科目2");
        while (true){
            signInfo.setId("ZhangSan");
            signInfo.setLocation("ZhangSan");
            (new MultiThread(signInfo)).start();
            signInfo.setId("LiSi");
            signInfo.setLocation("LiSi");
            (new MultiThread(signInfo)).start();
        }
    }

}
