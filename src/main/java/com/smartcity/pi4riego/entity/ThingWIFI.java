package com.smartcity.pi4riego.entity;

import com.smartcity.pi4riego.constant.Enumerator;

/**
 * Created by eliasibz on 16/08/16.
 */
public class ThingWIFI extends Thing{

    private String ip;

    public ThingWIFI(long id, ThingComponent[] thingComponents, String ip) {
        super(id, thingComponents);
        this.ip = ip;
    }

    public String getIp(){
        return ip;
    }

    @Override
    public Enumerator.THING_TYPE getType() {
        return Enumerator.THING_TYPE.THING_WIFI;
    }
}
