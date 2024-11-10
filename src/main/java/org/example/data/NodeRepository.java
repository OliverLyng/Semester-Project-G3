package org.example.data;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;


import java.util.concurrent.ExecutionException;

public class NodeRepository {
    private final OpcUaClient client;

    public NodeRepository(OpcUaClient client) {
        this.client = client;
    }
    public DataValue readNodeValue(NodeId nodeId) throws ExecutionException, InterruptedException {
        return client.readValue(0, null, nodeId).get();
    }

    public void writeNodeValue(NodeId nodeId, Variant value) throws ExecutionException, InterruptedException {
        DataValue dataValue = new DataValue(value, null, null);
        client.writeValue(nodeId, dataValue).get();
    }

}
