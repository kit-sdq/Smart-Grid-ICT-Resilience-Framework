package smartgrid.simcontrol.baselib.coupling;

import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.simcontrol.baselib.ErrorCodeEnum;
import smartgrid.simcontrol.coupling.ISmartMeterState;
import smartgrid.simcontrol.coupling.PowerSpec;

public interface IPowerLoadSimulationWrapper extends ISimulationComponent {

    /**
     * If using ExtensionPoints and so 0-parameter Constructor pass the config from Simcontrol UI to
     * this Method to build the desired AttackerSimulation ("Factory Method")
     *
     * @param config
     *            behavior for the Attacker
     * @return true if Init was successful
     * @throws CoreException
     *             If ILaunchConfiguration.getAttribute fails
     */
    public ErrorCodeEnum init(ILaunchConfiguration config) throws CoreException;

    void initData(String gridFileContent, List<String> nodeIDs);

    /**
     * @param kritisDemands
     *            Map von Node ID (String) auf Map von CI ID (String) auf PowerSpecs
     * @param smartMeterStates
     *            Map von Node ID auf Map von CI ID auf SmartMeterState
     * @return Map von Node ID (String) auf Map von CI ID (String) auf Leistung (MW)
     */
    Map<String, Map<String, Double>> run(Map<String, Map<String, PowerSpec>> kritisDemands, Map<String, Map<String, ISmartMeterState>> smartMeterStates);
}
