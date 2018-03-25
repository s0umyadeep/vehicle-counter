package com.survey.vehicle.presenter;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractPresenter implements Presenter {
    protected static long oneHourMili = 1000 * 60 * 60;
    private static Map<Integer, String> dayStr = new HashMap<>();

    static {
        dayStr.put(1, "Mon");
        dayStr.put(2, "Tue");
        dayStr.put(3, "Wed");
        dayStr.put(4, "Thu");
        dayStr.put(5, "Fri");
    }

    private PrintStream out = System.out;

    public PrintStream getOut() {
        return out;
    }

    public void setOut(PrintStream out) {
        this.out = out;
    }

    public String getDayStr(Integer i) {
        return dayStr.get(i);
    }
}
