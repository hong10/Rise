package com.hong.thread;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Administrator on 2016/7/13.
 */
public class Driver {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(5);
        for (int i = 0; i < 5; ++i) // create and start threads
            new Thread(new Worker(startSignal, doneSignal)).start();


        doSomethingElse1();            // don't let run yet

        Thread.sleep(3000);

        startSignal.countDown();      // let all threads proceed

        doneSignal.await();           // wait for all to finish

        doSomethingElse2();
        System.out.println("over");


    }

    private static void doSomethingElse1() {
        System.out.println("Driver preparing");
    }

    private static void doSomethingElse2() {
        System.out.println("Driver clean");
    }


}
