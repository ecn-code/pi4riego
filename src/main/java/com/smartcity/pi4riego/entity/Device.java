package com.smartcity.pi4riego.entity;

/**
 * Created by eliasibz on 13/08/16.
 */
public class Device {

    private final long id;
    private final String name;
    private final int addressNumber;

    public Device(long id, String name, int addressNumber) {
        this.id = id;
        this.name = name;
        this.addressNumber = addressNumber;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAddressNumber() {
        return addressNumber;
    }
}
