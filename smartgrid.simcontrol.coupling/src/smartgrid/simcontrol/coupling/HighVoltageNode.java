package smartgrid.simcontrol.coupling;

import java.util.ArrayList;

public class HighVoltageNode {

////////////////////////////////////////////////////////////////////////////////
//constants definitions
////////////////////////////////////////////////////////////////////////////////
  /** A logger used to redirect debug output. */
  // private static final Logger LOG = LogManager.getLogger(JAreaHandling.class);

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
    
  }

}
