package com.fablen;

import java.sql.DriverManager;

/**
 * <p>
 * Description:
 * </p>
 *
 * @author zhangbaosheng
 * @version v1.0.0
 * @date 2020-10-29 16:21
 * @see
 */
public class ClassLoaderDemo {
    public static void main(String[] args) {
        try {
            //方式一：获取当前类的ClassLoader   默认情况下拿到的是系统类加载器
            ClassLoader classLoader = Class.forName("java.lang.String").getClassLoader();
            System.out.println(classLoader);

            //方式二：获取当前线程上下文的ClassLoader
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            System.out.println(contextClassLoader);

            //方式三：获取系统的ClassLoader 。通过getParent可以获得扩展类加载器
            ClassLoader parent = ClassLoader.getSystemClassLoader().getParent();
            System.out.println(parent);

        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }

    }
}
