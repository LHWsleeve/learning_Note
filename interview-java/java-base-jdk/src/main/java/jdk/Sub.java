package jdk;

public class Sub extends Super{

    public Sub(int a) {
        super(a);
    }

    public void test(){
        super.print();
    }

    public static void main(String[] args) {

        Sub sub = new Sub(5);
        System.out.println("===============");
        sub.test();

    }
}
