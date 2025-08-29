package flyweight_pattern.core28_4;

import flyweight_pattern.core28_1.SignInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SignInfo4Pool extends SignInfo {

    //定义一个对象池提取key值
    private String key;
}
