import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class AQS_singal_await {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        System.out.println("条件变量开始,先获取锁");

        try {
            lock.lock();
            condition.await();
            System.out.println("条件变量被释放");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

}
