import java.util.concurrent.atomic.AtomicLong;

public class AtomicTest {
    private static AtomicLong atomicLong = new AtomicLong();

    public static void main(String[] args) throws InterruptedException {
        Integer[] one = {1, 0, 1, 2, 4, 60, 34, 60, 0};
        Integer[] two = {0, 0, 5, 2, 0, 60, 4, 60, 0};

        Thread onethread = new Thread(new Runnable() {
            @Override
            public void run() {
                //注意一点，foreach不是多线程操作，是遍历集合的，且在遍历时不能修改集合的内容，所以要是有需要修改集合内容的操作时，不能用foreach
                for (int i=0;i<one.length;i++) {
                    if (one[i] == 0) {
                        atomicLong.incrementAndGet();
                    }
                }
            }
        });
        Thread twothread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0;i<two.length;i++) {
                    if (two[i] == 0) {
                        atomicLong.incrementAndGet();
                    }
                }
            }
        });

        onethread.start();
        twothread.start();
        //等待执行完毕，防止提前打印原子变量
        onethread.join();
        twothread.join();
        System.out.println(atomicLong);


    }


}
