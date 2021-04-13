package smartgrid.newsimcontrol.controller;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

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
import smartgridinput.ScenarioState;
import smartgridtopo.SmartGridTopology;


/**
 * Local Implementation of {@link ISimulationController}
 * 
 * @author majuwa
 *
 */
//TODO Refactor to remove Code-Duplicates from RmiServer

@Component
public class LocalController implements ISimulationController{

    private static final Logger LOG = Logger.getLogger(LocalController.class);
    private ReactiveSimulationController reactiveSimControl;

    //1
    @Override
    public PowerSpecContainer getModifiedPowerSpec(PowerSpecContainer powerSpecs, PowerAssigned sMPowerAssigned)
            throws SimcontrolException, InterruptedException {
    	//TODO Check PowerSpecs wegen PowerdistrictId, smartID
    	reactiveSimControl.run(sMPowerAssigned);
        return reactiveSimControl.modifyPowerSpecContainer(powerSpecs);
    }
    
    //2
    @Override
    public SmartComponentStateContainer getDysfunctSmartComponents()
            throws SimcontrolException, InterruptedException {
        return reactiveSimControl.getDysfunctionalcomponents();
    }

    

    @Override
    public Collection<ICTElement> initTopo(SmartGridTopoContainer topologyContainer)
            throws SimcontrolException {
        if (topologyContainer == null) {
            LOG.warn("Topo Container is null");
        } else {
            return reactiveSimControl.initTopo(topologyContainer);
        }
        return new LinkedList<>();
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
        for (Entry<InitializationMapKeys, String> entry : initMap.entrySet()) {
            if (entry.getKey().equals(InitializationMapKeys.INPUT_PATH_KEY)) {
                inputStatePath = entry.getValue();
            } else if (entry.getKey().equals(InitializationMapKeys.TOPO_PATH_KEY)) {
                topoPath = entry.getValue();
            } else if (entry.getKey().equals(InitializationMapKeys.OUTPUT_PATH_KEY)) {
                outputPath = entry.getValue();
            } else if (entry.getKey().equals(InitializationMapKeys.TOPO_GENERATION_KEY)) {
                generateTopo = Boolean.valueOf(entry.getValue());
            }
        }
        if (outputPath == null) {
            outputPath = System.getProperty("java.io.tmpdir");
            outputPath += File.separator + "smargrid" + System.currentTimeMillis();
        }
        reactiveSimControl.init(outputPath);
        if (!generateTopo) {
            reactiveSimControl.initModelsFromFiles(topoPath, inputStatePath);
        }
       
	    try {
			reactiveSimControl.loadCustomUserAnalysis(initMap);
		} catch (CoreException e) {
			LOG.error("Error while intializing the simulations");
		}
        

    }
    
    public SmartGridTopology getTopo() {
        return reactiveSimControl.getTopo();
    }
    
    public ScenarioState getInitalState(){
        return reactiveSimControl.getInitialState();
    }
    
    public void setTopo(SmartGridTopology topo) {
    	reactiveSimControl.setTopo(topo);
    }
    
    public void setInitalState(ScenarioState initialState) {
    	reactiveSimControl.setInitialState(initialState);
    }
    
    public void setImpactInput(ScenarioState impactInput) {
    	reactiveSimControl.setImpactInput(impactInput);
    }
    
    
}
