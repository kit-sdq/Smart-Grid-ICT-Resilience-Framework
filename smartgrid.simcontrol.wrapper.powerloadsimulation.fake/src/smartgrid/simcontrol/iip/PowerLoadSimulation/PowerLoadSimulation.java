package smartgrid.simcontrol.iip.PowerLoadSimulation;

import java.util.List;
import java.util.Map;

import smartgrid.simcontrol.coupling.IPowerLoadSimulation;
import smartgrid.simcontrol.coupling.ISmartMeterState;
import smartgrid.simcontrol.coupling.PowerSpec;

/**
 * The sole purpose of this class and this project is to create a small jar that can be used in the
 * PowerLoadSimulation wrapper. So, the project still compiles. We don't want to commit new jars to
 * SVN every time, the real jar changes.
 * 
 * @author Misha
 */
public class PowerLoadSimulation implements IPowerLoadSimulation {

    @Override
    public Map<String, Map<String, Double>> run(Map<String, Map<String, PowerSpec>> kritisDemands, Map<String, Map<String, ISmartMeterState>> smartMeterStates) {
        throw new RuntimeException("You used the PowerLoadSimulation wrapper with the dummy jar. This will never work. Replace the jar with the propper one (from the IIP).");
    }

    @Override
    public void initializeNodeIDs(List<String> _nodeIDs) {
        throw new RuntimeException("You used the PowerLoadSimulation wrapper with the dummy jar. This will never work. Replace the jar with the propper one (from the IIP).");
    }
}
