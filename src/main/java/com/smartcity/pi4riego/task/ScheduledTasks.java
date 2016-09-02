package com.smartcity.pi4riego.task;

import java.io.IOException;
import java.text.SimpleDateFormat;

import com.pi4j.io.i2c.I2CFactory;
import com.smartcity.pi4riego.constant.Enumerator;
import com.smartcity.pi4riego.controller.ApplicationController;
import com.smartcity.pi4riego.controller.MQTTController;
import com.smartcity.pi4riego.controller.ThingI2CController;
import com.smartcity.pi4riego.entity.Thing;
import com.smartcity.pi4riego.entity.ThingComponent;
import com.smartcity.pi4riego.entity.ThingI2C;
import org.json.simple.parser.ParseException;
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
        try {
            ApplicationController.discoverI2CThings();
        }catch (UnsatisfiedLinkError e){
            ApplicationController.getConsole().println("No hay puerto I2C disponible");
        }
        ApplicationController.discoverWIFIThings();
    }

    @Scheduled(fixedRate = 2000)
    public void pullSensors() {

        ApplicationController.getConsole().println("--- Start Pull Sensors--");

        for(int i=0;i<ApplicationController.getDevices().size();i++){
            Thing thing = (Thing) ApplicationController.getDevices().toArray()[i];

            //Segun su tipo de comunicacion solicitamos informacion del sensor
            if(thing.getType() == Enumerator.THING_TYPE.THING_I2C) {
                try {
                    //Actualizar estados
                    ThingI2CController.updateStatus((ThingI2C) thing);

                    for(int j=0;j<thing.getThingComponents().length;j++){
                        ThingComponent thingComponent = thing.getThingComponents()[j];
                        String status = thingComponent.getStatus();

                        //Enviar lectura al broker
                        MQTTController.publish("topic", thingComponent.getName(),
                                "{\"value\":\""+status+"\"," +
                                        "\"name\":\""+thingComponent.getName()+"\"," +
                                        "\"type\":\""+thingComponent.getTypeInt()+"\"," +
                                        "\"subtype\":\""+thingComponent.getSubType()+"\"}");

                        ApplicationController.getConsole().println(status);
                        ApplicationController.getConsole().separatorLine();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (I2CFactory.UnsupportedBusNumberException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else if(thing.getType() == Enumerator.THING_TYPE.THING_WIFI){

            }
        }

        ApplicationController.getConsole().separatorLine();
    }


}
