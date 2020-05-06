import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.concurrent.PriorityBlockingQueue;

public class priorityBlockQueueTest {
    //优先级队列
    PriorityQueue<Integer> priorityQueue = new PriorityQueue<Integer>();

    //阻塞优先级队列
    static PriorityBlockingQueue<Task> blockingQueue = new PriorityBlockingQueue<>();
    static class Task implements Comparable<Task>{
        private int priority=0;
        private String taskName;
        public int getPriority() {
            return priority;
    }
        public void setPriority(int priority) {
        this.priority = priority;
    }
        public void setTaskName(String taskName) {
            this.taskName = taskName;
        }

        public void doString(){
            System.out.println(taskName+":"+priority);
        }

        @Override
    public int compareTo(Task o) {
        return this.getPriority()>o.getPriority()? 1:-1;
    }
}
    public static void main(String[] args) {
        priorityBlockQueueTest priorityBlockQueueTest = new priorityBlockQueueTest();
        Random random = new Random();
        for (int i=0;i<10;i++){
            Task task = new Task();
            task.setPriority(random.nextInt(10));
            task.setTaskName("taskName:"+i);
            blockingQueue.offer(task);
        }
        while (!blockingQueue.isEmpty()){
            Task poll = blockingQueue.poll();
            if (poll!=null){
                poll.doString();
            }
        }
    }
}
