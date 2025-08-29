package flyweight.pattern.core28_14;

import flyweight.pattern.core28_1.SignInfo;
import flyweight.pattern.core28_13.ExtrinsicState;
import java.util.HashMap;

public class SignInfoFactory {

    //池容器
    private static HashMap<ExtrinsicState, SignInfo> pool = new HashMap<>();

    //从池中获的对象
    public static SignInfo getSignInfo(ExtrinsicState key) {
        //设置返回对象
        SignInfo result;
        //池中没有该对象,则建立,并放入池中
        if (!pool.containsKey(key)) {
            result = new SignInfo();
            pool.put(key, result);
        } else {
            result = pool.get(key);
        }
        return result;
    }
}
