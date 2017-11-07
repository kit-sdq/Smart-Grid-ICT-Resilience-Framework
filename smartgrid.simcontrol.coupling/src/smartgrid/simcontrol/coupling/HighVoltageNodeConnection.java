package smartgrid.simcontrol.coupling;

public class HighVoltageNodeConnection {

////////////////////////////////////////////////////////////////////////////////
//constants definitions
////////////////////////////////////////////////////////////////////////////////
  /** A logger used to redirect debug output. */
  // private static final Logger LOG = LogManager.getLogger(JAreaHandling.class);

////////////////////////////////////////////////////////////////////////////////
// field definitions
////////////////////////////////////////////////////////////////////////////////
  private int id;
  
  private HighVoltageNode fromNode;
  
  private HighVoltageNode toNode;
  
  private double voltageLevel;
  
  private String type;
  
  private double length;
  
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

}
