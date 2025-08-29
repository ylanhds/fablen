package Proxy_Pattern;

public class Test {
    public static void main(String[] args) {
        Action userAction = new UserAction();
        ActionProxy proxy = new ActionProxy(userAction);
        proxy.doAction();
    }
}


//代理
class ActionProxy implements Action{
    private Action targer;//被代理对象
    public ActionProxy(Action targer){
        this.targer =targer;
    }
    //执行操作
    @Override
    public void doAction() {
        long start = System.currentTimeMillis();

        //可添加控制操作(方法之前..要干什么)

        targer.doAction();;

        //方法之后..要干什么
        long end =System.currentTimeMillis();
        System.out.println("共耗时"+(end-start)+" ms");
    }
}

//业务接口
interface Action{
     void doAction();
}
//业务具体实现类
class  UserAction implements Action{
    @Override
    public void doAction() {
        System.out.println("用户开始工作");
    }
}