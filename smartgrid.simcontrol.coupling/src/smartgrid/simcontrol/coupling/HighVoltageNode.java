package smartgrid.simcontrol.coupling;

import java.io.Serializable;
import java.util.ArrayList;

public class HighVoltageNode
  implements Serializable {

////////////////////////////////////////////////////////////////////////////////
//constants definitions
////////////////////////////////////////////////////////////////////////////////
  /** A logger used to redirect debug output. */
  // private static final Logger LOG =
  // LogManager.getLogger(HighVoltageNode.class);

////////////////////////////////////////////////////////////////////////////////
// field definitions
////////////////////////////////////////////////////////////////////////////////
  private int id;

  private String shortName;

  private String longName;

  private double lat;

  private double lon;

  private boolean hasMediumVoltage;

  private double p_max;

  private double p_min;

  private String comment;

  private ArrayList<HighVoltageNodeConnection> connections;

////////////////////////////////////////////////////////////////////////////////
// constructor, main and initialization
////////////////////////////////////////////////////////////////////////////////

  /**
   * Beanstyle constructor of HighVoltageNode.
   * No parameters required.
   * Calls preInit(), init() and postInit();
   */
  public HighVoltageNode() {
    preInit();
    init();
    postInit();
  }

  /**
   * Initializations before main initialization (of components).
   */
  private void preInit() {
  }

  /**
   * Main initializations (of components).
   */
  private void init() {
    initDefaults();
  }

  /**
   * Initializations after main initialization (of components).
   */
  private void postInit() {
  }

  /**
   * Initializations of the default values
   */
  private void initDefaults() {
    id = -1;
    shortName = "";
    longName = "";
    lat = 0.0;
    lon = 0.0;
    hasMediumVoltage = false;
    p_max = -1;
    p_min = -1;
    comment = "";
    connections = new ArrayList<>();
  }

  /**
   * @return the id
   */
  public int getId() {
    return id;
  }

  /**
   * @param _id
   *          the id to set
   */
  public void setId(int _id) {
    this.id = _id;
  }

  /**
   * @return the shortName
   */
  public String getShortName() {
    return shortName;
  }

  /**
   * @param _shortName
   *          the shortName to set
   */
  public void setShortName(String _shortName) {
    this.shortName = _shortName;
  }

  /**
   * @return the longName
   */
  public String getLongName() {
    return longName;
  }

  /**
   * @param _longName
   *          the longName to set
   */
  public void setLongName(String _longName) {
    this.longName = _longName;
  }

  /**
   * @return the lat
   */
  public double getLat() {
    return lat;
  }

  /**
   * @param _lat
   *          the lat to set
   */
  public void setLat(double _lat) {
    this.lat = _lat;
  }

  /**
   * @return the lon
   */
  public double getLon() {
    return lon;
  }

  /**
   * @param _lon
   *          the lon to set
   */
  public void setLon(double _lon) {
    this.lon = _lon;
  }

  /**
   * @return the hasMediumVoltage
   */
  public boolean isHasMediumVoltage() {
    return hasMediumVoltage;
  }

  /**
   * @param _hasMediumVoltage
   *          the hasMediumVoltage to set
   */
  public void setHasMediumVoltage(boolean _hasMediumVoltage) {
    this.hasMediumVoltage = _hasMediumVoltage;
  }

  /**
   * @return the p_max
   */
  public double getP_max() {
    return p_max;
  }

  /**
   * @param _p_max
   *          the p_max to set
   */
  public void setP_max(double _p_max) {
    this.p_max = _p_max;
  }

  /**
   * @return the p_min
   */
  public double getP_min() {
    return p_min;
  }

  /**
   * @param _p_min
   *          the p_min to set
   */
  public void setP_min(double _p_min) {
    this.p_min = _p_min;
  }

  /**
   * @return the comment
   */
  public String getComment() {
    return comment;
  }

  /**
   * @param _comment
   *          the comment to set
   */
  public void setComment(String _comment) {
    this.comment = _comment;
  }

  public void addConnection(HighVoltageNodeConnection _connection) {
    connections.add(_connection);
  }

  public HighVoltageNodeConnection getConnection(int _index) {
    return connections.get(_index);
  }

  public void removeConnection(HighVoltageNodeConnection _connection) {
    connections.remove(_connection);
  }

  public void clearConnections() {
    connections.clear();
  }

  public int getConnectionsCount() {
    return connections.size();
  }

  public boolean hasConnections() {
    return !connections.isEmpty();
  }

  public boolean isConnectioned(HighVoltageNode _node) {
    boolean __connected;
    //
    __connected = false;
    for (HighVoltageNodeConnection tmpConnection : connections) {
      if (tmpConnection.getToNodeId() == _node.getId()) {
        __connected = true;
        break;
      }
    }
    //
    return __connected;
  }

  public static String getHeader() {
    return "standort_ID,name_kurz,name_lang,latitude,longitude,gemeindekennziffer_REF,postleitzahl_REF,hat_mittelspannung,p_max [MW],p_min [MW],kommentar";
  }

  @Override public String toString() {
    StringBuilder builder;
    String separator;
    //
    separator = ",";
    builder = new StringBuilder();
    builder.append(id);
    builder.append(separator);
    builder.append(shortName);
    builder.append(separator);
    builder.append(longName);
    builder.append(separator);
    builder.append(lat);
    builder.append(separator);
    builder.append(lon);
    builder.append(separator);
    builder.append("");
    builder.append(separator);
    builder.append("");
    builder.append(separator);
    builder.append(hasMediumVoltage ? "1" : "0");
    builder.append(separator);
    builder.append(p_max);
    builder.append(separator);
    builder.append(p_min);
    builder.append(separator);
    builder.append(comment);
    //
    return builder.toString();
  }

  public static HighVoltageNode parse(String _line) {
    HighVoltageNode __node;
    String[] split;
    //
    split = _line.split(",", -1);
    __node = new HighVoltageNode();
    __node.setId(Integer.parseInt(split[0]));
    __node.setShortName(split[1]);
    __node.setLongName(split[2]);
    __node.setLat(Double.parseDouble(split[3]));
    __node.setLon(Double.parseDouble(split[4]));
    __node.setHasMediumVoltage(Integer.parseInt(split[7]) == 1);
    __node.setP_max(Double.parseDouble(split[8]));
    __node.setP_min(Double.parseDouble(split[9]));
    __node.setComment(split[10]);
    //
    return __node;
  }

}
