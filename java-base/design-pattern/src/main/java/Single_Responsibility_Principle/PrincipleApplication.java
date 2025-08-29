package Single_Responsibility_Principle;

import Single_Responsibility_Principle.service.IUserBO;
import Single_Responsibility_Principle.service.IUserBiz;
import Single_Responsibility_Principle.service.impl.UserInfo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PrincipleApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrincipleApplication.class, args);
    }

    private void Test(){
        IUserBiz userInfo = new UserInfo();

        IUserBO userBO = (IUserBO)userInfo;
        userBO.setPassword("abc");

        IUserBiz userBiz = userInfo;
        userBiz.changePassword();

    }


}
