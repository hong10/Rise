package com.example.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Administrator on 2016/5/5.
 */
public class TestThread {


    /**
     * 定义装苹果的篮子
     */
    class Basket {
        BlockingQueue<String> basket = new LinkedBlockingQueue<String>(3);

        public void produce() throws InterruptedException {
            basket.put("An apple");
        }

        public String consume() throws InterruptedException {
            return basket.take();
        }

    }


    class Producer implements Runnable {
        private String instance;
        private Basket basket;

        Producer(String instance, Basket basket) {
            this.instance = instance;
            this.basket = basket;
        }

        @Override
        public void run() {

            while (true) {
                try {
                    System.out.println("Producer prepare: " + instance);
                    basket.produce();
                    System.out.println("Producer over: " + instance);
                    Thread.sleep(300);

                } catch (InterruptedException e) {
                    System.out.println("Producer Interrupted");
                }

            }

        }
    }

    class Consumer implements Runnable {
        private String instance;
        private Basket basket;

        public Consumer(String instance, Basket basket) {
            this.instance = instance;
            this.basket = basket;
        }

        public void run() {
            try {
                while (true) {
                    System.out.println("Consumer prepare: " + instance);
                    System.out.println(basket.consume());
                    System.out.println("Consumer over: " + instance);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException ex) {
                System.out.println("Consumer Interrupted");
            }
        }
    }


    public static void main(String[] args) {
        TestThread tt = new TestThread();
        Basket basket = tt.new Basket();

        ExecutorService service = Executors.newCachedThreadPool();
        Producer producer = tt.new Producer("Producer001", basket);
        Producer producer2 = tt.new Producer("Producer002", basket);
        Consumer consumer = tt.new Consumer("Consumer001", basket);

        service.submit(producer);
        service.submit(producer2);
        service.submit(consumer);


    }
}
