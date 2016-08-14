package com.smartcity.pi4riego.controller;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.smartcity.pi4riego.entity.DeviceI2C;

import java.io.IOException;

/**
 * Created by eliasibz on 14/08/16.
 */
public class DeviceI2CController {

    public static void write(DeviceI2C device, String action) throws IOException, I2CFactory.UnsupportedBusNumberException {
        I2CBus i2c = I2CFactory.getInstance(I2CBus.BUS_1);
        I2CDevice i2cDevice = i2c.getDevice(device.getAddressNumber());

        i2cDevice.write(action.getBytes());
    }

}
