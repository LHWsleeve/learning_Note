
public class WaitTest {

    private static String s="Lock";

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                    System.out.println("测试wait中断");
                    synchronized (s){
                        try {
                            //要阻塞当前线程
                            s.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
            }
        });
        thread.start();
        Thread.sleep(1000);
        thread.interrupt();
    }
}
