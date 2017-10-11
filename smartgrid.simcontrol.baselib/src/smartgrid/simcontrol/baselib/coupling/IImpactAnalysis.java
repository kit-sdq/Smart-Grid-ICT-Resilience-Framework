package smartgrid.simcontrol.baselib.coupling;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.simcontrol.baselib.ErrorCodeEnum;
import smartgridinput.ScenarioState;
import smartgridoutput.ScenarioResult;
import smartgridtopo.SmartGridTopology;

public interface IImpactAnalysis extends ISimulationComponent {

    public ScenarioResult run(SmartGridTopology smartGridTopo, ScenarioState impactAnalysisInput);

    /**
     * If using ExtensionPoints and so 0-parameter Constructor pass the config from Simcontrol UI to
     * this Method to build the desired ImpactAnalysis ( "Factory Method")
     *
     * @param config
     *            behavior for the Attacker
     * @return true if Init was successful
     * @throws CoreException
     *             If ILaunchConfiguration.getAttribute fails
     */
    public ErrorCodeEnum init(ILaunchConfiguration config) throws CoreException;
}
