package com.pattern.obsrever;

/**
 * Created by Administrator on 2016/6/1.
 */
public class TestObserverPattern {

    public static void main(String[] args) {
        ExampleObservable exampleObservable = new ExampleObservable();

        //添加一个观察者
        exampleObservable.addObserver(new ExampleObserver());

        exampleObservable.setData(-5);
        exampleObservable.setData(2000);
        exampleObservable.setData(40);


    }
}
