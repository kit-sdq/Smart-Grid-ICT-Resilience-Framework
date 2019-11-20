package smartgrid.simcontrol.test.baselib;

import couplingToICT.initializer.HackingStyle;

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
    public static final String ITERATION_COUNT_KEY = "iterationCount"; //TODO:WEG
    public static final String DEFAULT_ITERATION_COUNT = "2"; //TODO:WEG
    public static final String SMARTMETER_COMPLETION_KEY = "smartmeter completion"; //TODO:Auskommentieren->WEG
    public static final String DEFAULT_SMARTMETER_COMPLETION = FALSE;
    public static final String TOPO_GENERATION_KEY = "topo generation";
    public static final boolean DEFAULT_TOPO_GENERATION = false;

    // local hacker
    public static final String HACKING_STYLE_KEY = "desiredHackingStyle"; //$NON-NLS-1$
    public static final String DEFAULT_HACKING_STYLE = HackingStyle.FULLY_MESHED_HACKING.toString(); //$NON-NLS-1$
    public static final String HACKING_SPEED_KEY = "hackingSpeed"; //$NON-NLS-1$
    public static final String DEFAULT_HACKING_SPEED = "1"; //$NON-NLS-1$
    public static final String ROOT_NODE_ID_KEY = "rootNodeID"; //$NON-NLS-1$
    public static final String NO_ROOT_NODE_ID = ""; //$NON-NLS-1$
    public static final String DEFAULT_ROOT_NODE_ID = NO_ROOT_NODE_ID; //$NON-NLS-1$

    // extension point IDs
    public static final String ATTACKER_SIMULATION_EXTENSION_POINT = "smartgrid.simcontrol.test.extensionpoints.attackerSimulation";
    public static final String IMPACT_ANALYSIS_EXTENSION_POINT = "smartgrid.simcontrol.test.extensionpoints.impactAnalysis";
    public static final String TERMINATION_CONDITION_EXTENSION_POINT = "smartgrid.simcontrol.test.extensionpoints.terminationCondition"; //TODO:WEG
    public static final String TIME_PROGRESSOR_EXTENSION_POINT = "smartgrid.simcontrol.test.extensionpoints.timeProgressor";
    public static final String POWER_LOAD_SIMULATION_EXTENSION_POINT = "smartgrid.simcontrol.test.extensionpoints.powerLoadSimulation"; //TODO:WEG
    //public static final String INPUT_GERNATOR_EXTENSION_POINT = "smartgrid.simcontrol.test.extensionpoints.inputGenerator"; //Currently unused
    public static final String KRITIS_SIMULATION_EXTENSION_POINT = "smartgrid.simucontrol.test.extensionpoints.kritisSimulation"; //TODO:WEG

    // extension point keys
    public static final String POWER_LOAD_SIMULATION_KEY = "powerLoadConfiguration"; //TODO:WEG
    public static final String ATTACKER_SIMULATION_KEY = "attackerSimulationConfiguration";
    public static final String IMPACT_ANALYSIS_SIMULATION_KEY = "impactAnalysisConfiguration";
    public static final String TERMINATION_CONDITION_SIMULATION_KEY = "terminationConditionConfiguration"; //TODO:WEG
    public static final String TIME_PROGRESSOR_SIMULATION_KEY = "timeProgressorConfiguration";
    public static final String KRITIS_SIMULATION_KEY = "kritisSimulationConfiguration"; //TODO:WEG

    // shared simulation component names
    public static final String IIP_OPF_NAME = "Power Load Simulation Wrapper"; //TODO:WEG
    public static final String KRITIS_SIMULATION_NAME = "KRITIS Remote Simulation Wrapper"; //TODO:WEG
}
