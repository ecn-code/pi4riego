package com.smartcity.pi4riego;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by eliasibz on 13/08/16.
 *
 * Clase utilizada para arrancar el proyector
 */
@SpringBootApplication
@EnableScheduling
public class Application {

    public static void main(String[] args){

        SpringApplication.run(Application.class, args);

    }

}
