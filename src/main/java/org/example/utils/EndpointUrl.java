package org.example.utils;

public class EndpointUrl {
    private static String endpointUrlRealMachine = "opc.tcp://192.168.0.122:4840";
    private static String endpointUrl = "opc.tcp://localhost:4840";
    private static String selectedEndpointMachine = "192.168.0.122";
    private static String selectedEndpointSimulation = "localhost";


    public static String getEndpointUrl() {
        return endpointUrl;
        //return endpointUrlRealMachine;

    }
    public static String getSelectedEndpoint() {
        return selectedEndpointSimulation;
        //return selectedEndpointMachine;
    }
}
