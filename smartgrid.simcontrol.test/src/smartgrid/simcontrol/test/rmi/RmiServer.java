package smartgrid.simcontrol.test.rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;


import smartgrid.simcontrol.test.rmi.BlockingKritisDataExchanger;
import smartgrid.simcontrol.test.ReactiveSimulationController;
import couplingToICT.SimcontrolInitializationException;
import couplingToICT.ISimulationController;
import couplingToICT.PowerAssigned;
import couplingToICT.PowerDemand;
import couplingToICT.PowerInfeed;
import couplingToICT.SmartGridTopoContainer;
import couplingToICT.SimcontrolException;

/**
 * This class acts as RMI Server for the KRITIS simulation of the IKET. The server is always
 * registered when Eclipse starts by the early {@link Startup} hook of the plugin. It also
 * implements the RMI Interface of the simulation coupling. The server delegates to the simulation
 * coupling either in active or reactive mode.
 * 
 * In <b>active mode</b>, the {@link SimulationController} has to be started via the
 * {@link SimcontroLaunchConfigurationDelegate}. The simulation coupling and the KRITIS simulation
 * will synchronize and exchange data using the {@link BlockingKritisDataExchanger}.
 * 
 * In <b>reactive mode</b>, the {@link ReactiveSimulationController} will be used. All control flow
 * will originate from the KRITIS simulation, thus no synchronization is required. However, all
 * configuration must also be passed in via the KRITIS simulation.
 * 
 * @author Mazen
 */
public class RmiServer implements ISimulationController {

    private static final Logger LOG = Logger.getLogger(RmiServer.class);

    private static final String ERROR_SERVER_NOT_INITIALIZED = "The SimControl RMI server is not initialized.";
    private static final String ERROR_SERVER_ALREADY_INITIALIZED = "The SimControl RMI server is already initialized.";

    private static RmiServer instance;

    private ReactiveSimulationController reactiveSimControl;

    /**
     * The registry is used to bind and unbind the server (on start and shutdown).
     */
    private Registry registry;

    /**
     * This state of the server enforces a meaningful call sequence protocol.
     */
    private RmiServerState state = RmiServerState.NOT_INIT;

    private enum RmiServerState {
        NOT_INIT, ACTIVE, REACTIVE;
    }

    public static synchronized void ensureRunning() {
        if (instance == null) {
            instance = new RmiServer();
            instance.startServer();
        }
    }

    public static synchronized void resetState() {
        if (instance != null) {
            instance.state = RmiServerState.NOT_INIT;
            LOG.info("The state of the RMI Server was reset to \"not initiated\".");
        }
    }

    public static synchronized void shutDown() {
        if (instance != null) {
            instance.shutDownServer();
            instance = null;
        }
    }

    /**
     * Binds the server to port 1099 ({@link java.rmi.registry.Registry.REGISTRY_PORT}).
     */
    private void startServer() {
        try {
            ISimulationController stub = (ISimulationController) UnicastRemoteObject.exportObject(this, 0);
            registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            registry.bind("ISimulationController", stub);
            LOG.info("RMI server running");
        } catch (Exception e) {
            LOG.error("Could not start RMI server", e);
        }
    }

    /**
     * Properly unbinds the server from the port.
     */
    private void shutDownServer() {
        if (registry != null) {
            try {
                registry.unbind("ISimulationController");
                LOG.info("SimControl RMI server shutdown successful");
            } catch (RemoteException e) {
                LOG.error("SimControl RMI server shutdown failed", e);
            } catch (NotBoundException e) {
                LOG.warn("SimControl RMI server shutdown failed: port was unbound. This is unexpected but harmless.",
                        e);
            }
            registry = null;
        }
    }

    @Override
    public void terminate() throws SimcontrolException {
        LOG.info("shutDown was called remotely");
        if (state == RmiServerState.NOT_INIT) {
            LOG.warn(ERROR_SERVER_NOT_INITIALIZED);
            throw new SimcontrolException(ERROR_SERVER_NOT_INITIALIZED);
        }
        // shut down if reactive
        else if (state == RmiServerState.REACTIVE) {
            reactiveSimControl.shutDown();
        }
        // To-do here, the active simcontrol could be shutDown if there was a pointer (rendezvous
        // required)

        state = RmiServerState.NOT_INIT;
    }

    @Override
    public void initActive() {
        LOG.info("init active called remotely");
        if (state != RmiServerState.NOT_INIT) {
            LOG.warn(ERROR_SERVER_ALREADY_INITIALIZED);
        }
        state = RmiServerState.ACTIVE; 
        
     // TODO remove when active mode works
        try {
			temp_initReactive();
		} catch (SimcontrolInitializationException e) {
			e.printStackTrace();
		} 
        
    }
    
    private void temp_initReactive() throws SimcontrolInitializationException {
    	LOG.info("temp init reactive called remotely");
    	state = RmiServerState.REACTIVE;
    	reactiveSimControl = new ReactiveSimulationController();
    	String outputPath = "/Users/mazenebada/Hiwi/SmartgridWorkspace/smartgrid.model.examples/";
    	reactiveSimControl.init(outputPath);
    	
    	try {
            // To-do initiator (KRITIS Sim) should be able to choose analyses
            // To-do initiator should send init data
            reactiveSimControl.loadDefaultAnalyses();
        } catch (CoreException e) {
            throw new SimcontrolInitializationException("Simcontrol failed to initialize all simulation components.",
                    e);
        }

    	LOG.info("temp init successfuly done");
    }

    @Override
    public void initReactive(String outputPath, String topoPath, String inputStatePath) throws SimcontrolException {
        LOG.info("init reactive called remotely");
        if (state != RmiServerState.NOT_INIT) {
            LOG.warn(ERROR_SERVER_ALREADY_INITIALIZED);
        }
        state = RmiServerState.REACTIVE;

        reactiveSimControl = new ReactiveSimulationController();
        reactiveSimControl.init(outputPath);
        reactiveSimControl.initModelsFromFiles(topoPath, inputStatePath); // To-do conditional auto
                                                                          // generation
        try {
            // To-do initiator (KRITIS Sim) should be able to choose analyses
            // To-do initiator should send init data
            reactiveSimControl.loadDefaultAnalyses();
        } catch (CoreException e) {
            throw new SimcontrolInitializationException("Simcontrol failed to initialize all simulation components.",
                    e);
        }
    }

    @Override
    public void initTopo(SmartGridTopoContainer topo) throws SimcontrolException {

        
        if (topo == null) {
        	LOG.info("Topo Container is null");
        } else {
	        if (state == RmiServerState.ACTIVE) {
	        	LOG.info("init topo called remotely (Active)");
	        	BlockingKritisDataExchanger.storeGeoData(topo);
	        } else if (state == RmiServerState.REACTIVE) {
	        	LOG.info("init topo called remotely (ReActive)");
	            reactiveSimControl.initTopo(topo);
	        } else {
	            LOG.warn(ERROR_SERVER_NOT_INITIALIZED);
	            throw new SimcontrolException(ERROR_SERVER_NOT_INITIALIZED);
	        }
        }

    }


    @Override
    public PowerInfeed getModifiedInfeedPowerSpec(PowerInfeed CIPowerInfeed) throws RemoteException, SimcontrolException, InterruptedException {
        LOG.info("getModifiedInfeedPowerSpec (not defined yet) was called remotely");
        return null;
    }

    @Override
    public PowerDemand getModifiedPowerSpec(PowerDemand CIPowerDemand, PowerAssigned power)
            throws RemoteException, couplingToICT.SimcontrolException, InterruptedException {
                
        LOG.info("run was called remotely");
        
        PowerDemand powerDemand = null;

        if (state == RmiServerState.ACTIVE) {
            try {
            	//buffer pA und pD
                BlockingKritisDataExchanger.bufferPDAndPA(power, CIPowerDemand);
                
                //get Modified Power Demand
                powerDemand = BlockingKritisDataExchanger.getModifiedPowerDemand();
            } catch (InterruptedException e) {
                throw e;
            } catch (Throwable e) {
                resetState();
                BlockingKritisDataExchanger.freeAll();
                LOG.info(
                        "The stored exception that originally occured in SimControl was passed to the remote KRITIS simulation. The RMI server and data exchange are now reset.");
                throw new SimcontrolException(
                        "There was an exception in SimControl. The RMI server has now been reset to 'uninitialized'.",
                        e);
            }
        } else if (state == RmiServerState.REACTIVE) {
        	//run
            reactiveSimControl.run(power);
            
            // Modify demand
            powerDemand = reactiveSimControl.modifyPowerDemand(CIPowerDemand);
        } else {
            LOG.warn(ERROR_SERVER_NOT_INITIALIZED);
            throw new SimcontrolException(ERROR_SERVER_NOT_INITIALIZED);
        }
    
        return powerDemand;
    }

    @Override
    public couplingToICT.SmartComponentStateContainer getDysfunctSmartComponents()
            throws RemoteException, couplingToICT.SimcontrolException, InterruptedException {
        LOG.info("Dysfunctional smart components will be returned");

        if (state == RmiServerState.ACTIVE) {
        	try {
				return BlockingKritisDataExchanger.getSCSC();
			} catch (Throwable e) {
				throw new SimcontrolException(
                        "There was an exception in SimControl. The dysfunctionalSmartComponents are not defined.",
                        e);
			}
        } else if (state == RmiServerState.REACTIVE) {
            return reactiveSimControl.getDysfunctionalcomponents();
        } else {
            LOG.warn(ERROR_SERVER_NOT_INITIALIZED);
            throw new SimcontrolException(ERROR_SERVER_NOT_INITIALIZED);
        }
        
    }

}