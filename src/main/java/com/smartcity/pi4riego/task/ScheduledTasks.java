package com.smartcity.pi4riego.task;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.smartcity.pi4riego.ApplicationStartup;
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
        //ApplicationStartup.getDevice("led01");
    }

}
