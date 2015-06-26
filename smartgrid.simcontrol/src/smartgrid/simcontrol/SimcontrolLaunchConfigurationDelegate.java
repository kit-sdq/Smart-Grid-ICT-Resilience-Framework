package smartgrid.simcontrol;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

/*TODO Delete after Branching */

//import smartgrid.simcontrol.interfaces.IAttackerSimulation;
//import smartgrid.simcontrol.interfaces.IImpactAnalysis;
//import smartgrid.simcontrol.interfaces.IPowerLoadSimulation;
//import smartgrid.simcontrol.interfaces.ITerminationCondition;
//import smartgrid.simcontrol.interfaces.ITimeProgressor;


import smartgridinput.ScenarioState;
import smartgridinput.impl.SmartgridinputPackageImpl;
import smartgridtopo.Scenario;
import smartgridtopo.impl.SmartgridtopoPackageImpl;

//import Contants from UI 
import smartgrid.simcontrol.ui.*;

/**
 * This class provides the Delegate for the SimControl Approach of the Smartgrid
 * Analysis'
 * 
 * 
 * @author Christian
 * @implements ILaunchConfigurationDelegate
 *
 */
public class SimcontrolLaunchConfigurationDelegate implements
		ILaunchConfigurationDelegate {

	/*
	 * Private Member
	 */
	SimulationController SimController;

	/**
	 * {@inheritDoc}
	 * <P>
	 * 
	 * Launches an SimController Analysis with the given Launch Configuration
	 */
	public void launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException { 

		
		//Code from Interface Hook In Removed -Christian
		
		//this.SimController = new SimulationController(configuration);
		

		
		SimulationController.init(configuration);
		
		SimulationController.run();

		
		// RUN Simulation
		//this.SimController.run();

	}

	@Deprecated
	private Scenario loadScenario(String path) {
		Scenario s = null;
		SmartgridtopoPackageImpl.init();

		// Code Removed - Christian

		ResourceSet resSet = new ResourceSetImpl();
		Resource resource = resSet.getResource(URI.createFileURI(path), true);

		try {
			EObject r = resource.getContents().get(0);
			System.out.println("Klasse: " + r.getClass());
			s = (Scenario) resource.getContents().get(0);

			// for (PhysicalCo)

		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	@Deprecated 
	private ScenarioState loadInput(String path) {
		ScenarioState input = null;
		SmartgridinputPackageImpl.init();

		// Code Removed - Christian

		ResourceSet resSet = new ResourceSetImpl();
		Resource resource = resSet.getResource(URI.createFileURI(path), true);
		try {
			EObject r = resource.getContents().get(0);
			System.out.println("Klasse: " + r.getClass());
			input = (ScenarioState) resource.getContents().get(0);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return input;
	}

}
