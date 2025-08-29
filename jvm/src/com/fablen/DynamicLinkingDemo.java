package com.fablen;

/**
 * <p>
 * Description:
 * </p>
 *
 * @author zhangbaosheng
 * @version v1.0.0
 * @date 2020-10-30 21:10
 * @see
 */
public class DynamicLinkingDemo {


    public void methodA(){
        System.out.println("methosA");
    }
    public void methodB(){
        System.out.println("methodB");
        methodA();
    }
}
