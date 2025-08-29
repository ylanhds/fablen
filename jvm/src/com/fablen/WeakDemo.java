package com.fablen;

import java.lang.ref.WeakReference;

/**
 * <p>
 * Description:
 * </p>
 *
 * @author zhangbaosheng
 * @version v1.0.0
 * @date 2020-11-05 16:45
 * @see
 */
public class WeakDemo {
    public static void main(String[] args) {
        //构造弱引用，（简写）
        WeakReference<User> oWeakReference = new WeakReference<User>(new User(18,"Hello,ylanhds"));

        // 从弱引用中重新获得强引用对象
        System.out.println(oWeakReference.get());
        System.gc();
        //不管当前内存空间足够与否，都会回收它的内存
        System.out.println("进行GC ");
        // 从弱引用中重新获得强引用对象
        System.out.println(oWeakReference.get());
    }
}
