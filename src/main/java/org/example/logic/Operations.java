package org.example.logic;


import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.example.data.NodeRepository;
import org.example.data.OPCUAServerConnection;
import org.example.exceptions.EmptyInventoryException;
import org.example.exceptions.MaintenanceException;
import org.example.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;


public class Operations {

    private static final Logger logger = LoggerFactory.getLogger(Operations.class);

    private NodeRepository nodeRepository;
    private OpcUaClient client;
    Settings settings;
    UaVariableNode variableNodeState;
    static String endpointUrl = "opc.tcp://localhost:4840";
    boolean needsReset = false;

    public Operations(OpcUaClient client) {
        this.client = client;
        this.nodeRepository = new NodeRepository(client);
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

            Operations operator = new Operations(client);

            // Subscribe to changes on status
            subscriptionService.subscribeToNode(Nodes.stateCurrent, dataValue -> {
                System.out.println("New value received for Current State: " + dataValue);
                logger.info("New value received for Current State: {}", dataValue);
            });
            // Subscribe to changes on produced items
            subscriptionService.subscribeToNode(Nodes.produced, dataValue -> {
                System.out.println("New value received for Produced Items: " + dataValue);
                logger.info("New value received for Produced Items: {}", dataValue);
            });



            //operator.reset(client);
            operator.loadSettings();
            STATES states = operator.checkStatus();
            System.out.println("Here we see if states are found: " + states);
            System.out.println("Here is reading from NodeRepo: " + nodeRepository.readNodeValue(Nodes.stateCurrent));

            operator.handleStartStatus(states);
            if (operator.needsReset) {
                operator.reset();
            }
            operator.execute(client);

            // After production is finished write batch report
            logger.info("Batch: " + nodeRepository.readNodeValue(Nodes.cmdBatchId));
            logger.info("BeerType: " + nodeRepository.readNodeValue(Nodes.cmdBeerType));
            logger.info("Total Produced: " + nodeRepository.readNodeValue(Nodes.prodProcessedCount));
            logger.info("Defective: " + nodeRepository.readNodeValue(Nodes.prodDefectiveCount));



            // Maybe?? Keep the application running to continue receiving updates
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


    private void reset() throws Exception {
        System.out.println("In Reset");
        logger.info("In Reset");
        nodeRepository.writeNodeValue (Nodes.cntrlCmd, new Variant(1));
        nodeRepository.writeNodeValue (Nodes.cmdChange, new Variant(true));
//        client.writeValue(Nodes.cntrlCmd, DataValue.valueOnly(new Variant(1)));
//        client.writeValue(Nodes.cmdChange, DataValue.valueOnly(new Variant(true)));
        System.out.println(Converter.showState(Integer.parseInt(variableNodeState.readValue().getValue().getValue().toString())));
        logger.info("State: {}", Converter.showState(Integer.parseInt(variableNodeState.readValue().getValue().getValue().toString())));
    }

    public void clear() throws Exception {

        nodeRepository.writeNodeValue (Nodes.cntrlCmd, new Variant(5));
        nodeRepository.writeNodeValue (Nodes.cmdChange, new Variant(true));

    }

    public void start() throws UaException, InterruptedException {
        //starts the production
        nodeRepository.writeNodeValue (Nodes.cntrlCmd, new Variant(2));
        nodeRepository.writeNodeValue(Nodes.cmdChange, new Variant(true));
    }

    public void execute(OpcUaClient client) throws Exception {

        clear();
        variableNodeState = client.getAddressSpace().getVariableNode(Nodes.stateCurrent);
        start();
        STATES states = checkStatus();
        STOPPED_REASON reason;
        if (states.equals(STATES.STOPPED)) {
            handleStoppedStatus(states);
            reset();
            start();
        }

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
//        System.out.println("Beers produced: " + client.getAddressSpace().getVariableNode(Nodes.produced).readValue().getValue().getValue().toString());
//        logger.info("Beers produced: : {}", client.getAddressSpace().getVariableNode(Nodes.produced).readValue().getValue().getValue().toString());
    }


    public void loadSettings() throws Exception {

        settings = new Settings(0.0f, 100.0f, 300.0f);
        UaVariableNode uaVariableNode;


        //chooses the type of beer
        nodeRepository.writeNodeValue(Nodes.cmdBeerType, new Variant(settings.getBeerType()));


        // This shows beertype as enum BEERTYPE using the converter
        // Unknown if this is useful
        uaVariableNode = client.getAddressSpace().getVariableNode(Nodes.cmdBeerType);
        Object obj = uaVariableNode.readValue().getValue().getValue();
        float f = (Float) obj;
        int roundedValue = Math.round(f);
        System.out.println("Beertype is " + Converter.showBeerType(roundedValue));
        logger.info("Beertype is  {}", Converter.showBeerType(roundedValue));

        //chooses the amount of beer
        nodeRepository.writeNodeValue(Nodes.cmdAmountOfBeer, new Variant(settings.getBeerAmount()));

        // Also unknown if this is useful
        uaVariableNode = client.getAddressSpace().getVariableNode(Nodes.cmdAmountOfBeer);
        System.out.println("Amount: " + uaVariableNode.readValue().getValue().getValue().toString());
        logger.info("Amount is  {}", uaVariableNode.readValue().getValue().getValue().toString());

        //switches speed of the product
        nodeRepository.writeNodeValue(Nodes.cmdMachSpeed, new Variant(settings.getMachSpeed()));

        // Also unknown if this is useful
        uaVariableNode = client.getAddressSpace().getVariableNode(Nodes.cmdMachSpeed);
        System.out.println("Speed: " + uaVariableNode.readValue().getValue().getValue().toString());
        logger.info("Speed: {}", uaVariableNode.readValue().getValue().getValue().toString());


        nodeRepository.writeNodeValue(Nodes.cmdBatchId, new Variant(14.0f));

        // Also unknown if this is useful
        uaVariableNode = client.getAddressSpace().getVariableNode(Nodes.cmdBatchId);
        System.out.println("Batch ID: " + uaVariableNode.readValue().getValue().getValue().toString());
        logger.info("Batch ID: {}", uaVariableNode.readValue().getValue().getValue().toString());
    }


    private void handleStartStatus(STATES state) throws UaException {
        if (state.equals(STATES.STOPPED)) {
            needsReset = true;
        }
    }

    private STOPPED_REASON handleStoppedStatus(STATES state) throws UaException, EmptyInventoryException, MaintenanceException {
        STOPPED_REASON reasonState = null;
        if (state.equals(STATES.STOPPED)) {
            int reason = Integer.parseInt(nodeRepository.readNodeValue(Nodes.stopReason).getValue().getValue().toString());
            reasonState =  Converter.showStopReason(reason);
            switch (Objects.requireNonNull(reasonState)) {
                case EMPTY_INVENTORY -> {
                    throw new EmptyInventoryException("Inventory is empty");
                }
                case MAINTENANCE -> {
                    throw new MaintenanceException("Maintenance");
                }
                case MANUAL_STOP -> {
                    System.out.println("Manual Stop");
                    logger.info("Manual Stop");
                }
                case MOTOR_POWER_LOSS -> {
                    System.out.println("Motor stopped");
                    logger.info("Motor Stopped");
                }
                case MANUAL_ABORT -> {
                    System.out.println("Production was aborted manually");
                    logger.info("Production was aborted manually");
                }
                default -> {
                    System.out.println("Stop reason unknown");
                    logger.info("Stop reason unknown");
                }
            }
        }
        return reasonState;
    }
    private STATES checkStatus() throws Exception {
        STATES state = Converter.showState(Integer.parseInt(nodeRepository.readNodeValue(Nodes.stateCurrent).getValue().getValue().toString()));
        return state;

    }

}