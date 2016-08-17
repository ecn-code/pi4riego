package com.smartcity.pi4riego.controller;

import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.util.Console;
import com.smartcity.pi4riego.constant.Enumerator;
import com.smartcity.pi4riego.entity.Thing;
import com.smartcity.pi4riego.entity.ThingComponent;
import com.smartcity.pi4riego.entity.ThingI2C;
import com.smartcity.pi4riego.entity.ThingWIFI;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.context.ApplicationListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by eliasibz on 14/08/16.
 */
@Component
public class ApplicationController
        implements ApplicationListener<ContextRefreshedEvent> {

    private static HashMap<String, Thing> devices;
    private static Set<String> sensors;
    private static final Console console = new Console();

    ApplicationController(){

        devices = new HashMap<String, Thing>();
        sensors = new HashSet<String>();

        //Guardamos el device en la tabla de la PI
        /*String[] deviceComponents = new String[]{"led01","led02"};
        Thing device = new ThingI2C(5, deviceComponents, 5);
        devices.put("led01", device);
        devices.put("led02", device);*/
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {

    }

    public static void addDevice(String key, Thing thing){
        devices.put(key, thing);
    }

    public static void discoverI2CThings() throws UnsatisfiedLinkError{
        try {
            ArrayList<ThingI2C> devices = ThingI2CController.discoverThings();
            ApplicationController.getConsole().println(devices);

            for (int i=0;i<devices.size();i++) {
                Thing thing = devices.get(i);
                for(int y = 0; y< thing.getThingComponents().length; y++){
                    ThingComponent component = thing.getThingComponents()[y];
                    ApplicationController.addDevice(component.getName(), thing);

                    //Si es un sensor, se registra para ser consultado
                    if(component.getType() == Enumerator.THING_COMPONENT_TYPE.SENSOR){
                        sensors.add(component.getName());
                    }
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

    public static void discoverWIFIThings(){
        ArrayList<ThingWIFI> devices = ThingWIFIController.discoverThings();
        ApplicationController.getConsole().println(devices);

        for (int i=0;i<devices.size();i++) {
            Thing thing = devices.get(i);
            for(int y = 0; y< thing.getThingComponents().length; y++){
                ThingComponent component = thing.getThingComponents()[y];
                ApplicationController.addDevice(component.getName(), thing);

                //Si es un sensor, se registra para ser consultado
                if(component.getType() == Enumerator.THING_COMPONENT_TYPE.SENSOR){
                    sensors.add(component.getName());
                }
            }
        }
        ApplicationController.getConsole().separatorLine();
    }

    public static Set<String> getSensors() {
        return sensors;
    }

    public static Thing getDevice(String key){
        return devices.get(key);
    }

    public static Console getConsole(){
        return console;
    }
}