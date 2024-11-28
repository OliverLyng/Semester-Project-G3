package org.example.logic;

import org.example.utils.Nodes;


public class Settings {

    Nodes nodes;

    private float beerType;
    private float beerAmount;
    private float machSpeed;

    public Settings(float beerType, float beerAmount, float machSpeed) {
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

    public float getMachSpeed() {
        return machSpeed;
    }

    public void setMachSpeed(float machSpeed) {
        this.machSpeed = machSpeed;
    }
}
