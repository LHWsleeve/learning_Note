import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 *通过反射来使用Unsafe类
 */
public class unsafeTest2 {
    static Unsafe theUnsafe;
    static long stateOffset;
    private  volatile long state = 0;
    static {
        try {
            //使用反射获取Unsafe的成员变量theUnasafe
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            //设置为可存取
            field.setAccessible(true);
            //获取该变量的值
            theUnsafe =(Unsafe) field.get(null);
            //获取state在类中的偏移量
            theUnsafe.objectFieldOffset(unsafeTest2.class.getDeclaredField("state"));

        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        //创建实例并且设置state的值为1
        unsafeTest2 unsafeTest2 = new unsafeTest2();
        boolean compareAndSwapInt=false;
           compareAndSwapInt = theUnsafe.compareAndSwapInt(unsafeTest2, stateOffset, 0, 1);
        System.out.println(compareAndSwapInt);
    }


}
