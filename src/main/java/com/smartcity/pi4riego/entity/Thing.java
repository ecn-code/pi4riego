package com.smartcity.pi4riego.entity;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by eliasibz on 13/08/16.
 */
public abstract class Thing {

    private final long id;
    private ThingComponent[] thingComponents;

    public Thing(long id, ThingComponent[] thingComponents) {
        this.id = id;
        this.thingComponents = thingComponents;
    }

    public long getId() {
        return id;
    }


    public ThingComponent[] getThingComponents() {
        return thingComponents;
    }

    public abstract int getType();

    @Override
    public String toString(){

        return "Thing id: "+id+ ", components: " + Arrays.toString(thingComponents);
    }

}
