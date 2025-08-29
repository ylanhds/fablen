package com.fablen;

import java.lang.ref.SoftReference;

/**
 * <p>
 * Description: -ea  -Xms10M -Xmx10M -XX:+PrintGCDetails
 * </p>
 *
 * @author zhangbaosheng
 * @version v1.0.0
 * @date 2020-11-05 15:44
 * @see
 */
public class SoftDemo {
    public static void main(String[] args) {
        User user = new User(18, "fablen");
        SoftReference<User> softReference = new SoftReference<>(user);
        user = null;

        // 从软引用中重新获得强引用对象
        System.out.println(softReference.get());

        System.gc();//只是建议回收 有可能在执行循环的时候没有进行回收
        System.out.println("进行GC ");
        System.out.println(softReference.get());//由于堆空间内存足够，所以不会回收软引用的可达对象
        try {
            // 让系统认为内存资源紧张，不够   (-Xms10M -Xmx10M -XX:+PrintGCDetails)
           // byte[] b = new byte[1024*1024*1];
            byte[] b = new byte[1024*1024*9];
        } catch (Throwable e) {
            e.printStackTrace();
        }finally {
            //再次从软引用中获得数据
            System.out.println(softReference.get());//在报OOM之前，垃圾回收器会回收软引用的可达对象
        }
    }
}
