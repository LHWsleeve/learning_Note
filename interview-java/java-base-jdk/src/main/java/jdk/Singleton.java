package jdk;

public class Singleton {
   // 声明为 private 避免调用默认构造方法创建对象
    private Singleton() {
    }

   // 声明为 private 表明静态内部该类只能在该 Singleton 类中被访问
    private static class SingletonHolder {
        private static final Singleton INSTANCE = new Singleton();
    }

    public static Singleton getUniqueInstance() {
        return SingletonHolder.INSTANCE;
    }

    public static void main(String[] args) {
        System.out.println("x");
        Singleton uniqueInstance = getUniqueInstance();
        System.out.println("x");
    }
}