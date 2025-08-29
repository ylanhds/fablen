package Single_Responsibility_Principle.service;
/**
 * 负责用用户行为
 * @author zhangbaosheng
 * @date 2:07 PM 12/13/19
 */
public interface IUserBiz {

    boolean changePassword();

    boolean deleteUser(IUserBO userBO);

    void mapUser(IUserBO userBO);

    boolean addOrg(IUserBO userBO, int orgID);

    boolean addRole(IUserBO userBO, int roleID);
}
