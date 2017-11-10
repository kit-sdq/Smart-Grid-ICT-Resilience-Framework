package smartgrid.simcontrol.coupling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class TopologyContainer {

  private HashMap<Integer,HighVoltageNode> highVoltageNodes;
  
  private HashMap<String, Map<String, SmartMeterGeoData>> mapCItoHVN; 
  
  private TopologyContainer() {
  }
 
  public HashMap<Integer,HighVoltageNode> getHighVoltageNodes() {
    return new HashMap<>(highVoltageNodes);
  }
  
  public HashMap<String, Map<String, SmartMeterGeoData>> getMapCItoHVN() {
    return mapCItoHVN;
  }
  public void setMapCItoHVN(Map<String, Map<String, SmartMeterGeoData>> _mapCItoHVN) {
    this.mapCItoHVN = new HashMap<>(_mapCItoHVN);
  }
  
  public String toStringHighVoltageNodes() {
    StringBuilder builder;
    //
    builder = new StringBuilder();
    builder.append("ToDo: structured export");
    //
    return builder.toString();
  }

  public String toCSVHighVoltageNodes() {
    StringBuilder builder;
    //
    builder = new StringBuilder();
    builder.append(HighVoltageNode.getHeader());
    builder.append("\n");
    for (HighVoltageNode tmpNode : highVoltageNodes.values()) {
      builder.append(tmpNode.toString());
      builder.append("\n");
    }
    //
    return builder.toString();
  }

  public String toCSVHighVoltageNodeConnections() {
    StringBuilder builder;
    //
    builder = new StringBuilder();
    builder.append(HighVoltageNodeConnection.getHeader());
    builder.append("\n");
    for (HighVoltageNode tmpNode : highVoltageNodes.values()) {
      for (int index = 0; index < tmpNode.getConnectionsCount(); index++) {
        builder.append(tmpNode.getConnection(index).toString());
        builder.append("\n");
      }
    }
    //
    return builder.toString();
  }
  
  public static TopologyContainer build(File _highVoltageNodesFile, File _highVoltageNodeConnectionsFile) {
    TopologyContainer __topologyContainer;
    //
    __topologyContainer = new TopologyContainer();
    __topologyContainer.highVoltageNodes = loadHighVoltageNodes(_highVoltageNodesFile, _highVoltageNodeConnectionsFile);
    //
    return __topologyContainer;
  }
  
  public static HashMap<Integer,HighVoltageNode> loadHighVoltageNodes(File _highVoltageNodesFile, File _highVoltageNodeConnectionsFile) {
    HashMap<Integer,HighVoltageNode> __nodes;
    String line;
    HighVoltageNode node;
    HighVoltageNodeConnection connection;
    //
    __nodes = new HashMap<>();
    // establish nodes
    try (BufferedReader highVoltageNodesReader = new BufferedReader(new FileReader(_highVoltageNodesFile))) {
      // skip X initial lines
      int linesToSkip = 1;
      for (int i = 0; i < linesToSkip; i++) {
        line = highVoltageNodesReader.readLine();
      }
      while((line = highVoltageNodesReader.readLine()) != null) {
        node = HighVoltageNode.parse(line);
        __nodes.put(node.getId(), node);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }    
    // establish connections
    try (BufferedReader highVoltageConnectionsReader = new BufferedReader(new FileReader(_highVoltageNodeConnectionsFile))) {
      // skip X initial lines
      int linesToSkip = 1;
      for (int i = 0; i < linesToSkip; i++) {
        line = highVoltageConnectionsReader.readLine();
      }
      while((line = highVoltageConnectionsReader.readLine()) != null) {
        connection = HighVoltageNodeConnection.parse(line);
        __nodes.get(connection.getFromNodeId()).addConnection(connection);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }    
    //
    return __nodes;
  }
  
}
