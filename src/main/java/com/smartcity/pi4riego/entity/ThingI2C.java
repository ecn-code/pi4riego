package com.smartcity.pi4riego.entity;

/**
 * Created by eliasibz on 14/08/16.
 */
public class ThingI2C extends Thing {

    private final int addressNumber;

    public ThingI2C(long id, ThingComponent[] thingComponents, int addressNumber) {
        super(id, thingComponents);
        this.addressNumber = addressNumber;
    }

    public int getAddressNumber() {
        return addressNumber;
    }

    @Override
    public int getType() {
        return 0;
    }
}
