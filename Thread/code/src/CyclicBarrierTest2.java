import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 假设有个任务，分三个阶段完成，多个线程必须都完成阶段1，阶段2，后才能继续阶段3
 */
public class CyclicBarrierTest2 {
    //输入的参数，和线程个数一致，为多个线程做屏障
    private static CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

    public static void main(String[] args) {
        //不符合P3C标准，但是，我懒
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        //线程A 加入线程池
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("阶段1");
                    // 显然cyclicBarrier可以重复使用
                    cyclicBarrier.await();
                    System.out.println("阶段2");
                    cyclicBarrier.await();
                    System.out.println("step 3");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        });
        //线程B
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("阶段1");
                    cyclicBarrier.await();
                    System.out.println("阶段2");
                    cyclicBarrier.await();
                    System.out.println("step 3");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        });
        //关闭线程池
        executorService.shutdown();
    }

}
