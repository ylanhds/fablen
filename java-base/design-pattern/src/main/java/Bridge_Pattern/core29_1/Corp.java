package Bridge_Pattern.core29_1;

public abstract class Corp {

    /**
     * 如果是公司就该有生产 每家公司的产品都不一样
     */
    protected abstract void produce();

    /**
     * 有产品,就有销售
     */
    protected abstract void sell();
    //公司是赚钱的
    public void makeMoney(){
        //先生产
        this.produce();
        //后销售
        this.sell();
    }

}
