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
		Object obj1 = ReadObjectFromFile(args[0]);
		Object obj2 = ReadObjectFromFile(args[1]);
		Object obj3 = ReadObjectFromFile(args[2]);
		Object obj4 = ReadObjectFromFile(args[3]);
		if (obj1 instanceof Map<?,?> && obj2 instanceof SmartGridTopoContainer 
				&& obj3 instanceof PowerSpecContainer && obj4 instanceof PowerAssigned) 
			return true;
		else
			return false;
	}

	@Override
	public void doCommand(String[] args) throws SimcontrolException, InterruptedException {
		LOG.info("Initializing the local controller");
		@SuppressWarnings("unchecked")
		Map<InitializationMapKeys, String> initMap = (Map<InitializationMapKeys, String>) ReadObjectFromFile(args[0]);
		controller.initConfiguration(initMap);
        
		
		LOG.info("Initializing the topology");
		SmartGridTopology topo = EObjectsHelper.loadTopology(args[1]);
		ScenarioState state = EObjectsHelper.loadInput(args[2]);
		controller.SetTopo(topo);
		controller.SetInitalState(state);
		controller.setImpactInput(state);
		
		LOG.info("Running the simulations and saving the modified powerspecContainer");
		PowerSpecContainer powerSpecs = (PowerSpecContainer)ReadObjectFromFile(args[3]);
		PowerAssigned SMPowerAssigned = (PowerAssigned)ReadObjectFromFile(args[4]);
		PowerSpecContainer powerSpecsModified;
		try {
            powerSpecsModified = controller.getModifiedPowerSpec(powerSpecs, SMPowerAssigned);
            WriteObjectToFile(powerSpecsModified, args[5]);
        } catch (SimcontrolException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
		
		LOG.info("Saving the dysfunctional smartcomponents");
		SmartComponentStateContainer scsc;
		try {
            scsc = controller.getDysfunctSmartComponents();
            WriteObjectToFile(scsc, args[6]);
        } catch (SimcontrolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	
		
	}

}
