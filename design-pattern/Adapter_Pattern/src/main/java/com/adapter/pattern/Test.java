package com.adapter.pattern;

/**
 * 适配器: 使得原本不兼容的接口可以一起工作
 */
public class Test {

    public static void main(String[] args) {
        PowerA powerA = new PowerAImpl();
        work(powerA);

        //通过适配器
        PowerB powerB = new PowerBImpl();
        Adapter adapter = new Adapter(powerB);
        work(adapter);// 实现调用同意个方法

    }
    //不做修改
    public static void work(PowerA a){
        System.out.println("正在链接>>>");
        a.insert();
        System.out.println("工作结束");
    }


}

//适配器  把b转换为a
class Adapter implements PowerA{
    private PowerB powerB;
    public Adapter(PowerB powerB){
        this.powerB =powerB;
    }
    @Override
    public void insert() {
        powerB.connet();
    }
}


interface PowerB{
     void connet();
}
class PowerBImpl implements PowerB{
    @Override
    public void connet() {
        System.out.println("电源B开始工作");
    }
}


interface PowerA{
     void insert();
}
class PowerAImpl implements PowerA{
    @Override
    public void insert() {
        System.out.println("电源A开始工作");
    }
}
