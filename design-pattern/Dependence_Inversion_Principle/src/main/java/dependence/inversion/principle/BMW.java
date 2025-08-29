package dependence.inversion.principle;

import dependence.inversion.principle.service.ICar;

public class BMW implements ICar {

    @Override
    public void run(){
        System.out.print("宝马的车");
    }
}
