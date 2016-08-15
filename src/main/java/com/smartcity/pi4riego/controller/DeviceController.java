package com.smartcity.pi4riego.controller;

import com.pi4j.io.i2c.I2CFactory;
import com.smartcity.pi4riego.entity.Device;
import com.smartcity.pi4riego.entity.DeviceI2C;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;

/**
 * Created by eliasibz on 13/08/16.
 */
@RestController
@RequestMapping("/thing")
public class DeviceController {

    public static final int DEVICE_ADDR = 0x05;

    @RequestMapping(value = "/{name}", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    Device get(@PathVariable String name) throws IOException, I2CFactory.UnsupportedBusNumberException {
        return ApplicationController.getDevice(name);
    }

    @RequestMapping(value = "/{name}",
            method = RequestMethod.PUT,
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<String> action(@PathVariable String name, @RequestBody String action) {


        ResponseEntity<String> response;

        //Obtener device segun nombre
        Device device = ApplicationController.getDevice(name);

        if (device == null) {
            response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        } else {
            try {
                ApplicationController.getConsole().println("Service action: " + action);

                action = action.replace("}", ", 'res':'" + name + "'}");

                if (device.getType().equals("DeviceI2C")) {
                    DeviceI2CController.write((DeviceI2C) device, action);

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

            //device.read(b, 0, b.length);

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
        ApplicationController.discoverI2CDevices();
        return new ResponseEntity<String>(HttpStatus.ACCEPTED);
    }

}
