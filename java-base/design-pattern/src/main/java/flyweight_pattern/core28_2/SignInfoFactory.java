package flyweight_pattern.core28_2;

import flyweight_pattern.core28_1.SignInfo;

/**
 * 报考信息工厂
 */
public class SignInfoFactory {

    //报名信息的对象工厂
    public static SignInfo getSignInfo() {
        return new SignInfo();
    }
}
