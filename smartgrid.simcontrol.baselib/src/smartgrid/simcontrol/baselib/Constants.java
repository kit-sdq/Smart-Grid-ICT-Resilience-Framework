/**
 *
 */
package smartgrid.simcontrol.baselib;

/**
 * Defines the Constants used as Keys in the {@link org.eclipse.debug.core#ILaunchConfiguration}
 * Dict
 *
 * @author Christian
 *
 */
public final class Constants {

    /*
     * Boolean Strings
     */
    /**
     * String Constant for Boolean True
     */
    public static final String TRUE = "true"; //$NON-NLS-1$
    /**
     * String Constant for Boolean False
     */
    public static final String FALSE = "false"; //$NON-NLS-1$

    /**
     * String Constant for failure
     */
    public static final String FAIL = "fail"; //$NON-NLS-1$

    /*
     * Constants for the KEYs of the Working Copy Dict
     */
    // TODO Change to Dict implementation friendly keys?
    /**
     * Defines the OUTPUT_PATH_KEY
     */
    public static final String OUTPUT_PATH_KEY = "outputPath"; //$NON-NLS-1$
    /**
     * Defines the TOPOLOGY_PATH_KEY
     */
    public static final String TOPOLOGY_PATH_KEY = "topologyPath"; //$NON-NLS-1$
    /**
     * Defines the INPUT_PATH_KEY
     */
    public static final String INPUT_PATH_KEY = "inputPath"; //$NON-NLS-1$

    /**
     *
     * Defines NODE PATH
     *
     * The path to File on Disk with a On EntityStates List
     *
     */
    public static final String NODE_PATH = "nodepath"; //$NON-NLS-1$

    /**
     * Defines the TIMESTEPS_KEY
     */
    public static final String TIMESTEPS_KEY = "timeSteps"; //$NON-NLS-1$

    /**
     * Defines Iteration Count key
     */
    public static final String ITERATION_COUNT_KEY = "iterationCount";

    /**
     * Defines the IGNORE_LOC_CON_KEY
     */
    public static final String IGNORE_LOC_CON_KEY = "logicalCon"; //$NON-NLS-1$

    /**
     * Defines DEFAULT_IGNORE_LOC_CON_KEY
     */
    public static final String DEFAULT_IGNORE_LOC_CON = Constants.TRUE;

    /*
     * Constants for the Default Input/Output File Paths
     */
    /**
     * Defines the DEFAULT_PATH
     */
    public static final String DEFAULT_PATH = ""; //$NON-NLS-1$
    /**
     * Defines the DEFAULT_INPUT_PATH
     */
    public static final String DEFAULT_INPUT_PATH = DEFAULT_PATH;
    /**
     * Defines the DEFAULT_OUTPUT_PATH
     */
    public static final String DEFAULT_OUTPUT_PATH = DEFAULT_PATH;
    /**
     * Defines the DEFAULT_TOPO_PATH
     */
    public static final String DEFAULT_TOPO_PATH = DEFAULT_PATH;
    /**
     * Defines the DEFAULT_TIME_STEPS
     */
    public static final String DEFAULT_TIME_STEPS = "1";
    /**
     * Default number of iterations per time step
     */
    public static final String DEFAULT_ITERATION_COUNT = "2";

    /*
     * Constants for valid Extensions
     */
    /**
     * Defines the TOPO_EXTENSION
     */
    public static final String TOPO_EXTENSION = "smartgridtopo"; //$NON-NLS-1$
    /**
     * Defines the INPUT_EXTENSION
     */
    public static final String INPUT_EXTENSION = "smartgridinput"; //$NON-NLS-1$
    /**
     * Defines the OUTPUT_EXTENSION
     */
    public static final String OUTPUT_EXTENSION = "smartgridoutput"; //$NON-NLS-1$

    /*
     * Constants for Local Hacker
     */
    /**
     * Defines the HACKING_STYLE_KEY
     */
    public static final String HACKING_STYLE_KEY = "desiredHackingStyle"; //$NON-NLS-1$

    /**
     * Defines DEFAULT_HACKING_STYLE
     */
    public static final String DEFAULT_HACKING_STYLE = "BFS_HACKING"; //$NON-NLS-1$

    /**
     * Defines the HACKING_SPEED
     */
    public static final String HACKING_SPEED_KEY = "hackingSpeed"; //$NON-NLS-1$

    /**
     * Defines the DEFAULT_HACKING_SPEED
     */
    public static final String DEFAULT_HACKING_SPEED = "1"; //$NON-NLS-1$

    /**
     * Defines the ROOT_NODE_ID_KEY
     */
    public static final String ROOT_NODE_ID_KEY = "rootNodeID"; //$NON-NLS-1$

    /**
     * Defines DEFAULT_ROOT_NODE_ID
     */
    public static final String DEFAULT_ROOT_NODE_ID = "-1"; //$NON-NLS-1$

    /*
     * Constants for Viral Hacker
     */
    /**
     * Defines DEFAULT_NODE_MODE
     */
    public static final String DEFAULT_NODE_MODE = "RandomNode"; //$NON-NLS-1$
    /**
     * Defines NODE_MODE
     */
    public static final String NODE_MODE = "nodeMode"; //$NON-NLS-1$

    /*
     * Constants for Input Model Generation
     */

    /**
     * Defines GEN_SYNTHETIC_INPUT_KEY
     */
    public static final String GEN_SYNTHETIC_INPUT_KEY = "genInput";

    /**
     * Defines DEFAULT_GEN_SYNTHETIC_INPUT
     */
    public static final String DEFAULT_GEN_SYNTHETIC_INPUT = Constants.FALSE;

    /**
     * Defines GENERATION_STYLE_KEY
     */
    public static final String GENERATION_STYLE_KEY = "GenStyle"; //$NON-NLS-1$

    /**
     * Defines DEFAULT_GENERATION_STYLE
     */
    public static final String DEFAULT_GENERATION_STYLE = "BIG_CLUSTERS"; //$NON-NLS-1$

    /**
     * Defines SMART_METER_COUNT_KEY
     */
    public static final String SMART_METER_COUNT_KEY = "SmartmeterCount"; //$NON-NLS-1$

    /**
     * Defines DEFAULT_SMART_METER_COUNT
     */
    public static final String DEFAULT_SMART_METER_COUNT = "42"; //$NON-NLS-1$

    /**
     * Defines CONTROL_CENTER_COUNT_KEY
     */
    public static final String CONTROL_CENTER_COUNT_KEY = "ControlCenterCount"; //$NON-NLS-1$

    /**
     * Defines DEFAULT_CONTROL_CENTER_COUNT
     */
    public static final String DEFAULT_CONTROL_CENTER_COUNT = "21"; //$NON-NLS-1$

    /**
     * Constants for Extension Point ID's
     */
    public static final String ATTACKER_SIMULATION_EXTENSION_POINT = "smartgrid.simcontrol.extensionpoints.attackerSimulation";
    public static final String IMPACT_ANALYSIS_EXTENSION_POINT = "smartgrid.simcontrol.extensionpoints.impactAnalysis";
    public static final String TERMINATION_CONDITION_EXTENSION_POINT = "smartgrid.simcontrol.extensionpoints.terminationCondition";
    public static final String TIME_PROGRESSOR_EXTENSION_POINT = "smartgrid.simcontrol.extensionpoints.timeProgressor";
    public static final String POWER_LOAD_SIMULATION_EXTENSION_POINT = "smartgrid.simcontrol.extensionpoints.powerLoadSimulation";
    public static final String INPUT_GERNATOR_EXTENSION_POINT = "smartgrid.simcontrol.extensionpoints.inputGenerator";
    public static final String KRITIS_SIMULATION_EXTENSION_POINT = "smartgrid.simucontrol.extensionpoints.kritisSimulation";

    public static final String POWER_LOAD_SIMULATION_CONFIG = "powerLoadConfiguration";
    public static final String ATTACKER_SIMULATION_CONFIG = "attackerSimulationConfiguration";
    public static final String IMPACT_ANALYSIS_SIMULATION_CONFIG = "impactAnalysisConfiguration";
    public static final String TERMINATION_CONDITION_SIMULATION_CONFIG = "terminationConditionConfiguration";
    public static final String TIME_PROGRESSOR_SIMULATION_CONFIG = "timeProgressorConfiguration";
    public static final String INPUT_GENERATOR_SIMULATION_CONFIG = "inputGeneratorConfiguration";
    public static final String KRITIS_SIMULATION_CONFIG = "kritisSimulationConfiguration";

}
