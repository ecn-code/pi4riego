package com.smartcity.pi4riego.entity;

import com.smartcity.pi4riego.constant.Enumerator;

/**
 * Created by eliasibz on 15/08/16.
 */
public class ThingComponent {

    private final String name;
    private final Enumerator.THING_COMPONENT_TYPE type;
    private final String subType;
    private String status;


    public ThingComponent(String name, Enumerator.THING_COMPONENT_TYPE type, String subType) {
        this.name = name;
        this.type = type;
        this.subType = subType;
    }

    public String getName() {
        return name;
    }

    public Enumerator.THING_COMPONENT_TYPE getType() {
        return type;
    }

    public String getStatus() {
        if(status == null){status = "-1";}
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString(){
        return "Name: "+name+" Type: "+getTypeStr();
    }

    private String getTypeStr(){
        String typeStr = "Actuador";
        if(type == Enumerator.THING_COMPONENT_TYPE.SENSOR){
            typeStr = "Sensor";
        }
        return typeStr;
    }

    public String getTypeInt() {
        String typeStr = "0";
        if(type == Enumerator.THING_COMPONENT_TYPE.SENSOR){
            typeStr = "1";
        }
        return typeStr;
    }

    public String getSubType() {
        return subType;
    }
}
