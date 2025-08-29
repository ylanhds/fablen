package flyweight_pattern.core28_5;

import flyweight_pattern.core28_1.SignInfo;
import flyweight_pattern.core28_4.SignInfo4Pool;
import java.util.HashMap;

public class SignInfoFactory {
    //池容器
    private static HashMap<String, SignInfo> pool = new HashMap<>();
    //报名信息的对象工厂
    @Deprecated
    public static SignInfo getSignInfo() {
        return new SignInfo();
    }

    public static SignInfo getSignInfo(String key){
        SignInfo result;
        //池中没有改对象,则建立,并翻入池中
        if (!pool.containsKey(key)){
            System.out.println(key+"-----建立对象,并放置到池中");
            result = new SignInfo4Pool(key);
            pool.put(key,result);
        }else {
            result = pool.get(key);
            System.out.println(key+"-----直接从池中取得");
        }
        return result;
    }

}
