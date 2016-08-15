package com.smartcity.pi4riego.controller;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.smartcity.pi4riego.entity.DeviceI2C;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by eliasibz on 14/08/16.
 */
public class DeviceI2CController {

    private static final int START_ADDRESS = 5;
    private static final int END_ADDRESS = 5;

    public static void write(DeviceI2C device, String action) throws IOException, I2CFactory.UnsupportedBusNumberException {
        I2CBus i2c = I2CFactory.getInstance(I2CBus.BUS_1);
        I2CDevice i2cDevice = i2c.getDevice(device.getAddressNumber());
        String[] msg = action.split(",");
        for (int i = 0; i < msg.length; i++) {
            if ((i + 1) != msg.length) {
                msg[i] += ",";
            }
            i2cDevice.write(msg[i].getBytes());
        }
    }

    public static ArrayList<DeviceI2C> discoverThings() throws IOException, I2CFactory.UnsupportedBusNumberException, InterruptedException {
        ArrayList<DeviceI2C> things = new ArrayList<DeviceI2C>();
        I2CBus i2c = I2CFactory.getInstance(I2CBus.BUS_1);
        String message = "";
        for (int i = START_ADDRESS; i <= END_ADDRESS; i++) {
            try{

            I2CDevice i2cDevice = i2c.getDevice(i);
            Thread.sleep(10);
            i2cDevice.write("{'res':'info'}".getBytes());


            int b = i2cDevice.read();
            while (((char) b) != '_') {
                if (b != 0) {
                    message += (char) b;
                }
                Thread.sleep(10);
                b = i2cDevice.read();
            }


            //s = new String(buffer, "UTF-8").split("�")[0];
            ApplicationController.getConsole().println(message);

            }catch(Exception e){
                ApplicationController.getConsole().println("En la direccion "+i+" no hay nada.");
            }

            if (message != null) {
                JSONParser parser = new JSONParser();
                try {
                    JSONObject json = (JSONObject) parser.parse(message);
                    ApplicationController.getConsole().println(json.toJSONString());
                    things.add(new DeviceI2C(i, (String[]) json.keySet().toArray(new String[json.keySet().size()]), i));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        return things;
    }

}
