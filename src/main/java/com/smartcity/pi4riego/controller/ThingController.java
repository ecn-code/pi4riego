package com.smartcity.pi4riego.controller;

import com.pi4j.io.i2c.I2CFactory;
import com.smartcity.pi4riego.constant.Enumerator;
import com.smartcity.pi4riego.entity.Thing;
import com.smartcity.pi4riego.entity.ThingI2C;
import com.smartcity.pi4riego.entity.ThingWIFI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;

/**
 * Created by eliasibz on 13/08/16.
 */
@RestController
@RequestMapping("/thing")
public class ThingController {

    public static final int DEVICE_ADDR = 0x05;

    @RequestMapping(value = "/{name}", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    Thing get(@PathVariable String name) throws IOException, I2CFactory.UnsupportedBusNumberException {
        return ApplicationController.getDevice(name);
    }

    @RequestMapping(value = "/{name}",
            method = RequestMethod.PUT,
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<String> action(@PathVariable String name, @RequestBody String action) {


        ResponseEntity<String> response;

        //Obtener thing segun nombre
        Thing thing = ApplicationController.getDevice(name);

        if (thing == null) {
            response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        } else {
            try {

                action = action.replace("}", ", \"res\":\"" + name + "\"}");
                ApplicationController.getConsole().println("Service action: " + action);

                if (thing.getType() == Enumerator.THING_TYPE.THING_I2C) {
                    try {
                        ThingI2CController.write((ThingI2C) thing, action);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else if(thing.getType() == Enumerator.THING_TYPE.THING_WIFI){
                    ThingWIFIController.write((ThingWIFI) thing, action);
                }

                response = new ResponseEntity<String>(HttpStatus.ACCEPTED);
            } catch (IOException e) {
                response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
            } catch (I2CFactory.UnsupportedBusNumberException e) {
                response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
            } catch (UnsatisfiedLinkError e) {
                ApplicationController.getConsole().println("No hay puerto I2C disponible");
                response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
            }

            //String s2 = Arrays.toString(b);
            //console.println(s2);

            //thing.read(b, 0, b.length);

            //s2 = Arrays.toString(b);
            //console.println(s2);

        }
        ApplicationController.getConsole().separatorLine();
        return response;
    }

    @RequestMapping(value = "/discover",
            method = RequestMethod.PUT,
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<String> discover() throws InterruptedException {
        ApplicationController.discoverI2CThings();
        return new ResponseEntity<String>(HttpStatus.ACCEPTED);
    }

}
