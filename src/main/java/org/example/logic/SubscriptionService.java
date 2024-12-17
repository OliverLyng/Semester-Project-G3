package org.example.logic;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaSubscription;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringParameters;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

public class SubscriptionService {
    private final OpcUaClient client;
    private final Map<NodeId, UaMonitoredItem> nodeItems = new HashMap<>(); // Map to track NodeId to MonitoredItem
    private final Map<NodeId, UaSubscription> nodeSubscriptions = new HashMap<>(); // Map to track NodeId to Subscription

    public SubscriptionService(OpcUaClient client) {
        this.client = client;
    }

    public UaSubscription createSubscription(double publishingInterval) throws ExecutionException, InterruptedException {
        return client.getSubscriptionManager().createSubscription(publishingInterval).get();
    }

    public UaMonitoredItem subscribeToNode(NodeId nodeId, Consumer<DataValue> valueConsumer) throws ExecutionException, InterruptedException {
        UaSubscription subscription = createSubscription(1000.0);
        UInteger attributeId = AttributeId.Value.uid();
        ReadValueId readValueId = new ReadValueId(nodeId, attributeId,null,null);
        // IMPORTANT: client handle must be unique per item within the context of a subscription.
        // You are not required to use the UaSubscription's client handle sequence; it is provided as a convenience.
        // Your application is free to assign client handles by whatever means necessary.
        UInteger clientHandle = subscription.nextClientHandle();
        MonitoringParameters parameters = new MonitoringParameters(
                clientHandle,
                1000.0,     // sampling interval
                null,       // filter, null means use default
                uint(10),   // queue size
                true        // discard oldest
        );
        MonitoredItemCreateRequest request = new MonitoredItemCreateRequest(
                readValueId,
                MonitoringMode.Reporting,
                parameters
        );
        List<UaMonitoredItem> items = subscription.createMonitoredItems(
                TimestampsToReturn.Both,
                List.of(request),
                (item, id) -> item.setValueConsumer(valueConsumer)).get();
        return items.get(0); // Assuming one node subscription

    }
    //    public void unsubscribeNode(UaMonitoredItem monitoredItem, UaSubscription subscription) throws ExecutionException, InterruptedException {
//        subscription.deleteMonitoredItems(List.of(monitoredItem)).get();
//        if (subscription.getMonitoredItems().isEmpty()) {
//            client.getSubscriptionManager().deleteSubscription(subscription.getSubscriptionId()).get();
//        }
//    }
    public void unsubscribeNode(NodeId nodeId) throws ExecutionException, InterruptedException {
        UaMonitoredItem monitoredItem = nodeItems.remove(nodeId);
        if (monitoredItem != null) {
            UaSubscription subscription = nodeSubscriptions.get(nodeId);
            if (subscription != null) {
                subscription.deleteMonitoredItems(List.of(monitoredItem)).get();
                if (subscription.getMonitoredItems().isEmpty()) {
                    client.getSubscriptionManager().deleteSubscription(subscription.getSubscriptionId()).get();
                    nodeSubscriptions.remove(nodeId);
                }
            }
        }
    }
    public CompletableFuture<DataValue> readNodeAsync(NodeId nodeId) {
        // This method uses the client to read a node value asynchronously.
        return client.readValue(0, TimestampsToReturn.Both, nodeId);
    }
}

// package org.example.logic;

// import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
// import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaMonitoredItem;
// import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaSubscription;
// import org.eclipse.milo.opcua.stack.core.AttributeId;
// import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
// import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
// import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
// import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
// import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateRequest;
// import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringParameters;

// import java.util.List;
// import java.util.function.Consumer;

// public class SubscriptionService {
//     private final OpcUaClient client;

//     public SubscriptionService(OpcUaClient client) {
//         this.client = client;
//     }

//     public UaSubscription createSubscription(double publishingInterval) throws Exception {
//         return client.getSubscriptionManager().createSubscription(publishingInterval).get();
//     }

//     public UaMonitoredItem subscribeToNode(NodeId nodeId, Consumer<DataValue> valueConsumer) throws Exception {
//         UaSubscription subscription = createSubscription(1000.0);
//         MonitoringParameters parameters = new MonitoringParameters(
//             subscription.nextClientHandle(),
//             1000.0,     // sampling interval
//             null,       // filter, null means use default
//             10,         // queue size
//             true        // discard oldest
//         );
//         MonitoredItemCreateRequest request = new MonitoredItemCreateRequest(
//             new ReadValueId(nodeId, AttributeId.Value.uid(), null, null),
//             MonitoringMode.Reporting,
//             parameters
//         );
//         List<UaMonitoredItem> items = subscription.createMonitoredItems(
//             TimestampsToReturn.Both,
//             List.of(request),
//             (item, id) -> item.setValueConsumer(valueConsumer)
//         ).get();
//         return items.get(0); // Assuming single subscription
//     }
// }
