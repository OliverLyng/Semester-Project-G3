package org.example.logic;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.example.utils.Nodes;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BeerMachineReport {

    private OpcUaClient client;
    Nodes node; 

    public BeerMachineReport(OpcUaClient client) {
        this.client = client;
    }

    // This method feteches live data for the report
    public double[] fetchLiveData() {
        double[] data = new double[5]; 

        try {
            // Batch ID
            data[0] = fetchLiveDataHelper(node.parameter0, "BatchID");

            // Temperature
            data[1] = fetchLiveDataHelper(node.temperature, "Temperature");

            // Humidity
            data[2] = fetchLiveDataHelper(node.humidity, "Humidity");

            // Produced Count
            data[3] = fetchLiveDataHelper(node.producedCount, "ProducedCount");

            // Defective Count
            //data[4] = fetchLiveDataHelper(node.defectCount, "DefectiveCount");

            // Beer Type 
            data[4] = fetchLiveDataHelper(node.parameter1, "BeerType");

        } catch (Exception e) {
            System.err.println("There was an error when fetching some data");
        }

        return data;
    }

    // Helper method to fetch data values 
    private double fetchLiveDataHelper(NodeId nodeId, String name) {
        try {
            UaVariableNode variableNode = client.getAddressSpace().getVariableNode(nodeId); 
            return Double.parseDouble(variableNode.readValue().getValue().getValue().toString());
        } catch (Exception e) {
            System.err.println("Error fetching " + name);
            return 0.0; // returns a default value (0.0) if an error happens when fetching data values
        }
    }

    // This method calculates the mean and variance 
    public double[] calculateStatistics(List<Double> data) {
        double sum = 0; 
        double sumOfSquares = 0;
        for (double value : data) {
            sum =sum+ value;
            sumOfSquares = sumOfSquares+ value * value;
        }

        double mean = sum / data.size();
        double variance = (sumOfSquares / data.size()) - (mean * mean);

        return new double[]{mean, variance};
    }


    public void generateReport(String fileName, double[] liveData, double[] stats) {
        String[] headers = {"Batch ID", "Beer Type", "Produced", "Temperature", "Humidity", "Mean Temp", "Variance Temp"};
    
        try (FileWriter writer = new FileWriter(fileName)) {
            
            for (String element : headers) {
                writer.write(element + ",");
            }
            System.lineSeparator();  
    
            // Write live data
            writer.write(liveData[0] + "," + 
                liveData[1] + "," + // Temperature
                liveData[2] + "," + // Humidity
                liveData[3] + "," + // Produced
                //liveData[4] + "," + // Defects
                liveData[4] + "," + // Beer Type
                stats[0] + "," + // Mean Temp
                stats[1]); // Variance Temp
            System.lineSeparator();  
    
            System.out.println("Report is generated successfully: " + fileName);
        } catch (IOException e) {
            System.err.println("Error writing report: " + e.getMessage());
        }
    }
   

    public static void main(String[] args) {
        try {
            
            OpcUaClient client = OpcUaClient.create("opc.tcp://localhost:4840");
            client.connect().get();
    
            
            BeerMachineReport reportGenerator = new BeerMachineReport(client);
    
            
            double[] liveData = reportGenerator.fetchLiveData();
    
            // Prepare historical data (example data)
            List<Double> temperatureData = List.of(22.5, 23.0, 22.8, liveData[1]); 

            double[] stats = reportGenerator.calculateStatistics(temperatureData); 
    
            
            reportGenerator.generateReport("beer_machine_report.csv", liveData, stats);
    
            
            client.disconnect().get();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    

    

}
