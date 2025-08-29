package dependence.inversion.principle;

import dependence.inversion.principle.service.Driver;

public class Client {

    public static void main(String[] args) {
        Driver zhangsan = new Driver();
        Benz benz = new Benz();
        zhangsan.driver(benz);
    }
}
