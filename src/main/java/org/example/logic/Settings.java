package org.example.logic;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.UaClient;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.example.utils.Nodes;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
