package com.smartcity.pi4riego.controller;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.smartcity.pi4riego.Application;
import com.smartcity.pi4riego.ApplicationStartup;
import com.smartcity.pi4riego.entity.Device;
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
        return ApplicationStartup.getDevice(name);
    }

    @RequestMapping(value = "/{name}",
            method = RequestMethod.PUT,
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<String> action(@PathVariable String name, @RequestBody String action){


        ResponseEntity<String> response;

        //Obtener device segun nombre
        Device device = ApplicationStartup.getDevice(name);

        if(device == null){
            response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }else {
            try {
                I2CBus i2c = I2CFactory.getInstance(I2CBus.BUS_1);
                I2CDevice deviceI2C = i2c.getDevice(DEVICE_ADDR);


                ApplicationStartup.getConsole().println("Service action: ", action);


                //byte[] b = "{'a':1234}".getBytes();
                deviceI2C.write(action.getBytes());
                response = new ResponseEntity<String>(HttpStatus.ACCEPTED);
            }catch (IOException e){
                response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
            }catch (I2CFactory.UnsupportedBusNumberException e){
                response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
            }catch(UnsatisfiedLinkError e){
                ApplicationStartup.getConsole().println("No hay puerto I2C disponible");
                response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
            }

            //String s2 = Arrays.toString(b);
            //console.println(s2);

            //device.read(b, 0, b.length);

            //s2 = Arrays.toString(b);
            //console.println(s2);

        }
        ApplicationStartup.getConsole().separatorLine();
        return response;
    }

}
