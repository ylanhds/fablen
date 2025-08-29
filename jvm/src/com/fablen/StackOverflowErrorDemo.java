package com.fablen;

/**
 * <p>
 * Description:
 * </p>
 *
 * @author zhangbaosheng
 * @version v1.0.0
 * @date 2020-10-30 14:22
 * @see
 */
public class StackOverflowErrorDemo {
    private static  int cuont=0;
    public static void main(String[] args) {
//        main(args);

        StackOverflowErrorDemo stackOverflowErrorDemo = new StackOverflowErrorDemo();
        stackOverflowErrorDemo.test();
    }

    public void test(){
        System.out.println(cuont++);
        test();
    }
}
