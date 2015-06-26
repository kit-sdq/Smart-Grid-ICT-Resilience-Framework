package smartgrid.impactanalysis;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import smartgrid.simcontrol.interfaces.ErrorCodeEnum;
import smartgrid.simcontrol.interfaces.IImpactAnalysis;
import smartgrid.simcontroller.baselibary.Constants;
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
import smartgridtopo.Scenario;

/**
 * This Class does an Impact Analysis.
 * 
 * The Target of this Analysis is to get to know which SmartMeters can communicate with which
 * ControlCenters respectively other SmartMeters For more Details see the Model Description in the @see
 * smartgrid.model.* packages
 * 
 * @author Torsten
 * @author Christian (modified)
 * @implements IImpactAnalysis
 */
public class GraphAnalyzer implements IImpactAnalysis {

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
            // checks whether ignoreLogicalConnectionsString is true and assigns it
            this.ignoreLogicalConnections = (Constants.TRUE).equals(ignoreLogicalConnectionsString);
        }

        this.initDone = true;

        return myError;
    }

    /**
     * The Constructor of the Class. For use with the old Impact Analysy only Version
     * {@link smartgrid.impactanalysis.ui}
     * 
     * @deprecated It's now integrated in the SimControl Approach
     * @param outputPath
     *            In case of using {@code public void analyze(...)} Method it is the Path to the
     *            Outputfile
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
     * {@inheritDoc}
     * <p>
     * 
     * 
     * 
     */
    @Override
    public ScenarioResult run(Scenario smartGridTopo, ScenarioState impactAnalysisInput) {

        assert (initDone) : "Init wasn't run! Run init() first !";

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

        // System.out.println("Working Directory = " + System.getProperty("user.dir"));

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
    public void analyze(Scenario scenario, ScenarioState state) {

        // Generates Result
        ScenarioResult result = run(scenario, state);

        if (result != null) {
            // Saves to File System
            FileSystem.saveToFileSystem(result, this.outputPath);

            // System.out.println("Working Directory = " + System.getProperty("user.dir"));
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

    private void readStates(Scenario scenario, ScenarioState state) {

        for (EntityState s : state.getEntityStates()) {
            // System.out.println("Klasse " + s.getOwner().getClass() + " ID " +
            // s.getOwner().getId() + " destroyed? "
            // + s.isIsDestroyed() + " powersource " + s.getOwner().getConnectedTo().toString());
            entityStates.put(s.getOwner().getId(), s);
            // if ((s.getOwner() instanceof ControlCenter) || (s.getOwner() instanceof SmartMeter))
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
                // System.out.println("ControlCenter found: " + s.getOwner().getId());
            }
        }
        for (PowerState p : state.getPowerStates()) {
            // System.out.println("Entity " + p.getOwner().getName() + " ID " + p.getOwner().getId()
            // + " powerOutage? "
            // + p.isPowerOutage());
            powerStates.put(p.getOwner().getId(), p);
        }
    }

    private void readPhysicalConnections(Scenario scenario, ScenarioState state) {

        List<PhysicalConnection> pConns = scenario.getContainsC();

        for (PhysicalConnection p : pConns) {
            // System.out.println("PhysicalConnection " + p.getName());
            for (NetworkEntity e : p.getLinks()) {
                // System.out.println(e.getClass() + " " + e.getName() + " ID " + e.getId());
            }
            NetworkEntity e1 = p.getLinks().get(0);
            NetworkEntity e2 = p.getLinks().get(1);
            if (externalNodeIsWorking(e1.getId()) && externalNodeIsWorking(e2.getId())) {
                int internal1 = externalToInternalID.get(e1.getId());
                int internal2 = externalToInternalID.get(e2.getId());
                // adjacentMatrix[e1.getId()][e2.getId()] = 1;
                // adjacentMatrix[e2.getId()][e1.getId()] = 1;
                adjacentMatrix[internal1][internal2] = 1;
                adjacentMatrix[internal2][internal1] = 1;
            }
        }

        // Building physical Cluster
        // System.out.println(Matrix.toString(adjacentMatrix));
        // System.out.println("Validate clusteralgorithm");
        physicalClusters = Tarjan.getClusters(adjacentMatrix, internalToExternalID);

    }

    private void readLogicalConnections(Scenario scenario, ScenarioState state) {

        for (List<Integer> l : physicalClusters) {
            // System.out.println("Cluster:");
            for (Integer i : l) {
                // System.out.print(internalToExternalID.get(i) + " ");

            }
            // System.out.println();
        }

        // set logical adjacent
        List<LogicalCommunication> lConns = scenario.getContainsLC();
        for (LogicalCommunication l : lConns) {
            int id1 = l.getLinks().get(0).getId();
            int id2 = l.getLinks().get(1).getId();
            int internal1 = externalToInternalID.get(id1);
            int internal2 = externalToInternalID.get(id2);
            /*
             * if (!logicalNodes.contains(id1)) logicalNodes.add(id1); if
             * (!logicalNodes.contains(id2)) logicalNodes.add(id2);
             */
            // System.out.println("Logical between " + id1 + " " + id2);
            /*
             * for (double[][] m : matrices) { if (m[internal1][internal2] > 0) {
             * logicalAdjacentMatrix[internal1][internal2] = 1;
             * logicalAdjacentMatrix[internal2][internal1] = 1; } }
             */
            if (areInSameCluster(internal1, internal2, physicalClusters)) {
                logicalAdjacentMatrix[internal1][internal2] = 1;
                logicalAdjacentMatrix[internal2][internal1] = 1;
            }
        }
        // System.out.println("Logical Adjacent Matrix:");
        // System.out.println(Matrix.toString(logicalAdjacentMatrix));

        // filthy Variant ! Better Solution?
        if (!ignoreLogicalConnections) {
            // find logical paths that work
            logicalClusters = Tarjan.getClusters(logicalAdjacentMatrix, internalToExternalID);
        } else {
            logicalClusters = this.physicalClusters;
        }

        // remove nodes that are not logical
        List<List<Integer>> newClusters = new LinkedList<List<Integer>>();

        for (List<Integer> cluster : logicalClusters) { // Not every time "logical" Clusters see
                                                        // above
            List<Integer> newCluster = new LinkedList<Integer>();
            for (Integer i : cluster) {
                if (logicalNodes.contains(internalToExternalID.get(i)))
                    newCluster.add(i);
            }
            if (newCluster.size() > 0)
                newClusters.add(newCluster);
        }

        logicalClusters = newClusters;
        // Just println
        for (List<Integer> l : logicalClusters) {
            // System.out.println("Logische Cluster:");
            for (Integer i : l) {
                // System.out.print(" " + internalToExternalID.get(i));

            }
            // System.out.println();
        }

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
        /*
         * List<double[][]> logicalMatrices = new LinkedList<double[][]>(); zwischen =
         * logicalAdjacentMatrix.clone(); logicalMatrices.add(zwischen.clone());
         * 
         * for (int i = 1; i <= internalMaxID + 1; i ++) { zwischen =
         * Matrix.multiplyByMatrix(zwischen, logicalAdjacentMatrix);
         * logicalMatrices.add(zwischen.clone()); ////System.out.println("i = " + i);
         * ////System.out.println(Matrix.toString(zwischen)); } for (Integer controlID :
         * controlCenters) { int internalControlID = externalToInternalID.get(controlID); Double[]
         * connectionAvailable = new Double[internalMaxID + 1]; for (int i = 0; i <
         * connectionAvailable.length; i++) { connectionAvailable[i] = 0.0; } for (double[][] m :
         * logicalMatrices) { for (int node = 0; node < internalMaxID + 1; node++) { if
         * (m[internalControlID][node] > 0) connectionAvailable[node] = 1.0; } }
         * 
         * //System.out.println("ControlCenter " + controlID + " can connect to " ); for (int i = 0;
         * i < connectionAvailable.length; i++) { if (connectionAvailable[i] > 0 && i !=
         * internalControlID) //System.out.println(internalToExternalID.get(i)); }
         * controlCenterConnectivity.put(controlID, connectionAvailable); }
         */
        // System.out.println("Anzahl CC " + controlCenters.size());

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

        for (int nodeID : logicalNodes) { // externalToInternalID.keySet()
            // F�rr jeden Knoten wird jetzt der Output erzeugt
            // Je nach Verbindungsstatus wird der SmartMeter entsprechend
            // instanziiert
            smartgridoutput.EntityState state = null;
            // System.out.println("Betrachte output von " + nodeID);
            int internalNode = externalToInternalID.get(nodeID);

            List<Integer> connectedCCs = new LinkedList<Integer>();
            for (int ccID : controlCenters) {

                if (controlCenterConnectivity.get(ccID)[internalNode] > 0)
                    connectedCCs.add(ccID);
            }// End Foreach ControlCenters

            if (externalNodeIsDestroyed(nodeID)) {
                state = factory.createDestroyed();
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

            result.getEntityStates().add(state);
        }// End Foreach LogicalNodes

        return result;

    }

    /**
     * @param factory
     * @param result
     */
    private void clusterCleaning(SmartgridoutputFactory factory, ScenarioResult result,
            List<List<Integer>> clusterToClean) {
        // Wir haben nun die Ergebnisse des Clusters f�r logische Verbindungen.
        // Hier gibt es allerdings noch Knoten, die f�r uns nicht weiter
        // interessant sind.
        // Deshalb wird jeder Cluster durchsucht und alle Elemente entfernt, die
        // kein ControlCenter oder SmartMeter sind.
        // Cluster die danach keine Elemente mehr haben werden gelöscht
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

    // Code Removed

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
            System.out
                    .println("Your input model may be not conform to the current topo model but hasn't set its Scenario attribute to a valid value");
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
