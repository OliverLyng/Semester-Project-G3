package org.example.logic;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.UaClient;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.example.data.NodeRepository;
import org.example.data.OPCUAServerConnection;

import org.example.utils.Converter;
import org.example.utils.Nodes;
import org.example.utils.STATES;

import java.util.Objects;

public class Operations {

    private static final Logger logger = LoggerFactory.getLogger(Operations.class);

    static Operations operator;

    Settings settings;

    UaVariableNode variableNodeState;

    static String endpointUrl = "opc.tcp://localhost:4840";


    private void reset(UaClient client) throws Exception {
        System.out.println("In Reset");
        logger.info("In Reset");
        client.writeValue(Nodes.cntrlCmd, DataValue.valueOnly(new Variant(1)));
        client.writeValue(Nodes.cmdChange, DataValue.valueOnly(new Variant(true)));
        System.out.println(Converter.showState(Integer.parseInt(variableNodeState.readValue().getValue().getValue().toString())));
        logger.info("State: {}", Converter.showState(Integer.parseInt(variableNodeState.readValue().getValue().getValue().toString())));
    }

    public void clear(UaClient client) throws Exception {
        client.writeValue(Nodes.cntrlCmd, DataValue.valueOnly(new Variant(5)));
        client.writeValue(Nodes.cmdChange, DataValue.valueOnly(new Variant(true)));
    }

    public void execute(OpcUaClient client) throws Exception {

        variableNodeState = client.getAddressSpace().getVariableNode(Nodes.stateCurrent);
        //starts the production
        client.writeValue(Nodes.cntrlCmd, DataValue.valueOnly(new Variant(2)));
        client.writeValue(Nodes.cmdChange, DataValue.valueOnly(new Variant(true)));
        STATES states = Converter.showState(Integer.parseInt(variableNodeState.readValue().getValue().getValue().toString()));
        STATES currentState = states;
        System.out.println(currentState);
        logger.info("State: {}",currentState);
        while (states != STATES.COMPLETE) {
            while (currentState == states) {
                states = Converter.showState(Integer.parseInt(variableNodeState.readValue().getValue().getValue().toString()));
            }
            System.out.println(states);
            logger.info("State: {}",states);
            currentState = states;

            //operator.checkStatus(client);
        }
        System.out.println("Beers produced: " + client.getAddressSpace().getVariableNode(Nodes.produced).readValue().getValue().getValue().toString());
        logger.info("Beers produced: : {}", client.getAddressSpace().getVariableNode(Nodes.produced).readValue().getValue().getValue().toString());
    }


    public void loadSettings(OpcUaClient client) throws Exception {

        settings = new Settings(2, 100, 300);
        UaVariableNode uaVariableNode;


        //chooses the type of beer
        client.writeValue(Nodes.parameter1, DataValue.valueOnly(new Variant(settings.getBeerType())));

        uaVariableNode = client.getAddressSpace().getVariableNode(Nodes.parameter1);

        Object obj = uaVariableNode.readValue().getValue().getValue();
        float f = (Float) obj;
        int roundedValue = Math.round(f);
        System.out.println("Beertype is " + Converter.showBeerType(roundedValue));
        logger.info("Beertype is  {}", Converter.showBeerType(roundedValue));

        //chooses the amount of beer
        client.writeValue(Nodes.parameter2, DataValue.valueOnly(new Variant(settings.getBeerAmount())));

        uaVariableNode = client.getAddressSpace().getVariableNode(Nodes.parameter2);
        System.out.println("Amount: " + uaVariableNode.readValue().getValue().getValue().toString());
        logger.info("Amount is  {}", uaVariableNode.readValue().getValue().getValue().toString());

        //switches speed of the product
        client.writeValue(Nodes.machSpeed, DataValue.valueOnly(new Variant(settings.getMachSpeed())));

        uaVariableNode = client.getAddressSpace().getVariableNode(Nodes.machSpeedState);
        System.out.println("Speed: " + uaVariableNode.readValue().getValue().getValue().toString());
        logger.info("Speed: {}", uaVariableNode.readValue().getValue().getValue().toString());
        //Batch Id
        client.writeValue(Nodes.parameter0, DataValue.valueOnly(new Variant(20)));

        uaVariableNode = client.getAddressSpace().getVariableNode(Nodes.statusParameter0);
        System.out.println("Batch ID: " + uaVariableNode.readValue().getValue().getValue().toString());
        logger.info("Batch ID: {}", uaVariableNode.readValue().getValue().getValue().toString());
    }

    private void printStatus() throws Exception {
        System.out.println();
        logger.info("");
    }

    private void checkStatus(OpcUaClient client) throws Exception {
        variableNodeState = client.getAddressSpace().getVariableNode(Nodes.stateCurrent);
        UaVariableNode uaVariableNode;
        STATES states = Converter.showState(Integer.parseInt(variableNodeState.readValue().getValue().getValue().toString()));
        switch (Objects.requireNonNull(states)) {
            case ABORTED -> {
                System.out.println("Machine production was aborted");
                logger.error("Machine production was aborted");
            }


            case COMPLETE, STOPPED -> {
                System.out.println("Production complete. Resetting...");
                logger.info("Production complete. Resetting...");
                operator.reset(client);
            }
            case EXECUTE -> {
                uaVariableNode = client.getAddressSpace().getVariableNode(Nodes.machSpeedState);
                System.out.println("Speed: " + uaVariableNode.readValue().getValue().getValue().toString());
                logger.info("Speed: {}", uaVariableNode.readValue().getValue().getValue().toString());
            }
//            case IDLE ->
//                    operator.execute(client);
        }
    }

    public static void main(String[] args) throws Exception {


        OPCUAServerConnection serverConnection = null;
        OpcUaClient client = null;
        try {
            serverConnection = new OPCUAServerConnection(endpointUrl);
            client = serverConnection.connect();
            SubscriptionService subscriptionService = new SubscriptionService(client);
            NodeRepository nodeRepository = new NodeRepository(client);

            // Subscribe to changes on status
            subscriptionService.subscribeToNode(Nodes.stateCurrent, dataValue -> {
                System.out.println("New value received for Current Stat2e: " + dataValue);
                logger.info("New value received for Current State: {}", dataValue);
            });
            // Subscribe to changes on produced items
            subscriptionService.subscribeToNode(Nodes.produced, dataValue -> {
                System.out.println("New value received for Produced Items: " + dataValue);
                logger.info("New value received for Produced Items: {}", dataValue);
            });


            operator = new Operations();

            //operator.reset(client);
            operator.loadSettings(client);
            operator.execute(client);


            DataValue dataValue = nodeRepository.readNodeValue(Nodes.stateCurrent);
            System.out.println("Current value: " + dataValue);
            logger.info("Current value: {}", dataValue);
            nodeRepository.writeNodeValue(Nodes.machSpeedState, new Variant(300));

            // Keep the application running to continue receiving updates
            Thread.sleep(Long.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(String.valueOf(e));
        } finally {
            if (client != null) {
                try {
                    client.disconnect().get();
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error(String.valueOf(e));

                }
            }
        }
    }
}


//
//
//        String endpointUrl = "opc.tcp://localhost:4840";  // Change to your server's URL
//
//        // Build OPC-UA client
//        OpcUaClient client = OpcUaClient.create(endpointUrl);
//
//        // Connect to the server
//        CompletableFuture<UaClient> connectFuture = client.connect();
//
//        // Block until connection is established
//        connectFuture.get();
//
//        System.out.println("Client connected to server: " + endpointUrl);
//        operator = new Operations();
//
//        operator.checkStatus(client);
//        //operator.reset(client);
//        operator.loadSettings(client);
//        operator.execute(client);






