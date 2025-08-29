package flyweight.pattern.core28_16;


import flyweight.pattern.core28_10.SignInfoFactory;

public class Client {

    public static void main(String[] args) {
        String key1 = "科目1上海";
        String key2 = "科目1上海";
        //初始化对象池
        SignInfoFactory.getSignInfo(key1);

        //计算执行100万次需要的时间
        long currentTime = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            SignInfoFactory.getSignInfo(key2);

        }
        long tailTime = System.currentTimeMillis();
        System.out.println("执行时间: " + (tailTime - currentTime) + " ms");
    }


}
