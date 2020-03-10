package jdk;

import javax.swing.plaf.synth.SynthStyle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

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

        System.out.println("开始异常测试");
        baseJava baseJava = new baseJava();
        int i = baseJava.instanceB(3);
        System.out.println(i);

//        System.out.println("测试获取键盘输入");
//        Scanner scanner = new Scanner(System.in);
////        System.out.println(scanner);
//        String s1 = scanner.nextLine();
//        System.out.println(s1);
//        scanner.close();
//
//        System.out.println("测试第二个键盘输入");
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
//        String readLine = null;
//        try {
//            readLine = bufferedReader.readLine();
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("第二次失败");
//        }
//        System.out.println(readLine);
        
    }
    public static void staticA(){

        System.out.println("测试静态方法");
    }


    public int instanceB(int a){
        try {
            System.out.println("try");
            return a*a;
        } finally {
            if (a==3){
                return a=0;
            }
            System.out.println("finally");
        }
    }

}

