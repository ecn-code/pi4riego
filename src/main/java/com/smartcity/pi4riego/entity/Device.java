package com.smartcity.pi4riego.entity;

import java.io.IOException;

/**
 * Created by eliasibz on 13/08/16.
 */
public abstract class Device {

    private final long id;
    private String[] deviceComponents;

    public Device(long id, String[] deviceComponents) {
        this.id = id;
        this.deviceComponents = deviceComponents;
    }

    public long getId() {
        return id;
    }


    public String[] getDeviceComponents() {
        return deviceComponents;
    }

    public abstract String getType();

}
