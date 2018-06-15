package com.hz.junxinbaoan.utils.bluetooth;

/**
 * Created by sunkun on 2015/9/10.
 */
public class IBeacon {
    public String name;
    public String proximityUuid;
    public String bluetoothAddress;
    public int major;
    public int minor;
    public int txPower;
    public int rssi;
    public boolean isChoose;

    private String ibeaconId;
    private String ibeaconComment;

    public IBeacon() {
    }

    public IBeacon(int major, String ibeaconId, String ibeaconComment) {
        this.major = major;
        this.ibeaconId = ibeaconId;
        this.ibeaconComment = ibeaconComment;
    }

    public String getIbeaconId() {
        return ibeaconId;
    }

    public void setIbeaconId(String ibeaconId) {
        this.ibeaconId = ibeaconId;
    }

    public String getIbeaconComment() {
        return ibeaconComment;
    }

    public void setIbeaconComment(String ibeaconComment) {
        this.ibeaconComment = ibeaconComment;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }
    //    public void setRssi(int rssi) {
//        this.rssi = rssi;
//    }
//
//    public void setProximityUuid(String proximityUuid) {
//        this.proximityUuid = proximityUuid;
//    }
//
//    public void setBluetoothAddress(String bluetoothAddress) {
//        this.bluetoothAddress = bluetoothAddress;
//    }
//
//    public void setMajor(int major) {
//        this.major = major;
//    }
//
//    public void setMinor(int minor) {
//        this.minor = minor;
//    }
//
//    public void setTxPower(int txPower) {
//        this.txPower = txPower;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }

    public String getName() {
        return name;
    }

    public String getProximityUuid() {
        return proximityUuid;
    }

    public String getBluetoothAddress() {
        return bluetoothAddress;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getTxPower() {
        return txPower;
    }

    public int getRssi() {
        return rssi;
    }
}
