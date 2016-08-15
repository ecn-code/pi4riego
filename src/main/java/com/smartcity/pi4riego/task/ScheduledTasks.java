package com.smartcity.pi4riego.task;

import java.io.IOException;
import java.text.SimpleDateFormat;

import com.pi4j.io.i2c.I2CFactory;
import com.smartcity.pi4riego.controller.ApplicationController;
import com.smartcity.pi4riego.controller.ThingI2CController;
import com.smartcity.pi4riego.entity.Thing;
import com.smartcity.pi4riego.entity.ThingI2C;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by eliasibz on 14/08/16.
 */
@Component
public class ScheduledTasks {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    ScheduledTasks(){
        ApplicationController.getConsole().title("<-- SmartCity Riego-->", "Raspberry PI");


        ApplicationController.getConsole().separatorLine();
    }

    @Scheduled(fixedRate = 15000)
    public void discoverDevices() {
        ApplicationController.discoverI2CDevices();
    }

    @Scheduled(fixedRate = 2000)
    public void pullSensors() {

        ApplicationController.getConsole().println("--- Start Pull Sensors--");

        for(int i=0;i<ApplicationController.getSensors().size();i++){
            Thing thing = ApplicationController.getDevice((String)ApplicationController.getSensors().toArray()[i]);
            if(thing.getType() == 0) {
                try {
                    //Solicitamos informacion
                    String message = ThingI2CController.read((ThingI2C) thing);

                    ApplicationController.getConsole().println(message);
                    ApplicationController.getConsole().separatorLine();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (I2CFactory.UnsupportedBusNumberException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        ApplicationController.getConsole().separatorLine();
    }



}
