package com.smartcity.pi4riego;

import com.pi4j.util.Console;
import com.smartcity.pi4riego.entity.Device;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.context.ApplicationListener;

import java.util.HashMap;

/**
 * Created by eliasibz on 14/08/16.
 */
@Component
public class ApplicationStartup
        implements ApplicationListener<ContextRefreshedEvent> {

    private static HashMap<String, Device> devices;
    private static final Console console = new Console();

    ApplicationStartup(){
        console.title("<-- SmartCity Riego-->", "Raspberry PI");

        console.println("ApplicationStartup on!!");
        console.separatorLine();

        devices = new HashMap<String, Device>();

        //Guardamos el device en la tabla de la PI
        devices.put("led01", new Device(5, "led01", 5));
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {

    }

    public static Device getDevice(String key){
        return devices.get(key);
    }

    public static Console getConsole(){
        return console;
    }
}