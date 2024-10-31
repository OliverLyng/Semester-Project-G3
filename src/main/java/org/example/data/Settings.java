package org.example.data;

import org.example.utils.Nodes;

public class Settings {

    Nodes nodes;

    private float beerType;
    private float beerAmount;
    private int machSpeed;

    public Settings(float beerType, float beerAmount, int machSpeed) {
        this.beerType = beerType;
        this.beerAmount = beerAmount;
        this.machSpeed = machSpeed;
    }

    public float getBeerType() {
        return beerType;
    }

    public void setBeerType(float beerType) {
        this.beerType = beerType;
    }

    public float getBeerAmount() {
        return beerAmount;
    }

    public void setBeerAmount(float beerAmount) {
        this.beerAmount = beerAmount;
    }

    public int getMachSpeed() {
        return machSpeed;
    }

    public void setMachSpeed(int machSpeed) {
        this.machSpeed = machSpeed;
    }
}
