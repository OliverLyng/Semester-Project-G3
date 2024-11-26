package org.example.data;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;


public class NodeRepository {
    private final OpcUaClient client;

    public NodeRepository(OpcUaClient client) {
        this.client = client;
    }


    public DataValue readNodeValue(NodeId nodeId) throws UaException {
        UaVariableNode variableNode = client.getAddressSpace().getVariableNode(nodeId);
        return variableNode.readValue();

    }



    public void writeNodeValue(NodeId nodeId, Variant value) throws InterruptedException, UaException {
        UaVariableNode variableNode = client.getAddressSpace().getVariableNode(nodeId);
        System.out.println("For NodeId: " + nodeId);
        System.out.println("variableNode in write: " + variableNode);
        System.out.println("Datatype should be: "+variableNode.getDataType());
        System.out.println("Datavalue in write: "+DataValue.valueOnly(value));
        variableNode.writeValue(DataValue.valueOnly(value));
    }


}
