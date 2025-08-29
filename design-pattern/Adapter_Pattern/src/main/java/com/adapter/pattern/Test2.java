package com.adapter.pattern;

public class Test2 {

}

interface Animal{
    void sing();
    void cry();
    void run();
    void swim();
}
//场景一:要实现所有方法
//class Dog implements Animal{
//
//    @Override
//    public void sing() {
//
//    }
//
//    @Override
//    public void cry() {
//
//    }
//
//    @Override
//    public void run() {
//        System.out.println("跑的开");
//    }
//
//    @Override
//    public void swim() {
//
//    }
//}

//加上适配器
abstract class AnimalBio{
    void sing(){};
    void cry(){};
    void run(){};
    void swim(){};
}
class Dog extends AnimalBio{
    public void run(){
        System.out.println("跑的开");
    }
}
