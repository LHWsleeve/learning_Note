import java.util.concurrent.*;

/**
 * CyclicBarrier:回环屏障，可以让一组线程全部达到一个状态后再全部同时执行。
 * 回环：当所有扽等该线程执行完毕后，充值CyclicBarrier的状态后，它可以被重用。
 * 屏障：线程调用await()方法后就会被阻塞，这个阻塞点被称之为屏障点。当所有子线程都调用了await方法后，线程们会冲破屏障
 */
public class CyclicBarrierTest {

    private static CyclicBarrier cyclicBarrier = new CyclicBarrier(2, new Runnable() {
        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(2);
                System.out.println(Thread.currentThread()+"任务合并完成");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });

    public static void main(String[] args) {
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(2,2,0,
                TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(5),
                new ThreadPoolExecutor.DiscardOldestPolicy());

        poolExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(Thread.currentThread()+"task-1");
                    System.out.println(Thread.currentThread()+"1---进入屏障");
                    cyclicBarrier.await();
                    //cyclicBarrier 执行完毕后，await返回，继续执行
                    System.out.println(Thread.currentThread()+"1---出屏障");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        });


        poolExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(Thread.currentThread()+"task-2");
                    System.out.println(Thread.currentThread()+"2---进入屏障");
                    cyclicBarrier.await();
                    System.out.println(Thread.currentThread()+"2---出屏障");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        });
        //关闭线程池
poolExecutor.shutdown();
    }

}
