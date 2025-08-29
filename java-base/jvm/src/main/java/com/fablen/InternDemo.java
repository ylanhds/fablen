package com.fablen;


/**
 * <p>
 * Description:
 * </p>
 *
 * @author zhangbaosheng
 * @version v1.0.0
 * @date 2020-11-03 20:39
 * @see
 */
public class InternDemo {

    /**
     * 如果常量池中已经存在了你需要的字符串常量，就不要重新创建了
     * 方式一：        String s1 = "abc";
     *               String s2 = s1.intern();
     * 方式二：
     *               String s2 = new String("abc").intern();
     */
    public void test1() {
        String s1 = "abc";
        String s2 = s1.intern();
        System.out.println(s1==s2); //true
    }

    public void test2() {
        String s1 = new String("abc");
        s1.intern();
        String s2 ="abc";
        System.out.println(s1==s2); //false

    }

    public void test3() {
        String s1 = new String("a") + new String("b"); //堆空间中，编译期优化  new的东西都在堆空间里面
        String s2 ="ab";   //字符串常量池中

        System.out.println(s1==s2); //false

    }
}
