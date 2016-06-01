package com.pattern.obsrever;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Administrator on 2016/6/1.
 */
public class ExampleObserver implements Observer {

    @Override
    public void update(Observable o, Object arg) {
        ExampleObservable exampleObservable = (ExampleObservable) o;
        System.out.println("exampleObservable is change, the new value is : "+exampleObservable.getData());
    }
}
