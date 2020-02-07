package smartgrid.simcontrol.test.baselib.coupling;

import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import couplingToICT.initializer.InitializationMapKeys;
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
    @Deprecated
    public void init(ILaunchConfiguration config) throws CoreException;
    
    /**
     * To be used without a launch configuration
     * @param config
     *            behavior for the anaylsis as a Map
     * @return true if Init was successful
     */
    public void init(final Map<InitializationMapKeys, String> initMap);
}
