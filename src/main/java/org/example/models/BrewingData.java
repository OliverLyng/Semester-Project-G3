package org.example.models;

public class BrewingData {
    private double temperature;
    private double vibrations;
    // Add other fields as needed

    // Constructor, getters, and setters
    public BrewingData(double temperature, double vibrations) {
        this.temperature = temperature;
        this.vibrations = vibrations;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getVibrations() {
        return vibrations;
    }

    public void setVibrations(double vibrations) {
        this.vibrations = vibrations;
    }
}
