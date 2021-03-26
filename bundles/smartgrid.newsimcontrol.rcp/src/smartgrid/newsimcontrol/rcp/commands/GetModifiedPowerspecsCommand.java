package smartgrid.newsimcontrol.rcp.commands;

import java.util.Map;

import couplingToICT.PowerAssigned;
import couplingToICT.PowerSpecContainer;
import couplingToICT.SimcontrolException;
import couplingToICT.SmartComponentStateContainer;
import couplingToICT.SmartGridTopoContainer;
import couplingToICT.initializer.InitializationMapKeys;
import smartgrid.newsimcontrol.controller.LocalController;
import smartgrid.newsimcontrol.rcp.helper.EObjectsHelper;
import smartgridinput.ScenarioState;
import smartgridtopo.SmartGridTopology;

public class GetModifiedPowerspecsCommand extends ControllerCommand {

	public GetModifiedPowerspecsCommand(LocalController controller) {
		super(controller);
	}

	@Override
	public boolean allow() {
		return true;
	}

	@Override
	public boolean checkArguments(String[] args) {
		if (args.length != 7) {
        	LOG.error("The correct number of arguments isn't correct."
        			+ "For further info see the readme file");
			return false;
		}
		Object obj1 = readObjectFromFile(args[0]);
		Object obj2 = readObjectFromFile(args[3]);
		Object obj3 = readObjectFromFile(args[4]);
		return (obj1 instanceof Map<?,?> && obj2 instanceof PowerSpecContainer 
				&& obj3 instanceof PowerAssigned);
	}

	@Override
	public void doCommand(String[] args) throws SimcontrolException, InterruptedException {
		LOG.info("Initializing the local controller");
		@SuppressWarnings("unchecked")
		Map<InitializationMapKeys, String> initMap = (Map<InitializationMapKeys, String>) readObjectFromFile(args[0]);
		controller.initConfiguration(initMap);
        
		
		LOG.info("Initializing the topology");
		SmartGridTopology topo = EObjectsHelper.loadTopology(args[1]);
		ScenarioState state = EObjectsHelper.loadInput(args[2]);
		controller.setTopo(topo);
		controller.setInitalState(state);
		controller.setImpactInput(state);
		
		LOG.info("Running the simulations and saving the modified powerspecContainer");
		PowerSpecContainer powerSpecs = (PowerSpecContainer)readObjectFromFile(args[3]);
		PowerAssigned sMPowerAssigned = (PowerAssigned)readObjectFromFile(args[4]);
		PowerSpecContainer powerSpecsModified;
		try {
            powerSpecsModified = controller.getModifiedPowerSpec(powerSpecs, sMPowerAssigned);
            writeObjectToFile(powerSpecsModified, args[5]);
            EObjectsHelper.saveToFileSystem(topo, args[1]);
            EObjectsHelper.saveToFileSystem(state, args[2]);
    		LOG.info("Saving the dysfunctional smartcomponents");
    		SmartComponentStateContainer scsc;
		    scsc = controller.getDysfunctSmartComponents();
            writeObjectToFile(scsc, args[6]);
        } catch (SimcontrolException|InterruptedException e1) {
            e1.printStackTrace();
        }
		
	}

}
