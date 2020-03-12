package jdk;

public class Super {
    private int a;
    public void print(){
        System.out.println("super的print");
    }
    Super(){
        System.out.println("Spuer无参构造器");
  }
  Super(int a){
      System.out.println("super有参构造器："+a);
  }
}

