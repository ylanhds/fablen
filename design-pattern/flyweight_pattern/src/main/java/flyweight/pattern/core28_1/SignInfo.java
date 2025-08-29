package flyweight.pattern.core28_1;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignInfo {
    //报名人id
    private String id;
    //考试地点
    private String location;
    //考试科目
    private String subject;
    //邮件地址
    private String postAddress;
}
