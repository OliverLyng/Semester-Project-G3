package org.example.data;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.example.logic.Operations;
import org.example.utils.Nodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;


public class NodeRepository {

    private OPCUAServerConnection serverConnection;

    static String endpointUrl = "opc.tcp://localhost:4840";

    private static final Logger logger = LoggerFactory.getLogger(Operations.class);

    OpcUaClient client;

    public NodeRepository() throws UaException, ExecutionException, InterruptedException {
        this.serverConnection = OPCUAServerConnection.getInstance(endpointUrl);
    }
    public void setClient(OpcUaClient client) {
        this.client = client;
    }

//    public DataValue readNodeValue(NodeId nodeId) throws ExecutionException, InterruptedException {
//        return client.readValue(0, null, nodeId).get();
//    }
//
//    public void writeNodeValue(NodeId nodeId, Variant value) throws ExecutionException, InterruptedException {
//        DataValue dataValue = DataValue.valueOnly(value);
//        client.writeValue(nodeId, dataValue);
//    }

//    public DataValue readNodeValue(NodeId nodeId) throws ExecutionException, InterruptedException {
//
//        if (!serverConnection.isConnected()) {
//            logger.error("Client is not connected. Cannot read node: {}", nodeId);
//            throw new IllegalStateException("Client is not connected.");
//        }
//        return client.readValue(0, null, nodeId).get();
//    }
//
//    public void writeNodeValue(NodeId nodeId, Variant value) throws ExecutionException, InterruptedException {
//        if (!serverConnection.isConnected()) {
//            logger.error("Client is not connected. Cannot write node: {}", nodeId);
//            throw new IllegalStateException("Client is not connected.");
//        }
//        DataValue dataValue = new DataValue(value, null, null);
//        client.writeValue(nodeId, dataValue).get();
//    }

    public DataValue readNodeValue(NodeId nodeId) throws ExecutionException, InterruptedException {
        if (client == null) {
            throw new IllegalStateException("Client is not connected.");
        }
        System.out.println("Reading attempted");
        System.out.println(client.readValue(0, null, Nodes.stateCurrent).get());
        return client.readValue(0, null, nodeId).get();
    }

    public void writeNodeValue(NodeId nodeId, Variant value) throws ExecutionException, InterruptedException {
        if (client == null) {
            throw new IllegalStateException("Client is not connected.");
        }
        DataValue dataValue = new DataValue(value, null, null);
        client.writeValue(nodeId, dataValue).get();
    }

}
