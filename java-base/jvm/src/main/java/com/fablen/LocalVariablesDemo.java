package com.fablen;

import java.util.Date;

/**
 * <p>
 * Description:
 * </p>
 *
 * @author zhangbaosheng
 * @version v1.0.0
 * @date 2020-10-30 14:27
 * @see
 */
public class LocalVariablesDemo {


    private int count = 0;

    public static void main(String[] args) {
        LocalVariablesDemo localVariablesDemo = new LocalVariablesDemo();
        int num = 10;
        localVariablesDemo.test1();

    }

    public void test1() {
        Date date = new Date();
        String name = "fablen.com";
        test2(date, name);
        System.out.println(date + name);
    }

    public String test2(Date date, String name) {
        date = null;
        name = "djaohklgds";
        double weitht = 132.4; //double占据2个slot
        char gender = '男';

        double hight = 293.98;
        int a = 10;

        return date + name;
    }

    public void test3() {
        this.count++;

    }

    public void test4() {
        int a = 10;
        {
            int b = 0;
            b = a + 1;
        }
        //变量c使用之前已经销毁的变量b占据的slot位置：变量b已经销毁了，因为数组的长度已经开辟了所以，c就占据了b以前所在的位置
        int c = a + 1;
    }

    /**
     * 变量的分类：按数据类型分： 基本数据类型，引用数据类型
     *          按类中声明分，成员变量：
     *                          类型变量：
     *                          实例变量：
     *                     局部变量
     */
}
