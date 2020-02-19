package smartgrid.newsimcontrol.controller;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.osgi.service.component.annotations.Component;

import couplingToICT.ICTElement;
import couplingToICT.ISimulationController;
import couplingToICT.PowerAssigned;
import couplingToICT.PowerSpecContainer;
import couplingToICT.SimcontrolException;
import couplingToICT.SmartComponentStateContainer;
import couplingToICT.SmartGridTopoContainer;
import couplingToICT.initializer.InitializationMapKeys;


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
    public Collection<ICTElement> initTopo(SmartGridTopoContainer topologyContainer)
            throws RemoteException, SimcontrolException {
        if (topologyContainer == null) {
            LOG.warn("Topo Container is null");
        } else {
            return reactiveSimControl.initTopo(topologyContainer);
        }
        return null;
    }

    @Override
    public void initConfiguration(Map<InitializationMapKeys, String> initMap){
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
       
	    try {
			reactiveSimControl.loadCustomUserAnalysis(initMap);
		} catch (CoreException e) {
			LOG.error("Error while intializing the simulations");
		}
        

    }
}
