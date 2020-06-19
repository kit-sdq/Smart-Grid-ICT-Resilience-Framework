package smartgrid.newsimcontrol.application.commands;

import java.rmi.RemoteException;
import java.util.Map;

import couplingToICT.ISimulationController;
import couplingToICT.PowerAssigned;
import couplingToICT.PowerSpecContainer;
import couplingToICT.SimcontrolException;
import couplingToICT.SimcontrolInitializationException;
import couplingToICT.SmartComponentStateContainer;
import couplingToICT.SmartGridTopoContainer;
import couplingToICT.initializer.InitializationMapKeys;

public class GetModifiedPowerspecsCommand extends ControllerCommand {

	public GetModifiedPowerspecsCommand(ISimulationController controller) {
		super(controller);
	}

	@Override
	public boolean allow() {
		return true;
	}

	@Override
	public boolean checkArguments(String[] args) {
		if (args.length != 6) {
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
		Map<InitializationMapKeys, String> initMap = (Map<InitializationMapKeys, String>) ReadObjectFromFile(args[0]);
		try {
            controller.initConfiguration(initMap);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SimcontrolInitializationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		
		LOG.info("Initializing the topology");
		SmartGridTopoContainer topologyContainer = (SmartGridTopoContainer)ReadObjectFromFile(args[1]);
		try {
            controller.initTopo(topologyContainer);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SimcontrolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		
		LOG.info("Running the simulations and saving the modified powerspecContainer");
		PowerSpecContainer powerSpecs = (PowerSpecContainer)ReadObjectFromFile(args[2]);
		PowerAssigned SMPowerAssigned = (PowerAssigned)ReadObjectFromFile(args[3]);
		PowerSpecContainer powerSpecsModified;
		try {
            powerSpecsModified = controller.getModifiedPowerSpec(powerSpecs, SMPowerAssigned);
            WriteObjectToFile(powerSpecsModified, args[4]);
        } catch (RemoteException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
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
            WriteObjectToFile(scsc, args[5]);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SimcontrolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	
		
	}

}
