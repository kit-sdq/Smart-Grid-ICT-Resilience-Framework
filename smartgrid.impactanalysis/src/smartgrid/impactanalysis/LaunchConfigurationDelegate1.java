package smartgrid.impactanalysis;

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

import smartgridinput.ScenarioState;
import smartgridinput.impl.SmartgridinputPackageImpl;
import smartgridtopo.SmartGridTopology;
import smartgridtopo.impl.SmartgridtopoPackageImpl;
//import smartgrid.simcontrol.*; //Imports the SimControl Package to use the Interface based hook --to be changed (eventually) to Extension Point based 
//SimControl Approach was outsourced --see smartgrid.simcontrol

/**
 * This Class is the Delegate that launches an Impact Analysis.
 * 
 * @implements ILaunchConfigurationDelegate
 * 
 * @author Torsten
 * @author Christian (modified)
 *
 */
public class LaunchConfigurationDelegate1 implements ILaunchConfigurationDelegate {

	/**
	 * {@inheritDoc}
	 * <p>
	 * 
	 * Launches an Impact Analysis with the given Launch Configuration See
	 * {@link GraphAnalyzer} for Details about the Analysis
	 */
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {

		System.out.println("GELADEN");
		System.out.println("Finde Parameter");

		String inputPath = configuration.getAttribute("inputPath", "");
		String outputPath = configuration.getAttribute("outputPath", "");
		String topoPath = configuration.getAttribute("topologyPath", "");
		System.out.println("Input : " + inputPath);
		System.out.println("Topology : " + topoPath);

		ScenarioState initialState = loadInput(inputPath);
		SmartGridTopology topo = loadScenario(topoPath);

		GraphAnalyzer ana = new GraphAnalyzer(outputPath);
		ana.analyze(topo, initialState);
	}

	private SmartGridTopology loadScenario(String path) {
		SmartGridTopology s = null;
		SmartgridtopoPackageImpl.init();

		// Code Removed - Christian

		ResourceSet resSet = new ResourceSetImpl();
		Resource resource = resSet.getResource(URI.createFileURI(path), true);

		try {
			EObject r = resource.getContents().get(0);
			System.out.println("Klasse: " + r.getClass());
			s = (SmartGridTopology) resource.getContents().get(0);

			// for (PhysicalCo)

		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

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

	// Code Removed Christian
}