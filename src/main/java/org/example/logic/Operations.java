package org.example.logic;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.UaClient;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;

import java.util.concurrent.CompletableFuture;

import org.example.data.Settings;
import org.example.utils.Nodes;

public class Operations {

    Nodes node;

    Settings settings;
    Operations operator;

    public Operations(){
        this.node = new Nodes();
    }

    public void start(UaClient client) throws Exception{
        client.writeValue(node.cntrlCmd,DataValue.valueOnly(new Variant(1)));
        client.writeValue(node.cmdChange,DataValue.valueOnly(new Variant(true)));
    }

    public void stop(UaClient client) throws Exception{

        while(true){
            UaVariableNode stateNode = client.getAddressSpace().getVariableNode(node.stateCurrent);
            String var = stateNode.getValue().getValue().getValue().toString();
            System.out.println(var);
            //System.out.println("Running outside of if statement");
            //System.out.println(variableNode.getValue().getValue());
            if(var.equals("17")){
                System.out.println("Production finished");
                break;

            }
        }

        client.writeValue(node.cntrlCmd,DataValue.valueOnly(new Variant(3)));
        UaVariableNode stateNode = client.getAddressSpace().getVariableNode(node.stateCurrent);
        String var = stateNode.getValue().getValue().getValue().toString();
        System.out.println(var);
        client.writeValue(node.cmdChange,DataValue.valueOnly(new Variant(true)));
    }

    public void execute(OpcUaClient client) throws Exception{

        //resets the machine

        client.writeValue(node.cntrlCmd,DataValue.valueOnly(new Variant(1)));
        client.writeValue(node.cmdChange,DataValue.valueOnly(new Variant(true)));

        //starts the production
        client.writeValue(node.cntrlCmd,DataValue.valueOnly(new Variant(2)));
        client.writeValue(node.cmdChange,DataValue.valueOnly(new Variant(true)));




    }


    public void loadSettings(OpcUaClient client) throws Exception{

        settings = new Settings(0,1,300);

        //chooses the type of beer
        client.writeValue(node.parameter1,DataValue.valueOnly(new Variant(settings.getBeerType())));

        //chooses the amount of beer
        client.writeValue(node.parameter2,DataValue.valueOnly(new Variant(settings.getBeerAmount())));

        //switches speed of the product
        client.writeValue(node.machSpeed,DataValue.valueOnly(new Variant(settings.getMachSpeed())));


        client.writeValue(node.parameter0,DataValue.valueOnly(new Variant(2)));

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


        Operations operator = new Operations();
        operator.node = new Nodes();
        UaVariableNode uaVariableNode = client.getAddressSpace().getVariableNode(operator.node.produced);
        UaVariableNode stateNode = client.getAddressSpace().getVariableNode(operator.node.stateCurrent);
        System.out.println(uaVariableNode.getValue());

        operator.loadSettings(client);

        operator.start(client);

        operator.execute(client);
        operator.stop(client);


        NodeId produced = new NodeId(6,"::Program:product.produced");

        System.out.println(client.readValue(0,null,produced));



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
