package com.smartcity.pi4riego.controller;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.smartcity.pi4riego.constant.Enumerator;
import com.smartcity.pi4riego.entity.ThingComponent;
import com.smartcity.pi4riego.entity.ThingI2C;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by eliasibz on 14/08/16.
 */
public class ThingI2CController {

    private static final int START_ADDRESS = 5;
    private static final int END_ADDRESS = 6;
    private static Object lock1 = new Object();


    private static synchronized I2CBus getI2CBus() throws IOException, I2CFactory.UnsupportedBusNumberException, UnsatisfiedLinkError {
        return I2CFactory.getInstance(I2CBus.BUS_1);
    }

    public static void write(ThingI2C device, String action) throws IOException, I2CFactory.UnsupportedBusNumberException,UnsatisfiedLinkError {
        synchronized (lock1) {
            I2CBus i2c = getI2CBus();
            I2CDevice i2cDevice = i2c.getDevice(device.getAddressNumber());
            String[] msg = action.split(",");
            for (int i = 0; i < msg.length; i++) {
                if ((i + 1) != msg.length) {
                    msg[i] += ",";
                }
                i2cDevice.write(msg[i].getBytes());
            }
        }
    }

    public static String read(ThingI2C device) throws IOException, I2CFactory.UnsupportedBusNumberException, InterruptedException {
        String message = "";
        synchronized (lock1) {
            I2CBus i2c = getI2CBus();
            I2CDevice i2cDevice = i2c.getDevice(device.getAddressNumber());

            int b = i2cDevice.read();
            ApplicationController.getConsole().println(b);

            while (((char) b) != '_') {
                if (b != 0) {
                    message += (char) b;
                }
                Thread.sleep(10);
                b = i2cDevice.read();
            }
        }

        return message;
    }

    public static ArrayList<ThingI2C> discoverThings() throws IOException, UnsatisfiedLinkError, I2CFactory.UnsupportedBusNumberException, InterruptedException {

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
                e.printStackTrace();
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
                        Enumerator.THING_COMPONENT_TYPE type = Enumerator.THING_COMPONENT_TYPE.ACTUATOR;
                        String[] typeSubtype = ((String)json.get(componentName)).split("?");
                        if(Integer.parseInt(typeSubtype[0])  == 1) {
                            type = Enumerator.THING_COMPONENT_TYPE.SENSOR;
                        }

                        thingComponents[y] = new ThingComponent(componentName,
                               type,  typeSubtype[1]);
                    }

                    things.add(new ThingI2C(i, thingComponents, i));

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        return things;
    }

    public static void updateStatus(ThingI2C thing) throws InterruptedException, IOException, I2CFactory.UnsupportedBusNumberException, ParseException {

        String message = read(thing);

        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(message);
        ApplicationController.getConsole().println(json.toJSONString());

        for(int i=0;i<thing.getThingComponents().length;i++){
            ThingComponent thingComponent = thing.getThingComponents()[i];
            String status = (String) json.get(thingComponent.getName());
            thingComponent.setStatus(status);
        }
    }
}
