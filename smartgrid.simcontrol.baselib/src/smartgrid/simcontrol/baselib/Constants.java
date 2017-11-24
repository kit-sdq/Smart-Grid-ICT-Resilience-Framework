package smartgrid.simcontrol.baselib;

public final class Constants {
    // Boolean Strings
    public static final String TRUE = "true"; //$NON-NLS-1$
    public static final String FALSE = "false"; //$NON-NLS-1$

    public static final String FAIL = "fail"; //$NON-NLS-1$

    // default input/output file paths
    public static final String DEFAULT_PATH = ""; //$NON-NLS-1$
    public static final String INPUT_PATH_KEY = "inputPath"; //$NON-NLS-1$
    public static final String DEFAULT_INPUT_PATH = DEFAULT_PATH;
    public static final String OUTPUT_PATH_KEY = "outputPath"; //$NON-NLS-1$
    public static final String DEFAULT_OUTPUT_PATH = DEFAULT_PATH;
    public static final String TOPO_PATH_KEY = "topologyPath"; //$NON-NLS-1$
    public static final String DEFAULT_TOPO_PATH = DEFAULT_PATH;

    // file extensions
    public static final String TOPO_EXTENSION = "smartgridtopo"; //$NON-NLS-1$
    public static final String INPUT_EXTENSION = "smartgridinput"; //$NON-NLS-1$
    public static final String OUTPUT_EXTENSION = "smartgridoutput"; //$NON-NLS-1$

    // further settings
    public static final String IGNORE_LOC_CON_KEY = "logicalCon"; //$NON-NLS-1$
    public static final String DEFAULT_IGNORE_LOC_CON = Constants.TRUE;
    public static final String TIME_STEPS_KEY = "timeSteps"; //$NON-NLS-1$
    public static final String DEFAULT_TIME_STEPS = "1";
    public static final String ITERATION_COUNT_KEY = "iterationCount";
    public static final String DEFAULT_ITERATION_COUNT = "2";
    public static final String SMARTMETER_COMPLETION_KEY = "smartmeter completion";
    public static final String DEFAULT_SMARTMETER_COMPLETION = FALSE;
    public static final String TOPO_GENERATION_KEY = "topo generation";
    public static final boolean DEFAULT_TOPO_GENERATION = false;

    // The path to File on Disk with a On EntityStates List
    // TODO is this setting broken, dead or not correctly implemented?
    public static final String NODE_PATH_KEY = "nodepath";

    // local hacker
    public static final String HACKING_STYLE_KEY = "desiredHackingStyle"; //$NON-NLS-1$
    public static final String DEFAULT_HACKING_STYLE = "BFS_HACKING"; //$NON-NLS-1$
    public static final String HACKING_SPEED_KEY = "hackingSpeed"; //$NON-NLS-1$
    public static final String DEFAULT_HACKING_SPEED = "1"; //$NON-NLS-1$
    public static final String ROOT_NODE_ID_KEY = "rootNodeID"; //$NON-NLS-1$
    public static final String DEFAULT_ROOT_NODE_ID = "-1"; //$NON-NLS-1$

    // viral hacker
    public static final String DEFAULT_NODE_MODE = "RandomNode"; //$NON-NLS-1$
    public static final String NODE_MODE = "nodeMode"; //$NON-NLS-1$

    // extension point IDs
    public static final String ATTACKER_SIMULATION_EXTENSION_POINT = "smartgrid.simcontrol.extensionpoints.attackerSimulation";
    public static final String IMPACT_ANALYSIS_EXTENSION_POINT = "smartgrid.simcontrol.extensionpoints.impactAnalysis";
    public static final String TERMINATION_CONDITION_EXTENSION_POINT = "smartgrid.simcontrol.extensionpoints.terminationCondition";
    public static final String TIME_PROGRESSOR_EXTENSION_POINT = "smartgrid.simcontrol.extensionpoints.timeProgressor";
    public static final String POWER_LOAD_SIMULATION_EXTENSION_POINT = "smartgrid.simcontrol.extensionpoints.powerLoadSimulation";
    public static final String INPUT_GERNATOR_EXTENSION_POINT = "smartgrid.simcontrol.extensionpoints.inputGenerator";
    public static final String KRITIS_SIMULATION_EXTENSION_POINT = "smartgrid.simucontrol.extensionpoints.kritisSimulation";

    // extension point keys
    public static final String POWER_LOAD_SIMULATION_KEY = "powerLoadConfiguration";
    public static final String ATTACKER_SIMULATION_KEY = "attackerSimulationConfiguration";
    public static final String IMPACT_ANALYSIS_SIMULATION_KEY = "impactAnalysisConfiguration";
    public static final String TERMINATION_CONDITION_SIMULATION_KEY = "terminationConditionConfiguration";
    public static final String TIME_PROGRESSOR_SIMULATION_KEY = "timeProgressorConfiguration";
    public static final String KRITIS_SIMULATION_KEY = "kritisSimulationConfiguration";

    // shared simulation component names
    public static final String IIP_OPF_NAME = "Power Load Simulation Wrapper";
    public static final String KRITIS_SIMULATION_NAME = "KRITIS Remote Simulation Wrapper";
}
