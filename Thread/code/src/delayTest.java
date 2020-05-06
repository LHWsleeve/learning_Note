import java.util.Date;
import java.util.Random;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class delayTest {
    static class Task implements Delayed{
        private long delay;//延时时间
        private long expire;//到期时间
        private String taskName;

        public Task(long delay, String taskName) {
            this.delay = delay;
            //到期时间=当前时间+延迟时间...为啥不直接设置
            this.expire = System.currentTimeMillis()+delay;
            this.taskName = taskName;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(this.expire- System.currentTimeMillis(),TimeUnit.MILLISECONDS);
        }

        /**
         * 重写的剩余时间函数，作为比较器参数
         * @param o
         * @return
         */
        @Override
        public int compareTo(Delayed o) {
            return this.getDelay(TimeUnit.MILLISECONDS)>o.getDelay(TimeUnit.MILLISECONDS)?1:-1;
        }

        public void doString(){
            System.out.println(this.taskName+":delay="+this.delay+"/expire="+this.expire);
        }

    }

    public static void main(String[] args) {
        DelayQueue<Task> delayQueue = new DelayQueue<>();
        Random random = new Random();
        for (int i=0;i<10;i++){
            Task task = new Task(random.nextInt(500),"task"+i);
            delayQueue.offer(task);
        }
        while (!delayQueue.isEmpty()){
            Task poll = delayQueue.poll();
            if (poll!=null){
                poll.doString();
            }
        }
    }

}
