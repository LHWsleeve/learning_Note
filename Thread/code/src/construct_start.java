import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class construct_start {

    public static void main(String[] args) throws InterruptedException {
        System.out.println(Thread.currentThread());//主线程
        System.out.println("----");
        thread threadtask = new thread();
        threadtask.start();
        Thread.sleep(100);
        new Thread(new run()).start();
        FutureTask<String> futureTask = new FutureTask<>(new call());
        new Thread(futureTask).start();//这两种启动方式都得这样
        try {
            //任务执行结束，获取返回值
            String s = futureTask.get();
            System.out.println(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}

class thread extends Thread{
    @Override
    public void run() {
        System.out.println(this+":");//通过this直接获得线程名字
        System.out.println("继承Thread的多线程方式");
        System.out.println("========");

    }
}

class run implements Runnable{

    @Override
    public void run() {
        System.out.println(this+":");
        System.out.println("实现Runnable的run方法");
        System.out.println(Thread.currentThread());//Runnable必须通过这种方式获得线程名
        System.out.println("========");

    }
}

/**
 * 带返回值的并发线程
 */
class call implements Callable<String> {
    @Override
    public String call() throws Exception {
        return "我有返回值！！！";
    }
}