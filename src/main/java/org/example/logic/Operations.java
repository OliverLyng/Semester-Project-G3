package org.example.logic;


import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.UaClient;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;

import org.example.utils.LogAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.example.data.NodeRepository;
import org.example.data.OPCUAServerConnection;

import org.example.utils.Converter;
import org.example.utils.Nodes;
import org.example.utils.STATES;



public class Operations {

    private static final Logger logger = LoggerFactory.getLogger(Operations.class);

    static Operations operator;

    Settings settings;

    UaVariableNode variableNodeState;

    static String endpointUrl = "opc.tcp://localhost:4840";
    boolean needsReset = false;


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
        client.writeValue(Nodes.cmdBeerType, DataValue.valueOnly(new Variant(settings.getBeerType())));

        uaVariableNode = client.getAddressSpace().getVariableNode(Nodes.cmdBeerType);

        Object obj = uaVariableNode.readValue().getValue().getValue();
        float f = (Float) obj;
        int roundedValue = Math.round(f);
        System.out.println("Beertype is " + Converter.showBeerType(roundedValue));
        logger.info("Beertype is  {}", Converter.showBeerType(roundedValue));

        //chooses the amount of beer
        client.writeValue(Nodes.cmdAmountOfBeer, DataValue.valueOnly(new Variant(settings.getBeerAmount())));

        uaVariableNode = client.getAddressSpace().getVariableNode(Nodes.cmdAmountOfBeer);
        System.out.println("Amount: " + uaVariableNode.readValue().getValue().getValue().toString());
        logger.info("Amount is  {}", uaVariableNode.readValue().getValue().getValue().toString());

        //switches speed of the product
        client.writeValue(Nodes.cmdMachSpeed, DataValue.valueOnly(new Variant(settings.getMachSpeed())));

        uaVariableNode = client.getAddressSpace().getVariableNode(Nodes.cmdMachSpeed);
        System.out.println("Speed: " + uaVariableNode.readValue().getValue().getValue().toString());
        logger.info("Speed: {}", uaVariableNode.readValue().getValue().getValue().toString());

        //Batch Id
        client.writeValue(Nodes.cmdBatchId, DataValue.valueOnly(new Variant(20)));
        uaVariableNode = client.getAddressSpace().getVariableNode(Nodes.cmdBatchId);
        System.out.println("Batch ID: " + uaVariableNode.readValue().getValue().getValue().toString());
        logger.info("Batch ID: {}", uaVariableNode.readValue().getValue().getValue().toString());
    }


    private void handleStartStatus(STATES state) throws UaException {
        if (state.equals(STATES.STOPPED)) {
            needsReset = true;
        }
    }

    private void handleStoppedStatus(STATES state) {
        if (state.equals(STATES.STOPPED)) {

        }
    }
    private STATES checkStatus(OpcUaClient client) throws Exception {
        variableNodeState = client.getAddressSpace().getVariableNode(Nodes.stateCurrent);
        UaVariableNode uaVariableNode;
        STATES state = Converter.showState(Integer.parseInt(variableNodeState.readValue().getValue().getValue().toString()));
        return state;
//        switch (Objects.requireNonNull(states)) {
//            case ABORTED -> {
//                System.out.println("Machine production was aborted");
//                logger.error("Machine production was aborted");
//            }
//
//
//            case COMPLETE, STOPPED -> {
//                System.out.println("Production complete. Resetting...");
//                logger.info("Production complete. Resetting...");
//                operator.reset(client);
//            }
//            case EXECUTE -> {
//                uaVariableNode = client.getAddressSpace().getVariableNode(Nodes.machSpeedState);
//                System.out.println("Speed: " + uaVariableNode.readValue().getValue().getValue().toString());
//                logger.info("Speed: {}", uaVariableNode.readValue().getValue().getValue().toString());
//            }
//            case IDLE ->
//                    operator.execute(client);
//        }
    }

    public static void main(String[] args) throws Exception {
        LogAppender.appendNewLineToLog(); // Appends a newline at the start of each run
        logger.info("Starting Program");

        OPCUAServerConnection serverConnection = null;
        OpcUaClient client = null;
        try {
            serverConnection = OPCUAServerConnection.getInstance(endpointUrl);
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
            STATES states = operator.checkStatus(client);
            operator.handleStartStatus(states);
            if (operator.needsReset) {
                operator.reset(client);
            }
            operator.execute(client);


            DataValue dataValue = nodeRepository.readNodeValue(Nodes.stateCurrent);
            System.out.println("Current value: " + dataValue);
            logger.info("Current value: {}", dataValue);
            nodeRepository.writeNodeValue(Nodes.cmdMachSpeed, new Variant(300));

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