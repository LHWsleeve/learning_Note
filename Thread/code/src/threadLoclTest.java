public class threadLoclTest {
    //每个线程可以设置多个ThreadLocal实例，每个实例都可以设置一个条件变量，从而放在本地内存的hashmap里(threadLocal是key,本地变量是value)
    static ThreadLocal<String> localVariable = new ThreadLocal<String>();

    static void print(String str){
        //打印当前线程本地内存
        System.out.println(str+":"+localVariable.get());
        //清楚本地内存中的变量
        localVariable.remove();
    }

    public static void main(String[] args) {
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                //设置本地内存变量值。本地变量并不是直接放在localVariable实例里面，而是放在threadslocals这个map里面，通过set，get方法获取
                localVariable.set("线程1本地内存变量");
                localVariable.set("线程1本地内存变量2");//会覆盖第一次的设置
                //调用print函数打印，并清除
                print("thread1");
                //打印本地变量值
                System.out.println("thread1:"+localVariable.get());
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                //设置本地内存变量值
                localVariable.set("线程2本地内存变量");
                //调用print函数打印，并清除
                print("thread2");
                //打印本地变量值
                System.out.println("thread2:"+localVariable.get());
            }
        });

        thread1.start();
        thread2.start();
        System.out.println("主线程的本地变量："+localVariable.get());//这是为了证明，threadLocals作为线程内部成员变量，子线程和父线程互不影响。
    }
    /**
     *
     *ThreadLocal类会产生子类无法访问父类本地变量的问题。inheritableThreadLocal类解决
     * inheritableThreadLocal类继承并重写了ThreadLocal的三个方法。
     * 在Thread创建的时候，就会判断inheritableThreadLocal是否是null，如果不是就会创建这种类型。
     * 当父线程创建子线程时候，构造函数会把父线程中的本地变量复制一份保存到子线程的inheritableThreadLocal中去。
     */
      InheritableThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<String>();
}
