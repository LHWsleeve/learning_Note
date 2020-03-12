package jdk;

import java.util.ArrayList;
import java.util.Iterator;

public class foreach_remove {
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<String>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
//      //迭代器方式
//        Iterator<String> iterator = list.iterator();
//        while (iterator.hasNext()){
//            String s = iterator.next();
//            if (s.equals("1")){
//                iterator.remove();
//            }
//        }
        for (int i=0;i<list.size();i++){
            if (list.get(i).equals("1")){
                list.remove(list.get(i));
            }
        }

        System.out.println(list);
    }
}
