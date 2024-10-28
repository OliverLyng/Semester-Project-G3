package org.example.utils;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.UaClient;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;

import java.util.concurrent.CompletableFuture;



public class Nodes {

    //Status
    NodeId stateCurrent = new NodeId(6, "::Program:Cube.Status.StateCurrent");
    NodeId machSpeedState = new NodeId(6, "::Program:Cube.Status.MachSpeed");


    //Commands
    NodeId cntrlCmd = new NodeId(6,"::Program:Cube.Command.CntrlCmd");
    NodeId cmdChange = new NodeId(6,"::Program:Cube.Command.CmdChangeRequest");
    NodeId machSpeed = new NodeId(6,"::Program:Cube.Command.MachSpeed");

    //Commands/Parameters
    NodeId parameter0 = new NodeId(6,"::Program:Cube.Command.Parameter[0].Value");
    NodeId parameter1 = new NodeId(6,"::Program:Cube.Command.Parameter[1].Value");
    NodeId parameter2 = new NodeId(6,"::Program:Cube.Command.Parameter[2].Value");

    //Data
    NodeId temperature = new NodeId(6,"::Program:Data.Value.Temperature");

    //Inventory
    NodeId barley = new NodeId(6,"::Program:Inventory.Barley");
    NodeId hops = new NodeId(6,"::Program:Inventory.Hops");
    NodeId malt = new NodeId(6,"::Program:Inventory.Malt");
    NodeId wheat = new NodeId(6,"::Program:Inventory.Wheat");
    NodeId yeast = new NodeId(6,"::Program:Inventory.Yeast");

    //Product
    NodeId produced = new NodeId(6,"::Program:product.produced");
    NodeId produce_amount = new NodeId(6,"::Program:product.produce_amount");




}
