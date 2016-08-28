package com.smartcity.pi4riego.controller;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by eliasibz on 17/08/16.
 */
public class MQTTController {

    private static String IPBROKER;//"tcp://192.168.1.37:1883";
    private static final int QOS = 2;

    public static void setIPBROKER(String ipBroker) {
        IPBROKER = ipBroker;
        ApplicationController.getConsole().println("MQTTServer: "+ipBroker);
    }

    public static void publish(String topic, String clientId, String content) {

        try {
            MqttClient sampleClient = new MqttClient(IPBROKER, clientId);

            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            ApplicationController.getConsole().println("Connecting to broker: " + IPBROKER);
            sampleClient.connect(connOpts);
            ApplicationController.getConsole().println("Connected");
            ApplicationController.getConsole().println("Publishing message: " + content);

            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(QOS);
            sampleClient.publish(topic, message);
            ApplicationController.getConsole().println("Message published");

            sampleClient.disconnect();
            ApplicationController.getConsole().println("Disconnected");

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}
