package single.responsibility.principle;

import single.responsibility.principle.service.IUserBO;
import single.responsibility.principle.service.IUserBiz;
import single.responsibility.principle.service.impl.UserInfo;
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
