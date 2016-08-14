package com.smartcity.pi4riego.entity;

/**
 * Created by eliasibz on 13/08/16.
 */
public class Device {

    private final long id;
    private String[] deviceComponents;
    private final int addressNumber;

    public Device(long id, String[] deviceComponents, int addressNumber) {
        this.id = id;
        this.deviceComponents = deviceComponents;
        this.addressNumber = addressNumber;
    }

    public long getId() {
        return id;
    }

    public int getAddressNumber() {
        return addressNumber;
    }

    public String[] getDeviceComponents() {
        return deviceComponents;
    }
}
