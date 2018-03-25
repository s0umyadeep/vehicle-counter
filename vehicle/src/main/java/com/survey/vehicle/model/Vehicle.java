package com.survey.vehicle.model;

public class Vehicle {

    private int day;
    private long frontAxleTime;
    private long backAxleTime;
    private Direction direction;
    private int fifteenMinsGroup;
    private int twentyMinsGroup;
    private int thirtyMinsGroup;
    private int oneHourGroup;
    private int twelveHourGroup;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public long getFrontAxleTime() {
        return frontAxleTime;
    }

    public void setFrontAxleTime(long frontAxleTime) {
        this.frontAxleTime = frontAxleTime;
    }

    public long getBackAxleTime() {
        return backAxleTime;
    }

    public void setBackAxleTime(long backAxleTime) {
        this.backAxleTime = backAxleTime;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getFifteenMinsGroup() {
        return fifteenMinsGroup;
    }

    public void setFifteenMinsGroup(int fifteenMinsGroup) {
        this.fifteenMinsGroup = fifteenMinsGroup;
    }

    public int getTwentyMinsGroup() {
        return twentyMinsGroup;
    }

    public void setTwentyMinsGroup(int twentyMinsGroup) {
        this.twentyMinsGroup = twentyMinsGroup;
    }

    public int getThirtyMinsGroup() {
        return thirtyMinsGroup;
    }

    public void setThirtyMinsGroup(int thirtyMinsGroup) {
        this.thirtyMinsGroup = thirtyMinsGroup;
    }

    public int getOneHourGroup() {
        return oneHourGroup;
    }

    public void setOneHourGroup(int oneHourGroup) {
        this.oneHourGroup = oneHourGroup;
    }

    public int getTwelveHourGroup() {
        return twelveHourGroup;
    }

    public void setTwelveHourGroup(int twelveHourGroup) {
        this.twelveHourGroup = twelveHourGroup;
    }
}
