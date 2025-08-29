package com.fablen;


/**
 * <p>
 * Description: 栈上分配测试   逃逸分析
 *   -Xmx256m -Xms256m -XX:+DoEscapeAnalysis -XX:+PrintGCDetails
 * </p>
 *
 * @author zhangbaosheng
 * @version v1.0.0
 * @date 2020-11-02 0:36
 * @see
 */
public class StackAllocation {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) {
            alloc();
        }
        long end = System.currentTimeMillis();

        System.out.println("花费时间： " + (start - end) + "ms");
        try {
            Thread.sleep(1000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void alloc() {
        User user = new User(); //未发生逃逸
    }
    static class User{}

}