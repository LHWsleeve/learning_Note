package jdk;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class reverse {
    public static void main(String[] args) {
        Integer [] s= new Integer[]{1,2,3,4,5,6,7
        };
        List<Integer> list = Arrays.asList(s);
        Collections.reverse(list);
        System.out.println(list);
        s=list.toArray(new Integer[0]);//没有指定类型的话会报错
    }
}
