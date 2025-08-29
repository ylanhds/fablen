package com.factory.pattern;

public class Test {

    public static void main(String[] args) {
        //生产产品 耦合,依赖太强.如果手机类变化,会导致使用类受到影响
        Product phone = new Phone();
        phone.work();

        //加入工厂方法后生产产品 ProductFactory   转移依赖(降低了和被使用者的依赖关系)
        Product phone1 = ProductFactory.getProduct("phone");
        if (phone1!=null){
            phone1.work();
        }

    }
}
