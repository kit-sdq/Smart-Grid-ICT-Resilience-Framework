package smartgrid.simcontrol.coupling;

import java.io.Serializable;

public class HighVoltageNodeConnection
  implements Serializable {

////////////////////////////////////////////////////////////////////////////////
//constants definitions
////////////////////////////////////////////////////////////////////////////////
  /** A logger used to redirect debug output. */
  // private static final Logger LOG = LogManager.getLogger(JAreaHandling.class);

////////////////////////////////////////////////////////////////////////////////
// field definitions
////////////////////////////////////////////////////////////////////////////////
  private int id;
  
  private String fromNodeId;
  
  private String toNodeId;
  
  private double voltageLevel;
  
  private double r;
  private double xl;
  private double bc;

  private String type;
  
  private double geomLength;
  private double length;
  
  private double ith;  
  
////////////////////////////////////////////////////////////////////////////////
// constructor, main and initialization
////////////////////////////////////////////////////////////////////////////////

  /**
   * Beanstyle constructor of HighVoltageNode.
   * No parameters required.
   * Calls preInit(), init() and postInit();
   */
  public HighVoltageNodeConnection() {
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
    
  }


  /**
   * @return the id
   */
  public int getId() {
    return id;
  }


  /**
   * @param _id the id to set
   */
  public void setId(int _id) {
    this.id = _id;
  }

  /**
   * @return the fromNodeId
   */
  public String getFromNodeId() {
    return fromNodeId;
  }


  /**
   * @param _fromNodeId the fromNodeId to set
   */
  public void setFromNodeId(String _fromNodeId) {
    this.fromNodeId = _fromNodeId;
  }


  /**
   * @return the toNodeId
   */
  public String getToNodeId() {
    return toNodeId;
  }


  /**
   * @param _toNodeId the toNodeId to set
   */
  public void setToNodeId(String _toNodeId) {
    this.toNodeId = _toNodeId;
  }


  /**
   * @return the voltageLevel
   */
  public double getVoltageLevel() {
    return voltageLevel;
  }


  /**
   * @param _voltageLevel the voltageLevel to set
   */
  public void setVoltageLevel(double _voltageLevel) {
    this.voltageLevel = _voltageLevel;
  }


  /**
   * @return the type
   */
  public String getType() {
    return type;
  }


  /**
   * @param _type the type to set
   */
  public void setType(String _type) {
    this.type = _type;
  }


  /**
   * @return the geomLength
   */
  public double getGeomLength() {
    return geomLength;
  }


  /**
   * @param _geomLength the geomLength to set
   */
  public void setGeomLength(double _geomLength) {
    this.geomLength = _geomLength;
  }


  /**
   * @return the length
   */
  public double getLength() {
    return length;
  }


  /**
   * @param _length the length to set
   */
  public void setLength(double _length) {
    this.length = _length;
  }

  /**
   * @return the r
   */
  public double getR() {
    return r;
  }


  /**
   * @param _r the r to set
   */
  public void setR(double _r) {
    this.r = _r;
  }


  /**
   * @return the xl
   */
  public double getXl() {
    return xl;
  }


  /**
   * @param _xl the xl to set
   */
  public void setXl(double _xl) {
    this.xl = _xl;
  }

  /**
   * @return the bc
   */
  public double getBc() {
    return bc;
  }


  /**
   * @param _bc the bc to set
   */
  public void setBc(double _bc) {
    this.bc = _bc;
  }


  /**
   * @return the ith
   */
  public double getIth() {
    return ith;
  }


  /**
   * @param _ith the ith to set
   */
  public void setIth(double _ith) {
    this.ith = _ith;
  }
  
  public static String getHeader() {
    return "ID,von_ID,von_Standort,nach_ID,nach_Standort,Spannungslevel,Typ,R',XL',BC',geom. L [km],Länge,Ith";
  }

  public String toString() {
    StringBuilder builder;
    String separator;
    //
    separator = ",";
    builder = new StringBuilder();
    builder.append(id);
    builder.append(separator);
    builder.append(fromNodeId);
    builder.append(separator);
    builder.append("");
    builder.append(separator);
    builder.append(toNodeId);
    builder.append(separator);
    builder.append("");
    builder.append(separator);
    builder.append(voltageLevel);
    builder.append(separator);
    builder.append(type);
    builder.append(separator);
    builder.append(r);
    builder.append(separator);
    builder.append(xl);
    builder.append(separator);
    builder.append(bc);
    builder.append(separator);
    builder.append(geomLength);
    builder.append(separator);
    builder.append(length);
    builder.append(separator);
    builder.append(ith);
    //
    return builder.toString();
  }
  
  public static HighVoltageNodeConnection parse(String _line) {
    HighVoltageNodeConnection __connection;
    String[] split;
    //
    split = _line.split(",", -1);
    __connection = new HighVoltageNodeConnection();
    __connection.setId(Integer.parseInt(split[0]));
    __connection.setFromNodeId(split[1]);
    __connection.setToNodeId(split[3]);
    __connection.setVoltageLevel(Double.parseDouble(split[5]));
    __connection.setType(split[6]);
    __connection.setR(Double.parseDouble(split[7]));
    __connection.setXl(Double.parseDouble(split[8]));
    __connection.setBc(Double.parseDouble(split[9]));
    __connection.setGeomLength(Double.parseDouble(split[10]));
    __connection.setLength(Double.parseDouble(split[11]));
    __connection.setIth(Double.parseDouble(split[12]));
    //
    return __connection;
  }
  
}
