package com.pattern.obsrever;

import java.util.Observable;

/**
 * Created by Administrator on 2016/6/1.
 */
public class ExampleObservable extends Observable {
    private int data = 0;

    public void setData(int data) {
        this.data = data;
        this.setChanged();
        this.notifyObservers();
    }

    public int getData() {
        return this.data;
    }

}
