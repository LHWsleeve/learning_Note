import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用reentrantLock生成线程安全的List
 */
public class reentrantLockList {
final static ReentrantLock lock = new ReentrantLock();
final static ArrayList<Integer> list = new ArrayList<Integer>();

    /**
     * 增加数据
     * @param a
     */
    public void add(Integer a){
    lock.lock();
    try {
        list.add(a);
    } finally {
        lock.unlock();
    }
}

    /**
     * 删数据
     * @param b
     */
    public void remove(Integer b){
    lock.lock();
    try {
        list.remove(b);
    } finally {
        lock.unlock();
    }
}
/**
 * 查数据
 */


}
