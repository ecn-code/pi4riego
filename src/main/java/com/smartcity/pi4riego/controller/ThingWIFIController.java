package com.smartcity.pi4riego.controller;

import com.pi4j.io.i2c.I2CFactory;
import com.smartcity.pi4riego.constant.Enumerator;
import com.smartcity.pi4riego.entity.ThingComponent;
import com.smartcity.pi4riego.entity.ThingI2C;
import com.smartcity.pi4riego.entity.ThingWIFI;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by eliasibz on 16/08/16.
 */
public class ThingWIFIController {

    private static String[] ips = new String[]{"http://192.168.43.46", "http://192.168.1.38"};

    public static ArrayList<ThingWIFI> discoverThings() {

        ApplicationController.getConsole().separatorLine();

        ArrayList<ThingWIFI> things = new ArrayList<ThingWIFI>();

        String response = null;
        String resourceUrl = null;

        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(1000);
        httpRequestFactory.setConnectTimeout(1000);
        httpRequestFactory.setReadTimeout(1000);

        ApplicationController.getConsole().println("Buscando dispositivos WIFI: ");

        for (int i = 0; i < ips.length; i++) {
            try {
                RestTemplate restTemplate = new RestTemplate(httpRequestFactory);

                resourceUrl = ips[i];
                HttpEntity<String> request = new HttpEntity(new String());
                response = null;
                response =
                        restTemplate.getForObject(resourceUrl + "/info", String.class);

                ApplicationController.getConsole().println(resourceUrl);


            } catch (Exception e) {
                //e.printStackTrace();
                ApplicationController.getConsole().println("Dispostivo "+resourceUrl+" no disponible.");
            }

            if (response != null && response !="") {
                JSONParser parser = new JSONParser();
                try {
                    JSONObject json = (JSONObject) parser.parse(response);
                    ThingComponent[] thingComponents = new ThingComponent[json.keySet().size()];
                    ApplicationController.getConsole().println(response);


                    //Se instancian los componentes de la thing
                    for (int y = 0; y < json.keySet().size(); y++) {
                        String componentName = (String) json.keySet().toArray()[y];
                        Enumerator.THING_COMPONENT_TYPE type = Enumerator.THING_COMPONENT_TYPE.ACTUATOR;
                        String[] typeSubtype = ((String) json.get(componentName)).split("-");
                        if(Integer.parseInt(typeSubtype[0])  == 1) {
                            type = Enumerator.THING_COMPONENT_TYPE.SENSOR;
                        }
                        thingComponents[y] = new ThingComponent(componentName,
                                type, typeSubtype[1]);
                    }

                    things.add(new ThingWIFI(i, thingComponents, resourceUrl));

                } catch (ParseException e) {
                    //e.printStackTrace();
                    ApplicationController.getConsole().println("Error de parseo: "+response);
                }
            }
            ApplicationController.getConsole().separatorLine();
        }

        return things;
    }

    public static void write(ThingWIFI thingWIFI, String action){
        try {
            RestTemplate restTemplate = new RestTemplate();
            String resourceUrl = thingWIFI.getIp();

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(action);

            MultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>();
            request.add("res", (String) json.get("res"));
            request.add("action", (String) json.get("action"));
            String response =
                    restTemplate.postForObject(resourceUrl + "/",request, String.class);
            ApplicationController.getConsole().println(resourceUrl);
            ApplicationController.getConsole().println(response);
            ApplicationController.getConsole().separatorLine();

        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public static void updateStatus(ThingWIFI thingWIFI) throws InterruptedException, IOException, I2CFactory.UnsupportedBusNumberException {

        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = thingWIFI.getIp();
        String message =
                restTemplate.getForObject(resourceUrl + "/" + "status", String.class);

        JSONParser parser = new JSONParser();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(message);
        } catch (ParseException e) {
            ApplicationController.getConsole().println("Error parseo: "+message);
        }
        ApplicationController.getConsole().println(json.toJSONString());

        for(int i=0;i<thingWIFI.getThingComponents().length;i++){
            ThingComponent thingComponent = thingWIFI.getThingComponents()[i];
            String status = (String) json.get(thingComponent.getName());
            thingComponent.setStatus(status);
        }
    }

    public static void setIPs(String[] IPs) {
        ips = IPs;
    }
}
