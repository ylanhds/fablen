package com.fablen;

import org.junit.jupiter.api.Test;

/**
 * <p>
 * Description:
 * </p>
 *
 * @author zhangbaosheng
 * @version v1.0.0
 * @date 2020-11-03 16:08
 * @see
 */
public class StringTestDemo {

    @Test
    public void test1() {
        String s1 = "a" + "b" + "c";   //
        String s2 = "abc";    // "abc" 一定是放在字符串常量池中，将此地址赋值给s2

        System.out.println(s1 == s2); //true
        System.out.println(s1.equals(s2));//true

    }

    @Test
    public void test2() {
        String s1 = "abc";
        String s2 = "ab";
        String s3 = s2 + "c";

        System.out.println(s1 == s3); //判断地址：false
    }


    @Test
    public void test3() {
        String s1 = "fablen";
        String s2 = s1.intern();
        System.out.println(s2 == s1);
    }


    /**
     * 字符串拼接操作不一定使用StringBuilder
     * 如果拼接符号左右两边都是字符串常量或者常量引用，则仍然使用编译期优化，即非StringBuilder的方式
     * 针对于final修饰类、方法、基本数据类型、引用数据类型的结构时，尽量使用final
     */
    @Test
    public void test4() {
        final String s1 = "a";
        final String s2 = "b";
        String s3 = "ab";
        String s4 = s1 + s2;

        System.out.println(s3 == s4); //true
    }

    @Test
    public void test5() {
        long start = System.currentTimeMillis();

       //  methodA(100000); //花费时间：741
        methodB(100000); //花费时间：5

        long end = System.currentTimeMillis();
        System.out.println("花费时间：" + (end - start));
    }

    public void methodA(int count) {
        String src = "";
        for (int i = 0; i < count; i++) {
            src = src + "a";//每次循环都会创建一个StringBuilder,还会创建String
        }
    }

    public void methodB(int count) {
        //只创建一个StringBuilder
        StringBuilder src = new StringBuilder();
        for (int i = 0; i < count; i++) {
            src.append("a");
        }
    }






}
