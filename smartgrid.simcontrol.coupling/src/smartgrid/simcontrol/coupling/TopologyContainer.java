package smartgrid.simcontrol.coupling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class TopologyContainer implements Serializable {

  private static final long serialVersionUID = -6585199296553098329L;

  private LinkedHashMap<String,HighVoltageNode> highVoltageNodes;

  private LinkedHashMap<String,Map<String,SmartMeterGeoData>> mapCItoHVN;

  private String opfConfig;

  private TopologyContainer() {
  }

  public LinkedHashMap<String,HighVoltageNode> getHighVoltageNodes() {
    return new LinkedHashMap<>(highVoltageNodes);
  }

  public LinkedHashMap<String,Map<String,SmartMeterGeoData>> getMapCItoHVN() {
    return mapCItoHVN;
  }

  public void setMapCItoHVN(LinkedHashMap<String,Map<String,SmartMeterGeoData>> _mapCItoHVN) {
    this.mapCItoHVN = new LinkedHashMap<>(_mapCItoHVN);
  }

  public String getOpfConfig() {
    return opfConfig;
  }

  public void setOpfConfig(String _opfConfig) {
    this.opfConfig = _opfConfig;
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

  public static TopologyContainer build(File _highVoltageNodesFile, File _highVoltageNodeConnectionsFile, File _opfConfigFile) {
    TopologyContainer __topologyContainer;
    //
    __topologyContainer = new TopologyContainer();
    __topologyContainer.highVoltageNodes = loadHighVoltageNodes(_highVoltageNodesFile, _highVoltageNodeConnectionsFile);
    __topologyContainer.opfConfig = loadOpfConfig(_opfConfigFile);
    //
    return __topologyContainer;
  }

  public static LinkedHashMap<String,HighVoltageNode> loadHighVoltageNodes(File _highVoltageNodesFile, File _highVoltageNodeConnectionsFile) {
    LinkedHashMap<String,HighVoltageNode> __nodes;
    String line;
    HighVoltageNode node;
    HighVoltageNodeConnection connection;
    //
    __nodes = new LinkedHashMap<>();
    // establish nodes
    try (BufferedReader highVoltageNodesReader = new BufferedReader(new FileReader(_highVoltageNodesFile))) {
      // skip X initial lines
      int linesToSkip = 1;
      for (int i = 0; i < linesToSkip; i++) {
        line = highVoltageNodesReader.readLine();
      }
      while ((line = highVoltageNodesReader.readLine()) != null) {
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
      while ((line = highVoltageConnectionsReader.readLine()) != null) {
        connection = HighVoltageNodeConnection.parse(line);
        __nodes.get(connection.getFromNodeId()).addConnection(connection);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    //
    return __nodes;
  }

  public static String loadOpfConfig(File _opfConfigFile) {
    String line;
    StringBuilder builder;
    //
    builder = new StringBuilder();
    try (BufferedReader opfConfigReader = new BufferedReader(new FileReader(_opfConfigFile))) {
      while ((line = opfConfigReader.readLine()) != null) {
        builder.append(line);
        builder.append("\n");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    //
    return builder.toString();
  }

}
