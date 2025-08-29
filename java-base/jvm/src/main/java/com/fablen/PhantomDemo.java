package com.fablen;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

/**
 * <p>
 * Description:
 * </p>
 *
 * @author zhangbaosheng
 * @version v1.0.0
 * @date 2020-11-05 17:03
 * @see
 */
public class PhantomDemo {
    public static void main(String[] args) {
        PhantomReference<User> phantomReference = new PhantomReference<User>(new User(18,"Fablen"),new ReferenceQueue<User>());
        System.out.println(phantomReference.get());
    }
}
