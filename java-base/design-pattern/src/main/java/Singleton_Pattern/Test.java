package Singleton_Pattern;

import java.io.Serializable;

/**
 * 程序比较low,主要是为了清楚概念,入门.后续写了个比较完整的
 * <p>
 * 单例模式 构造方法私有化 声明一个本类对象 给外部提供一个静态方法获取对象实例
 * <p>
 * 饿汉式:在类被加载后,对象被创建,知道程序结束后释放对象 懒汉式:在第一次调用方法getInstance ,对象被创建,知道程序结束
 */
public class Test {

    public static void main(String[] args) {
        Singleton1 s = Singleton1.getInstance();
        s.print();

        Singleton2 s2 = Singleton2.getInstance();
        s2.print();
    }
}

//饿汉式(占用内存时间长,提高效率):在类被加载后,对象被创建,知道程序结束后释放对象
class Singleton1 {

    //构造方法私有化
    private Singleton1() {
    }

    //静态的只存一份
    private static Singleton1 s = new Singleton1();

    public static Singleton1 getInstance() {
        return s;
    }

    public void print() {
        System.out.println("测试方法");
    }
}

//懒汉式(占用内存时间短,效率低):在第一次调用方法getInstance ,对象被创建,知道程序结束
class Singleton2 {

    //构造方法私有化
    private Singleton2() {
    }

    //声明引用
    private static Singleton2 s;

    public static Singleton2 getInstance() {
        if (s == null) {
            s = new Singleton2();
        }
        return s;
    }

    public void print() {
        System.out.println("测试方法");
    }
}


//优化 在多线程访问中
class Singleton3 implements Serializable {

    private volatile static Singleton3 singleton3 = null;

    //构造方法私有化
    private Singleton3() {
        if (singleton3 != null) {
            throw new RuntimeException("此类对象为单例模式,已经被实例化");
        }

    }

    public static Singleton3 getInstance() {
        if (singleton3 == null) {
            synchronized (Singleton3.class) {
                if (singleton3 == null) {
                    singleton3 = new Singleton3();
                }
            }
        }
        return singleton3;
    }
}