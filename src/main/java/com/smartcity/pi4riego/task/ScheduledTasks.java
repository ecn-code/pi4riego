package com.smartcity.pi4riego.task;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.pi4j.io.i2c.I2CFactory;
import com.smartcity.pi4riego.ApplicationStartup;
import com.smartcity.pi4riego.controller.DeviceI2CController;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by eliasibz on 14/08/16.
 */
@Component
public class ScheduledTasks {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        try {
            ApplicationStartup.getConsole().println(DeviceI2CController.discoverThings());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (I2CFactory.UnsupportedBusNumberException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
