import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class AQS_C_P {
    static final ReentrantLock lock = new ReentrantLock();
    static final Condition notFull = lock.newCondition();
    static final Condition notEmpty = lock.newCondition();
    static final LinkedBlockingQueue<String> linkedBlockingQueue = new LinkedBlockingQueue<>();
    static final int queueSize=10;
    public static void main(String[] args) {
        Thread P_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //一定要先获取锁
                lock.lock();
                //使用while防止伪唤醒
                    try {
                        while (linkedBlockingQueue.size() == queueSize) {
                        //如果队列满了，等待
                        notEmpty.await();
                        }
                        linkedBlockingQueue.add("abb");
                        //唤醒消费者线程
                        notFull.signalAll();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        lock.unlock();
                    }


            }
        });
        //消费者线程
        Thread C_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                    try {
                        while (linkedBlockingQueue.size() == 0) {
                            //消费者线程等待
                        notFull.await();
                        }
                        linkedBlockingQueue.remove("abb");
                        //唤醒生产者线程
                        notEmpty.signalAll();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        lock.unlock();
                    }


            }
        });

        C_thread.start();
        P_thread.start();
    }
}
