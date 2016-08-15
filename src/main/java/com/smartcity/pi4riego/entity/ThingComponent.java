package com.smartcity.pi4riego.entity;

/**
 * Created by eliasibz on 15/08/16.
 */
public class ThingComponent {

    private final String name;
    private final int type;


    public ThingComponent(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    @Override
    public String toString(){
        return "Name: "+name+" Type: "+getTypeStr();
    }

    private String getTypeStr(){
        String typeStr = "Actuador";
        if(type == 1){
            typeStr = "Sensor";
        }
        return typeStr;
    }
}
