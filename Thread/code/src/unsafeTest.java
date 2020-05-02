import sun.misc.Unsafe;

/**
 *由于双亲委派机制，main方法从app类加载器启动，而rt.jar从bootstrap启动，所以通过main无法初始化unsafe
 * 会报错
 */
public class unsafeTest {
    //获取unsafe实例
    static final Unsafe unsafe = Unsafe.getUnsafe();
    //记录变量state在insafeTest类中的的偏移值
    static long stateOffset;
    private  volatile long state = 0;
    static {
        try {
            unsafe.objectFieldOffset(unsafeTest.class.getDeclaredField("state"));
        } catch (NoSuchFieldException e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        //创建实例并且设置state的值为1
        unsafeTest unsafeTest = new unsafeTest();
        boolean compareAndSwapInt = unsafe.compareAndSwapInt(unsafeTest, stateOffset, 0, 1);
        System.out.println(compareAndSwapInt);
    }


}
