package smartgrid.impactanalysis;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
// import org.eclipse.emf.common.util.URI;
// import org.eclipse.emf.ecore.resource.Resource;
// import org.eclipse.emf.ecore.resource.ResourceSet;
// import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import smartgrid.helper.FileSystem;
import smartgrid.model.helper.input.LoadInputModelConformityHelper;
import smartgrid.simcontrol.baselib.Constants;
import smartgrid.simcontrol.interfaces.ErrorCodeEnum;
import smartgrid.simcontrol.interfaces.IImpactAnalysis;
import smartgridinput.EntityState;
import smartgridinput.PowerState;
import smartgridinput.ScenarioState;
import smartgridoutput.Cluster;
import smartgridoutput.NoUplink;
import smartgridoutput.Online;
import smartgridoutput.ScenarioResult;
import smartgridoutput.SmartgridoutputFactory;
import smartgridoutput.impl.SmartgridoutputPackageImpl;
import smartgridtopo.ControlCenter;
import smartgridtopo.LogicalCommunication;
import smartgridtopo.NetworkEntity;
import smartgridtopo.PhysicalConnection;
import smartgridtopo.PowerGridNode;
import smartgridtopo.SmartGridTopology;

/**
 * This Class does an Impact Analysis.
 * 
 * The Target of this Analysis is to get to know which SmartMeters can
 * communicate with which ControlCenters respectively other SmartMeters For more
 * Details see the Model Description in the @see smartgrid.model.* packages
 * 
 * @author Torsten
 * @author Christian (modified)
 * @implements IImpactAnalysis
 */
public class GraphAnalyzer implements IImpactAnalysis {

	private static final Logger LOG = Logger.getLogger(GraphAnalyzer.class);

	private Map<Integer, PowerState> powerStates;
	private Map<Integer, EntityState> entityStates;

	private Map<Integer, Integer> externalToInternalID;
	private Map<Integer, Integer> internalToExternalID;
	private Map<Integer, Cluster> internalToCluster;
	private Map<Integer, Double[]> controlCenterConnectivity;
	//
	private String outputPath;
	private boolean initDone = false;
	private boolean ignoreLogicalConnections;

	List<List<Integer>> physicalClusters;
	List<List<Integer>> logicalClusters;

	// Die Adjazenzmatrizen waren noch vom vorherigen Ansatz
	int maxID = -1;
	double[][] adjacentMatrix;
	double[][] logicalAdjacentMatrix;
	List<Integer> logicalNodes;
	List<Integer> controlCenters;
	int internalMaxID;

	/**
	 * For ExtensionPoints .. use this together with the init() Method
	 */
	public GraphAnalyzer() {
	}

	/**
	 * The Constructor of the Class. For use with the old Impact Analysy only
	 * Version {@link smartgrid.impactanalysis.ui}
	 * 
	 * @deprecated It's now integrated in the SimControl Approach
	 * @param outputPath
	 *            In case of using {@code public void analyze(...)} Method it is
	 *            the Path to the Outputfile
	 */
	public GraphAnalyzer(String outputPath) {

		// Attention cloned from init() to be downward compatible
		internalMaxID = 0;
		powerStates = new HashMap<Integer, PowerState>();
		entityStates = new HashMap<Integer, EntityState>();
		controlCenters = new LinkedList<Integer>();
		internalToExternalID = new HashMap<Integer, Integer>();
		externalToInternalID = new HashMap<Integer, Integer>();
		internalToCluster = new HashMap<Integer, Cluster>();
		logicalNodes = new LinkedList<Integer>();
		controlCenterConnectivity = new HashMap<Integer, Double[]>();

		// TODO Overload Contructor to have 2 one for SimController and one for
		// ImpactAnalysis only?
		this.outputPath = outputPath;
		initDone = true;
		// Do it always with logical Connection
		ignoreLogicalConnections = false;
	}

	/**
	 * 
	 */
	@Override
	public ErrorCodeEnum init(ILaunchConfiguration config) throws CoreException {

		ErrorCodeEnum myError = ErrorCodeEnum.NOT_SET;

		internalMaxID = 0;
		powerStates = new HashMap<Integer, PowerState>();
		entityStates = new HashMap<Integer, EntityState>();
		controlCenters = new LinkedList<Integer>();
		internalToExternalID = new HashMap<Integer, Integer>();
		externalToInternalID = new HashMap<Integer, Integer>();
		internalToCluster = new HashMap<Integer, Cluster>();
		logicalNodes = new LinkedList<Integer>();
		controlCenterConnectivity = new HashMap<Integer, Double[]>();

		String ignoreLogicalConnectionsString = config.getAttribute(Constants.IGNORE_LOC_CON_KEY, Constants.FAIL);

		if (ignoreLogicalConnectionsString.equals(Constants.FAIL)) {
			myError = ErrorCodeEnum.DEFAULT_VALUES_USED;

			// Checks whether DEFAULT_IGNORE_LOC_CON_KEY is true and assigns it
			this.ignoreLogicalConnections = (Constants.TRUE).equals(Constants.DEFAULT_IGNORE_LOC_CON);
		} else {
			// checks whether ignoreLogicalConnectionsString is true and assigns
			// it
			this.ignoreLogicalConnections = (Constants.TRUE).equals(ignoreLogicalConnectionsString);
		}

		LOG.debug("Init done");
		this.initDone = true;

		return myError;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * 
	 * 
	 * 
	 */
	@Override
	public ScenarioResult run(SmartGridTopology smartGridTopo, ScenarioState impactAnalysisInput) {

		assert (initDone) : "Init wasn't run! Run init() first !";

		LOG.debug("[GraphAnalyzer]: Start impact analysis");

		clearAll();

		if (!(LoadInputModelConformityHelper.checkInputModelConformity(impactAnalysisInput, smartGridTopo))) {

			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				public void run() {
					Shell activeShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
					MessageBox messageBox = new MessageBox(activeShell, SWT.ICON_ERROR);
					messageBox.setMessage("Input model is not conform to the current topo model");
					messageBox.open();
				}
			});

			return null;
		}

		// Szenario einlesen
		readStates(smartGridTopo, impactAnalysisInput);
		// now slowly build adjacent matrix
		adjacentMatrix = new double[internalMaxID][internalMaxID];
		logicalAdjacentMatrix = new double[internalMaxID][internalMaxID];
		for (int i = 0; i < internalMaxID; i++) {
			for (int j = 0; j < internalMaxID; j++) {
				adjacentMatrix[i][j] = 0;
				logicalAdjacentMatrix[i][j] = 0;
			}
		}
		// Es werden insgesamt 2 mal Cluster gebildet. Einmal �ber die
		// physikalischen Verbindungen, dann �ber die logischen
		readPhysicalConnections(smartGridTopo, impactAnalysisInput);

		// Run only if Not Ignore LogicalConnection ^= I want logical
		// Connections
		// if (!ignoreLogicalConnections) {
		readLogicalConnections(smartGridTopo, impactAnalysisInput);
		// }//TODO Remove if for rollback

		// Generates Result
		ScenarioResult result = genOutputResult();
		result.setScenario(smartGridTopo);

		// Saves to File System
		// FileSystem.saveToFileSystem(result, this.outputPath); //TODO For
		// Debug purposes only can/should? be removed for SimController later

		LOG.debug("Working Directory = " + System.getProperty("user.dir"));

		return result;
	}

	/**
	 * This Method does the Impact Analysis.
	 * 
	 * @param scenario
	 *            the input scenario
	 * @param state
	 *            the input state
	 */
	public void analyze(SmartGridTopology scenario, ScenarioState state) {

		// Generates Result
		ScenarioResult result = run(scenario, state);

		if (result != null) {
			// Saves to File System
			FileSystem.saveToFileSystem(result, this.outputPath);
		}
	}

	/*
	 * Private Methods
	 */

	private int getInternID() {
		int result = internalMaxID;
		internalMaxID++;
		return result;
	}

	private void readStates(SmartGridTopology scenario, ScenarioState state) {
		LOG.debug("[Graph Analyzer]: Start readStates");

		for (EntityState s : state.getEntityStates()) {

			LOG.debug("Class " + s.getOwner().getClass() + " ID " + s.getOwner().getId() + " destroyed? "
					+ s.isIsDestroyed() + " powersource " + s.getOwner().getConnectedTo().toString());

			entityStates.put(s.getOwner().getId(), s);
			// if ((s.getOwner() instanceof ControlCenter) || (s.getOwner()
			// instanceof SmartMeter))
			// {
			logicalNodes.add(s.getOwner().getId());
			// }
			int internalID = getInternID();
			externalToInternalID.put(s.getOwner().getId(), internalID);
			internalToExternalID.put(internalID, s.getOwner().getId());

			if (s.getOwner().getId() > maxID)
				maxID = s.getOwner().getId();
			if (s.getOwner() instanceof ControlCenter) {
				controlCenters.add(s.getOwner().getId());

				LOG.debug("ControlCenter found: " + s.getOwner().getId());
			}
		}
		for (PowerState p : state.getPowerStates()) {
			LOG.debug("Entity " + p.getOwner().getName() + " ID " + p.getOwner().getId() + " powerOutage? "
					+ p.isPowerOutage());
			powerStates.put(p.getOwner().getId(), p);
		}
		LOG.debug("[Graph Analyzer]: End readStates");
	}

	private void readPhysicalConnections(SmartGridTopology scenario, ScenarioState state) {

		LOG.debug("[Graph Analyzer]: Start readPhysicalConnections");

		List<PhysicalConnection> pConns = scenario.getContainsPC();

		for (PhysicalConnection p : pConns) {
			NetworkEntity e1 = p.getLinks().get(0);
			NetworkEntity e2 = p.getLinks().get(1);
			if (externalNodeIsWorking(e1.getId()) && externalNodeIsWorking(e2.getId())) {
				int internal1 = externalToInternalID.get(e1.getId());
				int internal2 = externalToInternalID.get(e2.getId());

				adjacentMatrix[internal1][internal2] = 1;
				adjacentMatrix[internal2][internal1] = 1;
			}
		}

		// Building physical Cluster
		LOG.debug(Matrix.toString(adjacentMatrix));
		LOG.debug("Validate clusteralgorithm");
		physicalClusters = Tarjan.getClusters(adjacentMatrix, internalToExternalID);

		LOG.debug("[Graph Analyzer]: End readPhysicalConnections");
	}

	private void readLogicalConnections(SmartGridTopology scenario, ScenarioState state) {

		LOG.debug("[Graph Analyzer]: Start readLogicalConnections");

		// set logical adjacent
		List<LogicalCommunication> lConns = scenario.getContainsLC();
		for (LogicalCommunication l : lConns) {
			int id1 = l.getLinks().get(0).getId();
			int id2 = l.getLinks().get(1).getId();
			int internal1 = externalToInternalID.get(id1);
			int internal2 = externalToInternalID.get(id2);

			if (areInSameCluster(internal1, internal2, physicalClusters)) {
				logicalAdjacentMatrix[internal1][internal2] = 1;
				logicalAdjacentMatrix[internal2][internal1] = 1;
			}
		}
		// filthy Variant ! Better Solution?
		if (!ignoreLogicalConnections) {
			// find logical paths that work
			logicalClusters = Tarjan.getClusters(logicalAdjacentMatrix, internalToExternalID);
		} else {
			logicalClusters = this.physicalClusters;
		}

		// remove nodes that are not logical
		List<List<Integer>> newClusters = new LinkedList<List<Integer>>();

		// Not every time "logical" Clusters see above
		for (List<Integer> cluster : logicalClusters) {
			List<Integer> newCluster = new LinkedList<Integer>();
			for (Integer i : cluster) {
				if (logicalNodes.contains(internalToExternalID.get(i)))
					newCluster.add(i);
			}
			if (newCluster.size() > 0)
				newClusters.add(newCluster);
		}

		logicalClusters = newClusters;

		for (Integer controlID : controlCenters) {
			int internalControlID = externalToInternalID.get(controlID);
			Double[] connectionAvailable = new Double[internalMaxID + 1];
			for (int i = 0; i < connectionAvailable.length; i++) {
				connectionAvailable[i] = 0.0;
			}
			for (List<Integer> l : logicalClusters) {
				if (l.contains(internalControlID)) {
					for (Integer n : l) {
						connectionAvailable[n] = 1.0;
					}
				}
			}
			controlCenterConnectivity.put(controlID, connectionAvailable);
		}
		LOG.debug("[Graph Analyzer]: End readLogicalConnections");
	}

	private ScenarioResult genOutputResult() {
		SmartgridoutputPackageImpl.init();
		SmartgridoutputFactory factory = SmartgridoutputFactory.eINSTANCE;
		ScenarioResult result = factory.createScenarioResult();
		//

		// TODO Remove if/else for rollback
		if (ignoreLogicalConnections) {

			clusterCleaning(factory, result, physicalClusters);

		} else {
			clusterCleaning(factory, result, logicalClusters);
		}

		// Generate output for every node depending on connection status
		for (int nodeID : logicalNodes) {
			LOG.debug("[Graph Analyzer]: Generate output for node with id " + nodeID);
			smartgridoutput.EntityState state = null;

			int internalNode = externalToInternalID.get(nodeID);

			List<Integer> connectedCCs = new LinkedList<Integer>();
			for (int ccID : controlCenters) {

				if (controlCenterConnectivity.get(ccID)[internalNode] > 0)
					connectedCCs.add(ccID);
			} // End Foreach ControlCenters

			if (externalNodeIsDestroyed(nodeID)) {
				state = factory.createDefect();
			} else if (!externalNodeHasPower(nodeID)) {
				state = factory.createNoPower();
			} else if (connectedCCs.size() == 0) {
				NoUplink n = factory.createNoUplink();
				n.setBelongsToCluster(internalToCluster.get(internalNode));
				internalToCluster.get(internalNode).getHasEntities().add(n);

				// Passthrough IsHacked State from input into Output
				n.setIsHacked(externalNodeIsHacked(nodeID));

				state = n;
			} else {

				Online s = factory.createOnline();
				s.setBelongsToCluster(internalToCluster.get(internalNode));
				internalToCluster.get(internalNode).getHasEntities().add(s);
				// s.setReachableControlCenters(connectedCCs.size()); - Thorsten

				// Passthrough IsHacked State from input into Output
				s.setIsHacked(externalNodeIsHacked(nodeID));

				state = s;
			}

			state.setOwner(entityStates.get(nodeID).getOwner());

			result.getStates().add(state);
		} // End Foreach LogicalNodes

		return result;

	}

	/**
	 * @param factory
	 * @param result
	 */
	private void clusterCleaning(SmartgridoutputFactory factory, ScenarioResult result,
			List<List<Integer>> clusterToClean) {

		for (List<Integer> c : clusterToClean) {
			Cluster cluster = factory.createCluster();
			// cluster.setSmartMeterCount(c.size());
			int smCount = 0;
			int controlCentersInCluster = 0;
			for (Integer i : c) {
				if (i != null) {
					internalToCluster.put(i, cluster);
					// check if its a controlCenter and increase
					// controlCentersInCluster-Count
					int externalID = internalToExternalID.get(i);
					if (externalNodeIsWorking(externalID)) {
						if (controlCenters.contains(externalID)) {
							controlCentersInCluster++;
						} else {
							smCount++;
						}
					}
				}
			}
			cluster.setSmartMeterCount(smCount);
			cluster.setControlCenterCount(controlCentersInCluster);
			if (smCount > 0 || controlCentersInCluster > 0)
				result.getClusters().add(cluster);
		}
	}

	private boolean externalNodeIsWorking(int id) {
		return externalNodeHasPower(id) && !externalNodeIsDestroyed(id);
	}

	private boolean externalNodeHasPower(int id) {
		boolean connected = false;
		try {
			for (PowerGridNode pgn : entityStates.get(id).getOwner().getConnectedTo()) {
				if (!powerStates.get(pgn.getId()).isPowerOutage())
					connected = true;
			}
		} catch (NullPointerException e) {
			LOG.error(
					"Your input model may be not conform to the current topo model but hasn't set its Scenario attribute to a valid value");
		}
		return connected;
	}

	private boolean externalNodeIsDestroyed(int id) {
		return entityStates.get(id).isIsDestroyed();
	}

	private boolean externalNodeIsHacked(int id) {
		return entityStates.get(id).isIsHacked();
	}

	private boolean areInSameCluster(int n, int m, List<List<Integer>> clusterList) {
		boolean result = false;
		boolean notFound = true;
		for (List<Integer> l : clusterList) {
			if (notFound && l.contains(n)) {
				if (l.contains(m))
					result = true;
				notFound = false;
			}
		}
		return result;
	}

	private void clearAll() {
		entityStates.clear();
		powerStates.clear();
		controlCenters.clear();
		internalToCluster.clear();
		externalToInternalID.clear();
		internalToExternalID.clear();
		controlCenterConnectivity.clear();
		logicalNodes.clear();
		internalMaxID = 0;
	}

	@Override
	public String getName() {
		return "Graph Analyzer Impact Analysis";
	}
}
