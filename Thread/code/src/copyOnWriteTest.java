import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class copyOnWriteTest {

//增删改都是枷锁后，使用快照进行修改。
//查找时由于未加锁，同时又由于增删改使用的是快照，查找出来的值可能是作出修改之前的值。这就是弱一致性问题
private  static CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList();

  public static void main(String[] args) throws InterruptedException {
    copyOnWriteArrayList.add("hello");
    copyOnWriteArrayList.add("123");

    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        copyOnWriteArrayList.set(1, "nononon");
        copyOnWriteArrayList.add("sfsdfds");
      }
    });
    //重点在这里。复制时写的迭代器是快照迭代器，在线程启动前获取快照后，此后的任何修改堆这个迭代器都是不可见的
    Iterator iterator = copyOnWriteArrayList.iterator();
    thread.start();
    thread.join();

    while (iterator.hasNext()){
      System.out.println(iterator.next());
    }
    //实际上修改了，但是对快照不可见
    System.out.println(copyOnWriteArrayList.toString());
  }

}
