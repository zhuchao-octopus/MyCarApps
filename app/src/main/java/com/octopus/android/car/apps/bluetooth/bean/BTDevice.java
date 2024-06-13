package com.octopus.android.car.apps.bluetooth.bean;

public class BTDevice {
    //设备名称
    private String deviceName;
    //设备mac地址
    private String deviceMacAddress;
    //是否为配对过的设备
    private boolean pairState;
    private Long time;
    private boolean isConnectMac;
    private boolean showDelete;

    public boolean isShowDelete() {
        return showDelete;
    }

    public void setShowDelete(boolean showDelete) {
        this.showDelete = showDelete;
    }

    public boolean isConnectMac() {
        return isConnectMac;
    }

    public void setConnectMac(boolean connectMac) {
        isConnectMac = connectMac;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceMacAddress() {
        return deviceMacAddress;
    }

    public void setDeviceMacAddress(String deviceMacAddress) {
        this.deviceMacAddress = deviceMacAddress;
    }

    public boolean isPairState() {
        return pairState;
    }

    public void setPairState(boolean pairState) {
        this.pairState = pairState;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
