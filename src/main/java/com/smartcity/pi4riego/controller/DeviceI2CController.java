package com.smartcity.pi4riego.controller;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.smartcity.pi4riego.ApplicationStartup;
import com.smartcity.pi4riego.entity.DeviceI2C;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by eliasibz on 14/08/16.
 */
public class DeviceI2CController {

    private static final int START_ADDRESS = 5;
    private static final int END_ADDRESS = 6;

    public static void write(DeviceI2C device, String action) throws IOException, I2CFactory.UnsupportedBusNumberException {
        I2CBus i2c = I2CFactory.getInstance(I2CBus.BUS_1);
        I2CDevice i2cDevice = i2c.getDevice(device.getAddressNumber());

        i2cDevice.write(action.getBytes());
    }

    public static ArrayList<Integer> discoverThings() throws IOException, I2CFactory.UnsupportedBusNumberException {
        ArrayList<Integer> things = new ArrayList<Integer>();
        I2CBus i2c = I2CFactory.getInstance(I2CBus.BUS_1);
        for(int i=START_ADDRESS;i<=END_ADDRESS;i++){
            try{

                I2CDevice i2cDevice = i2c.getDevice(i);
                i2cDevice.write("{'res':'info'}".getBytes());

                byte[] buffer = new byte[5];
                i2cDevice.read(buffer, 0, 5);
                String s = new String(buffer);
                ApplicationStartup.getConsole().println(s);

                things.add(i);

            }catch(Exception e){
                ApplicationStartup.getConsole().println("En la direccion "+i+" no hay nada.");
            }
        }

        return things;
    }

}
