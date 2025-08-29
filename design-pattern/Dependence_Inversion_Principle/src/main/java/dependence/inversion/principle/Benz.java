package dependence.inversion.principle;

import dependence.inversion.principle.service.ICar;

public class Benz implements ICar {


    @Override
    public void run() {
        System.out.print("奔驰的车");
    }
}
