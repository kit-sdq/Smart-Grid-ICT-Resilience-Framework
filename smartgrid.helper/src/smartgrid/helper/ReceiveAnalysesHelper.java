package smartgrid.helper;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.RegistryFactory;

import smartgrid.simcontrol.baselib.Constants;
import smartgrid.simcontrol.interfaces.IAttackerSimulation;
import smartgrid.simcontrol.interfaces.IImpactAnalysis;
import smartgrid.simcontrol.interfaces.IPowerLoadSimulation;
import smartgrid.simcontrol.interfaces.ITerminationCondition;
import smartgrid.simcontrol.interfaces.ITimeProgressor;

public class ReceiveAnalysesHelper {

    private IExtensionRegistry registry = RegistryFactory.getRegistry();

    public ReceiveAnalysesHelper() {
    }

    public List<IPowerLoadSimulation> getPowerLoadElements() throws CoreException {
        List<IPowerLoadSimulation> list = new ArrayList<IPowerLoadSimulation>();
        IConfigurationElement[] elements = registry
                .getConfigurationElementsFor(Constants.POWER_LOAD_SIMULATION_EXTENSION_POINT);
        for (IConfigurationElement element : elements) {
            Object o = element.createExecutableExtension("powerLoadSimulation");
            if (o instanceof IPowerLoadSimulation) {
                list.add((IPowerLoadSimulation) o);
            }
        }
        return list;
    }

    public List<IAttackerSimulation> getAttackerSimulationElements() throws CoreException {
        List<IAttackerSimulation> list = new ArrayList<IAttackerSimulation>();
        IConfigurationElement[] elements = registry
                .getConfigurationElementsFor(Constants.ATTACKER_SIMULATION_EXTENSION_POINT);
        for (IConfigurationElement element : elements) {
            Object o = element.createExecutableExtension("class");
            if (o instanceof IAttackerSimulation) {
                list.add((IAttackerSimulation) o);
            }
        }
        return list;
    }

    /**
     * Not implemented yet
     * 
     * @param registry
     * @return
     */
    public List<String> getKritisSimulationElements() {
        // TODO: Implement this when ready
        return null;
    }

    public List<ITerminationCondition> getTerminationConditionElements() throws CoreException {
        List<ITerminationCondition> list = new ArrayList<ITerminationCondition>();
        IConfigurationElement[] elements = registry
                .getConfigurationElementsFor(Constants.TERMINATION_CONDITION_EXTENSION_POINT);
        for (IConfigurationElement element : elements) {
            Object o = element.createExecutableExtension("class");
            if (o instanceof ITerminationCondition) {
                list.add((ITerminationCondition) o);
            }
        }
        return list;
    }

    public List<ITimeProgressor> getProgressorElements() throws CoreException {
        List<ITimeProgressor> list = new ArrayList<ITimeProgressor>();
        IConfigurationElement[] elements = registry
                .getConfigurationElementsFor(Constants.TIME_PROGRESSOR_EXTENSION_POINT);
        for (IConfigurationElement element : elements) {
            Object o = element.createExecutableExtension("class");
            if (o instanceof ITimeProgressor) {
                list.add((ITimeProgressor) o);
            }
        }
        return list;
    }

    public List<IImpactAnalysis> getImpactAnalysisElements() throws CoreException {
        List<IImpactAnalysis> list = new ArrayList<IImpactAnalysis>();
        IConfigurationElement[] elements = registry
                .getConfigurationElementsFor(Constants.IMPACT_ANALYSIS_EXTENSION_POINT);
        for (IConfigurationElement element : elements) {
            Object o = element.createExecutableExtension("class");
            if (o instanceof IImpactAnalysis) {
                list.add((IImpactAnalysis) o);
            }
        }
        return list;
    }
}
