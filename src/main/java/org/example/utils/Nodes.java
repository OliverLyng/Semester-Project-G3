package org.example.utils;

import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;



public class Nodes {

    //Status
    public static final NodeId stateCurrent = new NodeId(6, "::Program:Cube.Status.StateCurrent");
    public static final NodeId statusMachSpeedState = new NodeId(6, "::Program:Cube.Status.MachSpeed");


    //Commands
    public static final NodeId cntrlCmd = new NodeId(6,"::Program:Cube.Command.CntrlCmd");
    public static final NodeId cmdChange = new NodeId(6,"::Program:Cube.Command.CmdChangeRequest");
    public static final NodeId cmdMachSpeed = new NodeId(6,"::Program:Cube.Command.MachSpeed");

    //Commands/Parameters
    public static final NodeId cmdBatchId = new NodeId(6,"::Program:Cube.Command.Parameter[0].Value");
    public static final NodeId cmdBeerType = new NodeId(6,"::Program:Cube.Command.Parameter[1].Value");
    public static final NodeId cmdAmountOfBeer = new NodeId(6,"::Program:Cube.Command.Parameter[2].Value");

    //Data
    public static final NodeId temperature = new NodeId(6,"::Program:Data.Value.Temperature");

    //Inventory
    public static final NodeId barley = new NodeId(6,"::Program:Inventory.Barley");
    public static final NodeId hops = new NodeId(6,"::Program:Inventory.Hops");
    public static final NodeId malt = new NodeId(6,"::Program:Inventory.Malt");
    public static final NodeId wheat = new NodeId(6,"::Program:Inventory.Wheat");
    public static final NodeId yeast = new NodeId(6,"::Program:Inventory.Yeast");

    //Product
    public static final NodeId produced = new NodeId(6,"::Program:product.produced");
    public static final NodeId produce_amount = new NodeId(6,"::Program:product.produce_amount");

    //Status/Parameters
    public static final NodeId statusBatchId = new NodeId(6,"::Program:Cube.Status.Parameter[0].Value");
    public static final NodeId statusAmount = new NodeId(6,"::Program:Cube.Status.Parameter[1].Value");
    public static final NodeId statusRelativeHumidity = new NodeId(6,"::Program:Cube.Status.Parameter[2].Value");
    public static final NodeId statusTemperature = new NodeId(6,"::Program:Cube.Status.Parameter[3].Value");
    public static final NodeId statusVibration = new NodeId(6,"::Program:Cube.Status.Parameter[4].Value");


    //Admin
    public static final NodeId admBatchId = new NodeId(6, "::Program:Cube.Admin.Parameter[0]");
    public static final NodeId prodDefectiveCount = new NodeId(6, "::Program:Cube.Admin.ProdDefectiveCount");
    public static final NodeId prodProcessedCount = new NodeId(6, "::Program:Cube.Admin.ProdProcessedCount");
    public static final NodeId stopReason = new NodeId(6,"::Program:Cube.Admin.StopReason.ID");
    public static final NodeId stopReasonValue = new NodeId(6,"::Program:Cube.Admin.StopReason.Value");

}
