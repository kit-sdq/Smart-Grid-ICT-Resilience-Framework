package smartgrid.newsimcontrol.controller;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.osgi.service.component.annotations.Component;

import couplingToICT.ICTElement;
import couplingToICT.ISimulationController;
import couplingToICT.PowerAssigned;
import couplingToICT.PowerSpecContainer;
import couplingToICT.SimcontrolException;
import couplingToICT.SimcontrolInitializationException;
import couplingToICT.SmartComponentStateContainer;
import couplingToICT.SmartGridTopoContainer;
import couplingToICT.initializer.AttackerSimulationsTypes;
import couplingToICT.initializer.HackingStyle;
import couplingToICT.initializer.InitializationMapKeys;
import couplingToICT.initializer.PowerSpecsModificationTypes;


/**
 * Local Implementation of {@link ISimulationController}
 * 
 * @author majuwa
 *
 */
//TODO Refactor to remove Code-Duplicates from RmiServer

@Component
public class LocalController implements ISimulationController {
    private static final Logger LOG = Logger.getLogger(LocalController.class);
    private ReactiveSimulationController reactiveSimControl;

    @Override
    public SmartComponentStateContainer getDysfunctSmartComponents()
            throws RemoteException, SimcontrolException, InterruptedException {
        return reactiveSimControl.getDysfunctionalcomponents();
    }

    @Override
    public PowerSpecContainer getModifiedPowerSpec(PowerSpecContainer powerSpecs, PowerAssigned SMPowerAssigned)
            throws RemoteException, SimcontrolException, InterruptedException {
        reactiveSimControl.run(SMPowerAssigned);
        return reactiveSimControl.modifyPowerSpecContainer(powerSpecs);
    }

    @Override
    public void initConfiguration(Map<InitializationMapKeys, String> initMap)
            throws RemoteException, SimcontrolInitializationException {
        reactiveSimControl = new ReactiveSimulationController();

        // Values in the map
        String outputPath = null;
        String topoPath = "";
        String inputStatePath = "";
        String ignoreLogicalConnections = "false";
        String attackerType = AttackerSimulationsTypes.NO_ATTACK_SIMULATION.getDescription();
        String hackingStype = null;
        String hackingSpeed = "1";
        boolean generateTopo = false;
        int timeSteps = 1;
        String powerSpecsModificationType = PowerSpecsModificationTypes.NO_CHANGE_MODIFIER.getDescription();
        String rootNode = "";
        String impactAnalysis = "Graph Analyzer Impact Analysis";
        String timeProgresser = "No Operation";

        // createLaunchConfig
        final ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
        final ILaunchConfigurationType type = manager
                .getLaunchConfigurationType("smartgrid.simcontrol.test.SimcontrolLaunchConfigurationType");
        ILaunchConfigurationWorkingCopy workingCopy = null;
        try {
            workingCopy = type.newInstance(null, "testInstance");
        } catch (CoreException e1) {
            throw new SimcontrolInitializationException("Creating eclipse launcher failed", e1);
        }
        // TODO check if minimal necessary set is in Map
        // fill values in the working copy
        for (InitializationMapKeys key : initMap.keySet()) {
            if (key.equals(InitializationMapKeys.INPUT_PATH_KEY)) {
                inputStatePath = initMap.get(key);
            } else if (key.equals(InitializationMapKeys.TOPO_PATH_KEY)) {
                topoPath = initMap.get(key);
            } else if (key.equals(InitializationMapKeys.OUTPUT_PATH_KEY)) {
                outputPath = initMap.get(key);
            } else if (key.equals(InitializationMapKeys.IGNORE_LOC_CON_KEY)) {
                ignoreLogicalConnections = initMap.get(key);
            } else if (key.equals(InitializationMapKeys.ATTACKER_SIMULATION_KEY)) {
                attackerType = AttackerSimulationsTypes.valueOf(initMap.get(key)).getDescription();
            } else if (key.equals(InitializationMapKeys.HACKING_STYLE_KEY)
                    && initMap.get(InitializationMapKeys.ATTACKER_SIMULATION_KEY) != null
                    && AttackerSimulationsTypes.valueOf(initMap.get(
                            InitializationMapKeys.ATTACKER_SIMULATION_KEY)) != (AttackerSimulationsTypes.NO_ATTACK_SIMULATION)) {
                hackingStype = HackingStyle.valueOf(initMap.get(key)).getDescription();
            } else if (key.equals(InitializationMapKeys.HACKING_SPEED_KEY)) {
                hackingSpeed = initMap.get(key);
            } else if (key.equals(InitializationMapKeys.TOPO_GENERATION_KEY)) {
                generateTopo = Boolean.valueOf(initMap.get(key));
            } else if (key.equals(InitializationMapKeys.TIME_STEPS_KEY)) {
                timeSteps = Integer.valueOf(initMap.get(key));
            } else if (key.equals(InitializationMapKeys.POWER_MODIFY_KEY)) {
                powerSpecsModificationType = PowerSpecsModificationTypes.valueOf(initMap.get(key)).getDescription();
            } else if (key.equals(InitializationMapKeys.ROOT_NODE_ID_KEY)) {
                rootNode = initMap.get(key);
            }
        }
        if (outputPath == null) {
            outputPath = System.getProperty("java.io.tmpdir");
            outputPath += File.separator + "smargrid" + System.currentTimeMillis();
        }
        reactiveSimControl.init(outputPath);
        if (generateTopo == false) {
            reactiveSimControl.initModelsFromFiles(topoPath, inputStatePath);
        }

        workingCopy.setAttribute(InitializationMapKeys.INPUT_PATH_KEY.getDescription(), inputStatePath);
        workingCopy.setAttribute(InitializationMapKeys.TOPO_PATH_KEY.getDescription(), topoPath);
        workingCopy.setAttribute(InitializationMapKeys.OUTPUT_PATH_KEY.getDescription(), outputPath);
        workingCopy.setAttribute(InitializationMapKeys.IGNORE_LOC_CON_KEY.getDescription(), ignoreLogicalConnections);
        workingCopy.setAttribute(InitializationMapKeys.ATTACKER_SIMULATION_KEY.getDescription(), attackerType);
        if (hackingStype != null) {
            workingCopy.setAttribute(InitializationMapKeys.HACKING_STYLE_KEY.getDescription(), hackingStype);
        }
        workingCopy.setAttribute(InitializationMapKeys.HACKING_SPEED_KEY.getDescription(), hackingSpeed);
        workingCopy.setAttribute(InitializationMapKeys.TOPO_GENERATION_KEY.getDescription(), generateTopo);
        workingCopy.setAttribute(InitializationMapKeys.TIME_STEPS_KEY.getDescription(), timeSteps);
        workingCopy.setAttribute(InitializationMapKeys.POWER_MODIFY_KEY.getDescription(), powerSpecsModificationType);
        workingCopy.setAttribute(InitializationMapKeys.ROOT_NODE_ID_KEY.getDescription(), rootNode);
        workingCopy.setAttribute(InitializationMapKeys.IMPACT_ANALYSIS_SIMULATION_KEY.getDescription(), impactAnalysis);
        workingCopy.setAttribute(InitializationMapKeys.TIME_PROGRESSOR_SIMULATION_KEY.getDescription(), timeProgresser);
        try {
            // create launch configuration
            final ILaunchConfiguration config = workingCopy.doSave();
            reactiveSimControl.loadCustomUserAnalysis(config);
        } catch (InterruptedException e) {
            throw new SimcontrolInitializationException("Simcontrol failed to initialize all simulation components.",
                    e);
        } catch (CoreException e) {
            throw new SimcontrolInitializationException("Creating eclipse launcher failed", e);
        }

    }

    @Override
    public Collection<ICTElement> initTopo(SmartGridTopoContainer topologyContainer)
            throws RemoteException, SimcontrolException {
        if (topologyContainer == null) {
            LOG.warn("Topo Container is null");
        } else {
            return reactiveSimControl.initTopo(topologyContainer);
        }
        return null;
    }

    //@Override
    public void initConfiguration_new(Map<InitializationMapKeys, String> initMap){
        reactiveSimControl = new ReactiveSimulationController();

        // Values in the map
        String outputPath = null;
        String topoPath = "";
        String inputStatePath = "";
        boolean generateTopo = false;
        
        // fill values in the working copy
        for (InitializationMapKeys key : initMap.keySet()) {
            if (key.equals(InitializationMapKeys.INPUT_PATH_KEY)) {
                inputStatePath = initMap.get(key);
            } else if (key.equals(InitializationMapKeys.TOPO_PATH_KEY)) {
                topoPath = initMap.get(key);
            } else if (key.equals(InitializationMapKeys.OUTPUT_PATH_KEY)) {
                outputPath = initMap.get(key);
            } else if (key.equals(InitializationMapKeys.TOPO_GENERATION_KEY)) {
                generateTopo = Boolean.valueOf(initMap.get(key));
            }
        }
        if (outputPath == null) {
            outputPath = System.getProperty("java.io.tmpdir");
            outputPath += File.separator + "smargrid" + System.currentTimeMillis();
        }
        reactiveSimControl.init(outputPath);
        if (generateTopo == false) {
            reactiveSimControl.initModelsFromFiles(topoPath, inputStatePath);
        }
       
        
	    // create launch configuration
	    reactiveSimControl.loadCustomUserAnalysis_new(initMap);
        

    }
}
