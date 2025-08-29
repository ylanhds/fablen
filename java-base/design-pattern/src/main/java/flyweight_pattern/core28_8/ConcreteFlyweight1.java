package flyweight_pattern.core28_8;

import flyweight_pattern.core28_7.Flyweight;

public class ConcreteFlyweight1 extends Flyweight {

    //接受外部状态
    public ConcreteFlyweight1(String _Extrinsic) {
        super(_Extrinsic);
    }

    //根据外部状态进行逻辑处理
    @Override
    public void operate() {
        //业务逻辑
    }
}
