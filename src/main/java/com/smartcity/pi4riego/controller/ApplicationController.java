package com.smartcity.pi4riego.controller;

import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.util.Console;
import com.smartcity.pi4riego.controller.DeviceI2CController;
import com.smartcity.pi4riego.entity.Device;
import com.smartcity.pi4riego.entity.DeviceI2C;
import org.json.simple.parser.ParseException;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.context.ApplicationListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by eliasibz on 14/08/16.
 */
@Component
public class ApplicationController
        implements ApplicationListener<ContextRefreshedEvent> {

    private static HashMap<String, Device> devices;
    private static final Console console = new Console();

    ApplicationController(){

        devices = new HashMap<String, Device>();

        //Guardamos el device en la tabla de la PI
        /*String[] deviceComponents = new String[]{"led01","led02"};
        Device device = new DeviceI2C(5, deviceComponents, 5);
        devices.put("led01", device);
        devices.put("led02", device);*/
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {

    }

    public static void addDevice(String key, Device device){
        devices.put(key, device);
    }

    public static void discoverI2CDevices(){
        try {
            ArrayList<DeviceI2C> devices = DeviceI2CController.discoverThings();
            ApplicationController.getConsole().println(devices);

            for (int i=0;i<devices.size();i++) {
                Device device = devices.get(i);
                for(int y=0;y<device.getDeviceComponents().length;y++){
                    String component = device.getDeviceComponents()[y];
                    ApplicationController.addDevice(component, device);
                }
            }
            ApplicationController.getConsole().separatorLine();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (I2CFactory.UnsupportedBusNumberException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static Device getDevice(String key){
        return devices.get(key);
    }

    public static Console getConsole(){
        return console;
    }
}