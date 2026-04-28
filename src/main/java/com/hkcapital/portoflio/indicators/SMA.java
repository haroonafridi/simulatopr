package com.hkcapital.portoflio.indicators;
import java.util.LinkedList;
import java.util.Queue;

public class SMA {

    private final int period;
    private final Queue<Double> window = new LinkedList<>();
    private double sum = 0.0;

    public SMA(int period) {
        this.period = period;
    }

    public Double onPrice(double price) {

        window.add(price);
        sum += price;

        if (window.size() > period) {
            sum -= window.poll();
        }

        if (window.size() < period) {
            return null;
        }

        return sum / period;
    }
}