import java.util.concurrent.CountDownLatch;

/**
 * 多个线程并行执行任务，等待所有子线程执行完毕之后在进行汇总。
 */
public class JoinCountDownLatch {
    //简历countDownLatch实例,volatile保证可见性,线程个数作为参数.线程个数一定要和子线程数一致，否则程序一直等待
    public static volatile CountDownLatch downLatch = new CountDownLatch(2);

    public static void main(String[] args) throws InterruptedException {
        Thread threadOne = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    downLatch.countDown();//返回主线程
                }
            }
        });

        Thread threadTwo = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    downLatch.countDown();
                }
            }
        });

        threadOne.start();
        threadTwo.start();
        System.out.println("等待子线程结束");

        downLatch.await();//等待子线程执行完毕，返回

        System.out.println("所有子线程执行结束");
    }




}
