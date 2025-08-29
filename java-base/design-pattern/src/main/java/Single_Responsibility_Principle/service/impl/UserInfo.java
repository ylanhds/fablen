package Single_Responsibility_Principle.service.impl;


import Single_Responsibility_Principle.service.IUserBO;
import Single_Responsibility_Principle.service.IUserInfo;

public class UserInfo implements IUserInfo {

    @Override
    public void setUserID(String userID) {

    }

    @Override
    public String getUserID() {
        return null;
    }

    @Override
    public void setPassword(String password) {

    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public void setUserName(String userName) {

    }

    @Override
    public String getUserName() {
        return null;
    }

    @Override
    public boolean changePassword() {
        return false;
    }

    @Override
    public boolean deleteUser(IUserBO userBO) {
        return false;
    }

    @Override
    public void mapUser(IUserBO userBO) {

    }

    @Override
    public boolean addOrg(IUserBO userBO, int orgID) {
        return false;
    }

    @Override
    public boolean addRole(IUserBO userBO, int roleID) {
        return false;
    }
}
