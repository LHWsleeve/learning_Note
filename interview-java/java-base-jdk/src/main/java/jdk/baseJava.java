package jdk;

import javax.swing.plaf.synth.SynthStyle;

public class baseJava {
    int a =2;
    static int b = 3;
    public static void main(String[] args) {
        String s = "sss2";
        String s2= new String("sss2");
        String s3= new String("sss2");

        StringBuffer stringBuffer = new StringBuffer("buffer");
        StringBuilder stringBuilder = new StringBuilder("StringBuilder");
        StringBuffer stringBuffer2 = stringBuffer;
        System.out.println(stringBuffer.hashCode()+"\n"+stringBuffer2.hashCode());
        System.out.println("\n");
        System.out.println(s.hashCode()+"\n"+s2.hashCode()+"\n"+s3.hashCode());
        System.out.println(s3==s2);

    }
    public static void staticA(){

        System.out.println("测试静态方法");
    }

    public void instanceB(){

        System.out.println("测试实例方法");
    }

}

