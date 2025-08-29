package Single_Responsibility_Principle.service;

/**
 * 负责用户属性用
 *
 * @author zhangbaosheng
 * @date 2:07 PM 12/13/19
 */
public interface IUserBO {

    void setUserID(String userID);

    String getUserID();

    void setPassword(String password);

    String getPassword();

    void setUserName(String userName);

    String getUserName();

}
