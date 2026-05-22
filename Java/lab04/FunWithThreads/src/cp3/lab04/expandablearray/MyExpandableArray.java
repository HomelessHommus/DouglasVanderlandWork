package cp3.lab04.expandablearray;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyExpandableArray extends Thread{

    ExpandableArray ea;
    Object lock =  new Object();

    public MyExpandableArray(ExpandableArray ea){
        this.ea = ea;
    }

    int threadCount = 10;
    public ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    public CyclicBarrier barrier = new CyclicBarrier(threadCount);
    public CountDownLatch countDownLatch = new CountDownLatch(threadCount);

    public void addWithThreads(Object o) {
            executor.submit(() -> {

                try {
                    barrier.await();
                    ea.add(o);
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
                finally{
                    countDownLatch.countDown();
                }
            });
        }

    public void allThreadGo() throws InterruptedException{
        countDownLatch.await();
        executor.shutdown();
    }
}


