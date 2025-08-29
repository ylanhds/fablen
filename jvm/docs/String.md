一、String的基本特性

------

- String：双引号表示字符串。

  ```java
          String s1 = "fablen";    //字面量的定义方式
          String s2 = new String("fablen"); // new方式
  ```

  

- String：由 final 关键字修饰。

  ```java
    public final class String implements java.io.Serializable, Comparable<String>, CharSequence {...}
  ```




- String：不可变性，代表不可变的字符序列。

  1. 当字符串重新赋值时，需要重写内存区域赋值，不能使用原有的value进行赋值

  2. 当对现有的字符串进行连接时，也需要重新指定内存区域赋值，不能使用原有的value进行赋值

     ```java
         @Test
         public void test2() {
             String s1 = "fablen";
             String s2 = "fablen";
             String s3="fablen";
             s2 += "hello";
             System.out.println(s1 == s2); //判断地址：false
         }
     ```

     

  3. 当调用String的replace()方法修改指定字符串或字符时，也需要重新指定内存区域赋值，不能使用原有的value进行赋值

     ```java
         @Test
         public void test3() {
             String s1 = "fablen";
             String s2 = s1.replace("f","y");
             System.out.println(s1); // fablen
             System.out.println(s2); // yablen
         }
     ```



- 在字符串常量池中是不会存储相同内容的字符串

		> Java语言规范里要求完全相同的字符串字面量应该包含相同的Unicode字符序列（同一份码点序列的常量），并且必须是指向同一个String类实例 



二、String的内存分配

------

- String类型的常量池比较特殊，主要的使用方法有2种

  1. 直接使用双引号声明出来的String对象会直接存储在常量池中

     ```java
         String info = "fablen";   //"fablen" 字符串是直接存储在常量池中
     ```

     

  2. 使用 intern（）方法    (  它会在常量池中查询当前字符串常量，没有就重新创建了,有就直接返回地址)

     ```java
         /**
          * 常量池中查询当前字符串常量，没有就重新创建了,有就直接返回地址
          * 方式一：        String s1 = "abc";
          *               String s2 = s1.intern();
          * 方式二：
          *               String s2 = new String("abc").intern();   
          */
         @Test
         public void test1() {
             String s1 = "abc";
        String s2 = s1.intern();
     
           System.out.println(s1==s2); //true
         }
     ```
     
     
  



三、字符串拼接

------

1. 常量与常量的拼接结果在常量池，原理是编译期优化

   ```java
       @Test
       public void test1() {
           String s1 = "a"+"b"+"c";   //
           String s2 = "abc";    // "abc" 一定是放在字符串常量池中，将此地址赋值给s2
   
           System.out.println(s1 == s2); //true
           System.out.println(s1.equals(s2));//true
   
       }
   ```

   

2. 常量池中不会存在相同内容的常量

3. 只要其中有一个是变量，结果就在堆中。(相当于在堆空间 new Sring()，具体的内容为拼接的结果)。变量拼接的原理是StringBuilder

   ```java
       @Test
       public void test2() {
           String s1 = "abc";
           String s2 = "ab";
           String s3 = s2 + "c"; 
   
           System.out.println(s1 == s3); //判断地址：false
       }
   
   ```

   ```java
       /**
        * 字符串拼接操作不一定使用StringBuilder
        * 如果拼接符号左右两边都是字符串常量或者常量引用，则仍然使用编译期优化，即非StringBuilder的方式
        * 针对于final修饰类、方法、基本数据类型、引用数据类型的结构时，能使用的尽量使用final修饰
        */
       @Test
       public void test4() {
           final String s1 = "a";
           final String s2 = "b";
           String s3 = "ab";
           String s4 = s1 + s2;
   
           System.out.println(s3 == s4); //true
       }
   ```

   

4. 如果拼接的结果调用intern()方法，则主动将常量池中还没有的字符串对象放入池中，并返回此对象地址。

   ```java
       @Test
       public void test3() {
           String s1 = "fablen";
           String s2 = s1.intern();
           
           System.out.println(s2 == s1);
       }
   ```

   

5. 拼接效率比较。执行效率：通过StringBuilder的append()的方式添加字符串的效率远高于使用String的字符串拼接方式。

   ```java
       @Test
       public void test5() {
           long start = System.currentTimeMillis();
   
          //  methodA(100000); //花费时间：741
           methodB(100000); //花费时间：5
   
           long end = System.currentTimeMillis();
           System.out.println("花费时间：" + (end - start));
       }
   
       public void methodA(int count) {
           String src = "";
           for (int i = 0; i < count; i++) {
               src = src + "a";//每次循环都会创建一个StringBuilder,还会创建String
           }
       }
   
       public void methodB(int count) {
           //只创建一个StringBuilder
           StringBuilder src = new StringBuilder();
           for (int i = 0; i < count; i++) {
               src.append("a");
           }
           
           //改进的空间：在实际开发中，如果基本确定要添加的字符串长度不高于某个限定值count的情况下，建议使用构造器的方式
           //StringBuilder src = new StringBuilder(count);
       }
   ```

   

   四、G1的String去重操作

   ------

   

   
   

   
   五、面试题
   
   ------
   
   题目： new String("fablen"); 会创建几个对象？看字节码,就知道是两个
   
   1. 一个是在堆空间：new 关键字在堆空间创建
2. 另一个对象是：在字符串常量池,字节码：ldc
   
```java
    0 new #2 <java/lang/String>
 3 dup
    4 ldc #3 <fablen>
    6 invokespecial #4 <java/lang/String.<init>>
    9 astore_1
   10 return
   ```
   
   思考： new String("fablen")+new String("ylanhds"); 会创建几个对象？
   
   ​       
   
   


