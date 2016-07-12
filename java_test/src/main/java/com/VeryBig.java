package com;

/**
 * Created by Administrator on 2016/7/12.
 */
public class VeryBig {


    private static final int SIZE = 1000;
    private double[] la = new double[SIZE];
    private String ident;

    public VeryBig(String id) {
        ident = id;
    }

    public VeryBig(String id, int s) {
        ident = id;
        la = new double[s];
    }

    public String toString() {
        return ident;
    }

    protected void finalize() {
        System.out.println("Finalizing " + ident);
    }
}
