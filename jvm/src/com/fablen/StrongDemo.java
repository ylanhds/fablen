package com.fablen;

/**
 * <p>
 * Description:
 * </p>
 *
 * @author zhangbaosheng
 * @version v1.0.0
 * @date 2020-11-05 15:19
 * @see
 */
public class StrongDemo {
    public static void main(String[] args) {
        StringBuffer s1 = new StringBuffer("Hello,fablen");
        StringBuffer s2 = s1;
        s1 = null;// s1置null
        System.gc();//只是建议回收 有可能在执行循环的时候没有进行回收
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(s1); //null
        System.out.println(s2); //Hello,fablen
    }
}
