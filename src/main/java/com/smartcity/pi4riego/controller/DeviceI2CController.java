package com.smartcity.pi4riego.controller;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.smartcity.pi4riego.ApplicationStartup;
import com.smartcity.pi4riego.entity.DeviceI2C;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by eliasibz on 14/08/16.
 */
public class DeviceI2CController {

    private static final int START_ADDRESS = 5;
    private static final int END_ADDRESS = 5;

    public static void write(DeviceI2C device, String action) throws IOException, I2CFactory.UnsupportedBusNumberException {
        I2CBus i2c = I2CFactory.getInstance(I2CBus.BUS_1);
        I2CDevice i2cDevice = i2c.getDevice(device.getAddressNumber());

        i2cDevice.write(action.getBytes());
    }

    public static ArrayList<Integer> discoverThings() throws IOException, I2CFactory.UnsupportedBusNumberException, InterruptedException {
        ArrayList<Integer> things = new ArrayList<Integer>();
        I2CBus i2c = I2CFactory.getInstance(I2CBus.BUS_1);
        String s = null;
        for(int i=START_ADDRESS;i<=END_ADDRESS;i++){
           // try{

                I2CDevice i2cDevice = i2c.getDevice(i);
                i2cDevice.write("{'res':'info'}".getBytes());

                String message = "";
                int b = i2cDevice.read();
                while(((char)b) != '_'){
                    if(b != 0) {
                        message += (char) b;
                    }
                    Thread.sleep(10);
                    b = i2cDevice.read();
                }



                //s = new String(buffer, "UTF-8").split("�")[0];
                ApplicationStartup.getConsole().println(message);
                things.add(i);

            /*}catch(Exception e){
                ApplicationStartup.getConsole().println("En la direccion "+i+" no hay nada.");
            }*/

            if(s != null) {
                JSONParser parser = new JSONParser();
                try {
                    JSONObject json = (JSONObject) parser.parse(s);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        return things;
    }

}
