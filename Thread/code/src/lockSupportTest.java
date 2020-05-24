import sun.util.resources.ja.LocaleNames_ja;

import java.util.concurrent.locks.LockSupport;

public class lockSupportTest {
    public static void main(String[] args) {
        //如果unpark在前，遇到park会直接返回
        LockSupport.unpark(Thread.currentThread());
        System.out.println("park 开始");
        //只有park，没有unpark会一直挂起
        LockSupport.park();
        System.out.println("park 结束");
    }

}
