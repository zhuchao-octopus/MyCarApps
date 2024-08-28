package com.octopus.android.car.apps.bluetooth.bean;

public class PhoneBookBean {

    private String name;
    private String number;
    private long time;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "PhoneBookBean{" + "name='" + name + '\'' + ", number='" + number + '\'' + ", time=" + time + '}';
    }
}
