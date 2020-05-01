import java.util.LinkedList;

/**
 * 最简单的生产者消费者模型--synchronized，wait，notify方法完成
 * 问题：由于使用同一个锁，线程唤醒时，消费者生产者都会被唤醒。
 */
public class notifyAndWait {
    LinkedList<Integer> queue = new LinkedList<>();
    private static final int MAX_SIZE=10;
    private  Integer count=0;
    public static void main(String[] args){
        notifyAndWait notifyAndWait = new notifyAndWait();
        //生产者线程
        for (int i=0;i<20;i++){
            Thread p = new Thread(notifyAndWait.new produrce());
            p.start();
        }
        //消费者线程
        for (int i=0;i<20;i++){
            Thread c = new Thread(notifyAndWait.new comsuer());
            c.start();
        }

    }
    //生产者内部类
    class produrce implements Runnable{
        @Override
        public void run() {
            //生产线程
            synchronized (queue){
                while (queue.size()==MAX_SIZE){
                    try {//如果队满，挂起线程
                        queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                queue.add(count);
                System.out.println(Thread.currentThread()+"生产物资:"+ count++);
                queue.notify();
            }
        }
    }

    //消费者内部类
    class comsuer implements Runnable{
        @Override
        public void run() {
            //消费者线程
            synchronized (queue){
                while (queue.size()==0){
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Object poll = queue.poll();
                System.out.println(Thread.currentThread()+"消费物资:"+poll);
                queue.notify();
            }
        }
    }
}

