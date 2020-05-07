import java.util.concurrent.*;

/**
 * 使用线程池
 */
public class JoinCountDownLatch2 {
private static CountDownLatch downLatch = new CountDownLatch(2);

    public static void main(String[] args) throws InterruptedException {
        //这种写法..不符合阿里规范
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        //要使用明确的线程池
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(2, 2, 0, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(2),
                new ThreadPoolExecutor.DiscardOldestPolicy());
        //将线程A添加入线程池(注意，加入后立即执行)
        poolExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("准备执行A");
                    Thread.sleep(10000);
                    System.out.println("执行子线程A");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    downLatch.countDown();
                }
            }
        });


        //将线程B添加入线程池
        poolExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("准备子线程B");
                    Thread.sleep(1000);
                    System.out.println("执行子线程B");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    downLatch.countDown();
                }
            }
        });
        System.out.println("等待子线程执行结束");
        downLatch.await();
        System.out.println("所有子线程执行结束");
        poolExecutor.shutdown();

    }

}
