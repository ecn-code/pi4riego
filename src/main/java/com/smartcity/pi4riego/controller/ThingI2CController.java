package com.smartcity.pi4riego.controller;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.smartcity.pi4riego.entity.ThingComponent;
import com.smartcity.pi4riego.entity.ThingI2C;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by eliasibz on 14/08/16.
 */
public class ThingI2CController {

    private static final int START_ADDRESS = 5;
    private static final int END_ADDRESS = 5;

    public static void write(ThingI2C device, String action) throws IOException, I2CFactory.UnsupportedBusNumberException {
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

    public static String read(ThingI2C device) throws IOException, I2CFactory.UnsupportedBusNumberException, InterruptedException {
        I2CBus i2c = I2CFactory.getInstance(I2CBus.BUS_1);
        I2CDevice i2cDevice = i2c.getDevice(device.getAddressNumber());
        String message = "";
        int b = i2cDevice.read();
        while (((char) b) != '_') {
            if (b != 0) {
                message += (char) b;
            }
            Thread.sleep(10);
            b = i2cDevice.read();
        }

        return message;
    }

    public static ArrayList<ThingI2C> discoverThings() throws IOException, I2CFactory.UnsupportedBusNumberException, InterruptedException {
        ArrayList<ThingI2C> things = new ArrayList<ThingI2C>();
        String message = "";
        for (int i = START_ADDRESS; i <= END_ADDRESS; i++) {
            try {

                //Enviamos una solicitud de informacion a una direccion sin saber si estara disponible
                Thread.sleep(10);
                write(new ThingI2C(-1, null, i), "{'res':'info'}");

                //Si va bien, leemos la informacion del dispositivo
                message = read(new ThingI2C(-1, null, i));

                //s = new String(buffer, "UTF-8").split("�")[0];
                ApplicationController.getConsole().println(message);

            } catch (Exception e) {
                ApplicationController.getConsole().println("En la direccion " + i + " no hay nada.");
            }

            if (message != null) {
                JSONParser parser = new JSONParser();
                try {
                    JSONObject json = (JSONObject) parser.parse(message);
                    ApplicationController.getConsole().println(json.toJSONString());
                    ThingComponent[] thingComponents = new ThingComponent[json.keySet().size()];

                    //Se instancian los componentes de la thing
                    for (int y=0;y<json.keySet().size();y++) {
                        String componentName = (String)json.keySet().toArray()[y];
                        thingComponents[y] = new ThingComponent(componentName,
                                Integer.parseInt((String)json.get(componentName)));
                    }

                    things.add(new ThingI2C(i, thingComponents, i));

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        return things;
    }

}
