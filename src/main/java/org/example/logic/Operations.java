package org.example.logic;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.UaClient;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.example.data.Settings;
import org.example.utils.Nodes;
import org.example.utils.STATES;
import org.example.utils.Statifyer;
import java.util.concurrent.CompletableFuture;

import static java.lang.Thread.sleep;

public class Operations {
    // Create a subscription at a desired publishing interval (e.g., 1000 ms)

    Statifyer statifyer = new Statifyer();

    Nodes node;

    Settings settings;
    int batchValue = 1;
    Variant batchId;


    public Operations(){
        this.node = new Nodes();
    }

    public void start(UaClient client) throws Exception{
        client.writeValue(node.cntrlCmd,DataValue.valueOnly(new Variant(1)));
        client.writeValue(node.cmdChange,DataValue.valueOnly(new Variant(true)));
    }

    public void stop(UaClient client) throws Exception {

        while (true) {
            System.out.println("CHECK "+client.readValue(0, TimestampsToReturn.Both,new NodeId(6,"::Program:Cube.Status.StateCurrent")));
            UaVariableNode stateNode = client.getAddressSpace().getVariableNode(node.stateCurrent);
            int stateInt = (int)(stateNode.getValue().getValue().getValue());
            STATES state = statifyer.showState(stateInt);
            System.out.println(state);
            sleep(1000);

            if (client.readValue(0, TimestampsToReturn.Both,new NodeId(6,"::Program:Cube.Status.StateCurrent")).isDone()) {
                System.out.println("Production finished");
                break;
            }
        }

        client.writeValue(node.cntrlCmd,DataValue.valueOnly(new Variant(3)));
        client.writeValue(node.cmdChange,DataValue.valueOnly(new Variant(true)));
        UaVariableNode stateNode = client.getAddressSpace().getVariableNode(node.stateCurrent);
        int stateInt = (int)(stateNode.getValue().getValue().getValue());
        STATES state = statifyer.showState(stateInt);
        System.out.println(state);

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
        batchValue += 1;
        batchId = new Variant(batchValue);

        //chooses the type of beer
        client.writeValue(node.parameter1,DataValue.valueOnly(new Variant(settings.getBeerType())));

        //chooses the amount of beer
        client.writeValue(node.parameter2,DataValue.valueOnly(new Variant(settings.getBeerAmount())));

        //switches speed of the product
        client.writeValue(node.machSpeed,DataValue.valueOnly(new Variant(settings.getMachSpeed())));

        //batch number
        client.writeValue(node.parameter0,DataValue.valueOnly(batchId));

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
        //sleep(1000);
        operator.start(client);
        //sleep(1000);

        operator.execute(client);
        //sleep(1000);

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
