package org.example.logic;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.UaClient;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.example.utils.Nodes;

public class OpcUaClientExample {

    Nodes node;
    UaClient client;


    public void start(){
        client.writeValue(node.cntrlCmd,DataValue.valueOnly(new Variant(1)));
        client.writeValue(node.cmdChange,DataValue.valueOnly(new Variant(true)));
    }

    public void execute(OpcUaClient client) throws Exception{

        boolean isDone = false;

        //switches speed of the product
        client.writeValue(node.machSpeed,DataValue.valueOnly(new Variant(settings.x)));

        //chooses the type of beer
        client.writeValue(node.parameter1,DataValue.valueOnly(new Variant(settings.x)));

        //chooses the amount of beer
        client.writeValue(node.parameter2,DataValue.valueOnly(new Variant(settings.x)));

        //starts the production
        client.writeValue(node.cntrlCmd,DataValue.valueOnly(new Variant(2)));
        client.writeValue(node.cmdChange,DataValue.valueOnly(new Variant(true)));

        while(isDone==false){

            if(client.readValue(0,null,node.produced).get().getValue()==settings.x){
                isDone=true;
            }
        }

        //resets the machine
        client.writeValue(node.cntrlCmd,DataValue.valueOnly(new Variant(1)));
        client.writeValue(node.cmdChange,DataValue.valueOnly(new Variant(true)));

    }






    public static void main(String[] args) throws Exception {
        String endpointUrl = "opc.tcp://localhost:4840";  // Change to your server's URL

        // Build OPC-UA client
        OpcUaClient client = OpcUaClient.create(endpointUrl);

        // Connect to the server
        CompletableFuture<UaClient> connectFuture = client.connect();

        // Block until connection is established
        connectFuture.get();

        System.out.println("Client connected to server: " + endpointUrl);











        //Examples
        /*
        UaVariableNode node = client.getAddressSpace().getVariableNode(stateCurrent);
        System.out.println(node.getValue());

        //Write values
        client.writeValue(cntrlCmd,DataValue.valueOnly(new Variant(1)));
        client.writeValue(cmdChange,DataValue.valueOnly(new Variant(true)));

        System.out.println(node.getValue());

         */

    }
}
