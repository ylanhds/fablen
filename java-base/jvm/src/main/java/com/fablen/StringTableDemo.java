package com.fablen;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

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
public class StringTableDemo {

    @Test
    public void test1() {
        String s1 = "fablen";    //字面量的定义方式,"fablen"存储在字符串常量池（不可能存相同字符）
        String s2 = "fablen";    //字面量的定义方式,"fablen"存储在字符串常量池（不可能存相同字符）

        System.out.println(s1 == s2); //判断地址：true

    }

    @Test
    public void test2() {
        String s1 = "fablen";
        String s2 = "fablen";
        String s3="fablen";
        s2 += "hello";
        System.out.println(s1 == s2); //判断地址：false
    }


    @Test
    public void test3() {
        String s1 = "fablen";
        String s2 = s1.replace("f","y");
        System.out.println(s1); // fablen
        System.out.println(s2); // yablen
    }

    // -XX:MetaspaceSize=60m -XX:MaxMetaspaceSize=60m -Xms60m -Xmx60m
    public static void main(String[] args) {
        //使用set保持常量池引用，避免full gc 回收常量池行为
        Set<String> set= new HashSet<>();
        short i = 0;
        while (true){
            set.add(String.valueOf(i++).intern());
        }
    }
}
