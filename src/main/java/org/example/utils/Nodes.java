package org.example.utils;

import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;



public class Nodes {

    //Status
    public static final NodeId stateCurrent = new NodeId(6, "::Program:Cube.Status.StateCurrent");
    public static final NodeId machSpeedState = new NodeId(6, "::Program:Cube.Status.MachSpeed");


    //Commands
    public static final NodeId cntrlCmd = new NodeId(6,"::Program:Cube.Command.CntrlCmd");
    public static final NodeId cmdChange = new NodeId(6,"::Program:Cube.Command.CmdChangeRequest");
    public static final NodeId machSpeed = new NodeId(6,"::Program:Cube.Command.MachSpeed");

    //Commands/Parameters
    public static final NodeId parameter0 = new NodeId(6,"::Program:Cube.Command.Parameter[0].Value");
    public static final NodeId parameter1 = new NodeId(6,"::Program:Cube.Command.Parameter[1].Value");
    public static final NodeId parameter2 = new NodeId(6,"::Program:Cube.Command.Parameter[2].Value");

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

    //Batch Id
    public static final NodeId statusParameter0 = new NodeId(6,"::Program:Cube.Status.Parameter[0].Value");
    public static final NodeId statusParameter1 = new NodeId(6,"::Program:Cube.Status.Parameter[1].Value");
    public static final NodeId statusParameter2 = new NodeId(6,"::Program:Cube.Status.Parameter[2].Value");



}
