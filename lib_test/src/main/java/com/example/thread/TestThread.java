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
        //篮子，能够容纳3个苹果
        BlockingQueue<String> basket = new LinkedBlockingQueue<String>(3);

        //生产苹果，放入篮子
        public void produce() throws InterruptedException {
            basket.put("An apple");
        }

        //消费苹果，从篮子中取走
        public String consume() throws InterruptedException {
            return basket.take();
        }

    }


    //定义苹果生产者
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
                // 生产苹果
                try {
                    System.out.println("Producer prepare: " + instance);
                    basket.produce();
                    System.out.println("Producer over: " + instance);
                    // 休眠300ms
                    Thread.sleep(300);

                } catch (InterruptedException e) {
                    System.out.println("Producer Interrupted");
                }

            }

        }
    }

    // 定义苹果消费者
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
                    // 消费苹果
                    System.out.println("Consumer prepare: " + instance);
                    System.out.println(basket.consume());
                    System.out.println("Consumer over: " + instance);
                    // 休眠1000ms
                    Thread.sleep(1000);
                }
            } catch (InterruptedException ex) {
                System.out.println("Consumer Interrupted");
            }
        }
    }


    public static void main(String[] args) {
        TestThread tt = new TestThread();
        //创建一个装苹果的篮子
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
