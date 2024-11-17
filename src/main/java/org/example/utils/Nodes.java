package org.example.utils;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.UaClient;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;

import java.util.concurrent.CompletableFuture;



public class Nodes {

    UaClient client;

    //Admin 
    public NodeId producedCount = new NodeId(6, "::Program:Cube.Admin.ProdProcessedCount"); 
    public NodeId defectCount = new NodeId(6,"::Program:Cube.Admin.ProdDefectiveCount"); 


    //Status
    public NodeId stateCurrent = new NodeId(6, "::Program:Cube.Status.StateCurrent");
    public NodeId machSpeedState = new NodeId(6, "::Program:Cube.Status.MachSpeed");


    //Commands
    public NodeId cntrlCmd = new NodeId(6,"::Program:Cube.Command.CntrlCmd");
    public NodeId cmdChange = new NodeId(6,"::Program:Cube.Command.CmdChangeRequest");
    public NodeId machSpeed = new NodeId(6,"::Program:Cube.Command.MachSpeed");

    //Commands/Parameters
    public NodeId parameter0 = new NodeId(6,"::Program:Cube.Command.Parameter[0].Value");
    public NodeId parameter1 = new NodeId(6,"::Program:Cube.Command.Parameter[1].Value");
    public NodeId parameter2 = new NodeId(6,"::Program:Cube.Command.Parameter[2].Value");

    //Data
    public NodeId temperature = new NodeId(6,"::Program:Data.Value.Temperature");
    public NodeId humidity = new NodeId(6, "::Program:Data.Value.RelHumidity"); 
   
    //Inventory
    public NodeId barley = new NodeId(6,"::Program:Inventory.Barley");
    public NodeId hops = new NodeId(6,"::Program:Inventory.Hops");
    public NodeId malt = new NodeId(6,"::Program:Inventory.Malt");
    public NodeId wheat = new NodeId(6,"::Program:Inventory.Wheat");
    public NodeId yeast = new NodeId(6,"::Program:Inventory.Yeast");

    //Product
    public NodeId produced = new NodeId(6,"::Program:product.produced");
    public NodeId produce_amount = new NodeId(6,"::Program:product.produce_amount");




}
