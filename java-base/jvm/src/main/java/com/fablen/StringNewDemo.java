package com.fablen;

/**
 * <p>
 * Description:
 * </p>
 *
 * @author zhangbaosheng
 * @version v1.0.0
 * @date 2020-11-03 20:54
 * @see
 */
public class StringNewDemo {
    public static void main(String[] args) {
        String s1 = new String("ab"); //执行完后，会在字符串常量池中生成"ab"
        String s2 ="ab";
        System.out.println(s1 == s2);
//        String s2 = new String("fablen") + new String("ylanhds");
//        String s2 = new String("fablen") + new String("fablen");
    }
}
