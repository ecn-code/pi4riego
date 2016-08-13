package com.smartcity.pi4riego.controller;

import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by eliasibz on 13/08/16.
 */
public class MotorController {

    @RequestMapping("/")
    public String getStatus(){
        return "Hola";
    }

}
