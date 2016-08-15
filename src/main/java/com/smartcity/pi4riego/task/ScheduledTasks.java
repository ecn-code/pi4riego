package com.smartcity.pi4riego.task;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.pi4j.io.i2c.I2CFactory;
import com.smartcity.pi4riego.controller.ApplicationController;
import com.smartcity.pi4riego.controller.DeviceI2CController;
import com.smartcity.pi4riego.entity.Device;
import com.smartcity.pi4riego.entity.DeviceI2C;
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
    public void reportCurrentTime() {
        ApplicationController.discoverI2CDevices();
    }

}
