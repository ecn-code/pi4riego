package com.smartcity.pi4riego.entity;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

/**
 * Created by eliasibz on 14/08/16.
 */
public class DeviceI2C extends Device{

    private final int addressNumber;

    public DeviceI2C(long id, String[] deviceComponents,int addressNumber) {
        super(id, deviceComponents);
        this.addressNumber = addressNumber;
    }

    public int getAddressNumber() {
        return addressNumber;
    }

    @Override
    public String getType() {
        return "DeviceI2C";
    }
}
