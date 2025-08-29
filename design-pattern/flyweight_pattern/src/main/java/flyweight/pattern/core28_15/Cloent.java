package flyweight.pattern.core28_15;

import flyweight.pattern.core28_13.ExtrinsicState;
import flyweight.pattern.core28_14.SignInfoFactory;

public class Cloent {

    public static void main(String[] args) {
        //初始化对象池
        ExtrinsicState state1 = new ExtrinsicState();
        state1.setSubject("科目1");
        state1.setLocation("上海");

        ExtrinsicState state2 = new ExtrinsicState();
        state2.setSubject("科目1");
        state2.setLocation("上海");

        //计算执行100万次需要的时间
        long currentTime = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            SignInfoFactory.getSignInfo(state2);

        }
        long tailTime = System.currentTimeMillis();
        System.out.println("执行时间: " + (tailTime - currentTime) + " ms");
    }

}
