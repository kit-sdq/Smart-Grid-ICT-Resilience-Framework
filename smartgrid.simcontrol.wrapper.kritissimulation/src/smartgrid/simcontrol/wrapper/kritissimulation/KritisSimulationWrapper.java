package smartgrid.simcontrol.wrapper.kritissimulation;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import simcity.libagent.BaseAgent;
import simcity.power.Electrified;
import simcity.xlauncher.SimConnector;
import smartgrid.simcontrol.baselib.ErrorCodeEnum;
import smartgrid.simcontrol.baselib.coupling.IKritisSimulationWrapper;
import smartgrid.simcontrol.baselib.coupling.PowerSpec;

public class KritisSimulationWrapper implements IKritisSimulationWrapper {

    private static final Logger LOG = Logger.getLogger(KritisSimulationWrapper.class);

    private SimConnector kritisSimConnector;

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, PowerSpec> run(Map<String, Double> power) {

        // get all agents with power supply
        Iterable<Electrified> agents = kritisSimConnector.getAgents(Electrified.class);

        // set power supply
        for (Electrified ag : agents) {
            String agentId = ((BaseAgent) ag).getAgentId();
            double powerSupply = power.get(agentId);
            ag.setPowerInputWatt((int) powerSupply); //TODO: remove cast

            LOG.debug("Set power input " + powerSupply + " for agent " + agentId);
        }

        LOG.debug("Performing KRITIS simulation step");
        kritisSimConnector.step();

        // get all agents with power supply
        agents = kritisSimConnector.getAgents(Electrified.class);

        // get power demand
        Map<String, PowerSpec> powerDemand = new HashMap<String, PowerSpec>();
        for (Electrified ag : agents) {
            double powerRequired = (double) ag.getPowerRequired(); //TODO: remove cast
            PowerSpec powerSpec = new PowerSpec(powerRequired, powerRequired, 0); //TODO: pass proper criticality value

            String agentId = ((BaseAgent) ag).getAgentId();

            powerDemand.put(agentId, powerSpec);
            LOG.debug("Got demand " + powerRequired + " from agent " + agentId);
        }

        return powerDemand;
    }

    @Override
    public Map<String, PowerSpec> getDefaultDemand() {

        // get all agents with power supply
        @SuppressWarnings("unchecked")
        Iterable<Electrified> agents = kritisSimConnector.getAgents(Electrified.class);

        //get default demand
        Map<String, PowerSpec> powerDemand = new HashMap<String, PowerSpec>();
        for (Electrified ag : agents) {
            double powerRequired = (double) ag.getPowerRequired(); // TODO: use proper methods, remove cast
            PowerSpec powerSpec = new PowerSpec(powerRequired, powerRequired, 0); //TODO: pass proper criticality value

            String agentId = ((BaseAgent) ag).getAgentId();

            powerDemand.put(agentId, powerSpec);
            LOG.debug("Got default demand " + powerRequired + " from agent " + agentId);
        }
        return powerDemand;
    }

    @Override
    public ErrorCodeEnum init(final ILaunchConfiguration config) throws CoreException {

        // initialize KRITIS-model
        kritisSimConnector = new SimConnector("/Shared_workspace/gekoppelte_simulation/IKET/TestSimulation/mysim_v1/SimCity.rs");

        kritisSimConnector.initSimulation("/Shared_workspace/gekoppelte_simulation/IKET/TestSimulation/mysim_v1/agents/houseHoldAgentArray.json",
                "/Shared_workspace/gekoppelte_simulation/IKET/TestSimulation/mysim_v1/agents/pharmacyAgentArray.json");

        return ErrorCodeEnum.SUCCESS;
    }

    @Override
    public String getName() {
        return "KRITIS Simulation Wrapper";
    }
}
