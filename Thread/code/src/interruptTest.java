/**
 * 所谓的优雅的中断退出
 */
public class interruptTest {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {//主要就是这个判断而已.....
                    System.out.println(Thread.currentThread() + "hello");
                }
                try {
                    Thread.sleep(1000000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("睡眠被终止，线程被唤醒");
            }
        });
        thread.start();
        thread.interrupt();
        thread.join();
    }
}
