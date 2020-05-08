import java.util.concurrent.*;

/**
 * 和countdownlatch和cyclicBarrier不同，内部计数器是递增的
 * 另外：semaphore也是可复用的。每次
 * semaphore.release()，会计数器+1.
 * semaphore.acquire(2);表示semaphore计数几次之后突破屏障
 * 但是semaphore的内部计数器是不可以自动重置的，但是可以通过更改acquire()的参数变相完成cyclicbarrier的回环效果。
 * 例如release两次之后acquire(2),再release两次，此时参数是acquire(4)
 */
public class semaphoreTest {
private static Semaphore semaphore = new Semaphore(0);

    public static void main(String[] args) throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        //A加入线程池
        executorService.submit(new Runnable() {
            @Override
            public void run() {

                semaphore.release();
            }
        });

        //A加入线程池
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread()+"over");
                semaphore.release();
            }
        });
        semaphore.acquire(2);
        System.out.println("所有子线程执行结束");
        executorService.shutdown();
    }




}
