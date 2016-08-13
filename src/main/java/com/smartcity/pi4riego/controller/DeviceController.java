package com.smartcity.pi4riego.controller;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.util.Console;
import com.smartcity.pi4riego.entity.Device;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Created by eliasibz on 13/08/16.
 */
@RestController
@RequestMapping("/device")
public class DeviceController {

    public static final int DEVICE_ADDR = 0x05;

    @RequestMapping(value="/{id}", method= RequestMethod.GET, produces = "application/json")
    public @ResponseBody Device getStatus(@PathVariable int id) throws IOException, I2CFactory.UnsupportedBusNumberException {

        final Console console = new Console();
        console.title("<-- The Pi4J Project -->", "I2C Example");

        console.promptForExit();

        I2CBus i2c = I2CFactory.getInstance(I2CBus.BUS_1);

        I2CDevice device = i2c.getDevice(DEVICE_ADDR);

        console.println("... reading ID register from TSL2561");
        byte[] b = "Hola Mundo".getBytes();
        device.write(b);

        return new Device(id);
    }

}
