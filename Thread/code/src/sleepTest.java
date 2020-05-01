import java.awt.desktop.SystemSleepEvent;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * sleep测试：验证sleep时候，线程不会释放锁。
 * 即，A线程sleep时候并没有释放独占锁，只有A执行成功后，B线程才获得lock
 * 另外：sleep时中断该线程，会抛出异常。但是要注意，sleep睡眠被终止，虽然会抛异常，但是子线程回重新回到激活状态向下继续执行
 */
public class sleepTest {
  private static final Lock lock = new ReentrantLock();//默认非公平锁

    public static void main(String[] args) {
        Thread threadA = new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                System.out.println("子线程A开始获得独占锁，准备睡眠");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        });

        Thread threadB = new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                System.out.println("子线程B开始执行");
                lock.unlock();
            }
        });
        threadA.start();
        threadB.start();
    }
}
