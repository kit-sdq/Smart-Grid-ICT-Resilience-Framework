package smartgrid.helper;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.RegistryFactory;

import smartgrid.simcontrol.baselib.Constants;
import smartgrid.simcontrol.baselib.coupling.IAttackerSimulation;
import smartgrid.simcontrol.baselib.coupling.IImpactAnalysis;
import smartgrid.simcontrol.baselib.coupling.IKritisSimulationWrapper;
import smartgrid.simcontrol.baselib.coupling.IPowerLoadSimulationWrapper;
import smartgrid.simcontrol.baselib.coupling.ITerminationCondition;
import smartgrid.simcontrol.baselib.coupling.ITimeProgressor;

public class SimulationExtensionPointHelper {

    private final IExtensionRegistry registry = RegistryFactory.getRegistry();

    public SimulationExtensionPointHelper() {
    }

    public List<IPowerLoadSimulationWrapper> getPowerLoadSimulationExtensions() throws CoreException {
        final List<IPowerLoadSimulationWrapper> list = new ArrayList<IPowerLoadSimulationWrapper>();
        final IConfigurationElement[] elements = this.registry
                .getConfigurationElementsFor(Constants.POWER_LOAD_SIMULATION_EXTENSION_POINT);
        for (final IConfigurationElement element : elements) {
            final Object o = element.createExecutableExtension("powerLoadSimulation");
            if (o instanceof IPowerLoadSimulationWrapper) {
                list.add((IPowerLoadSimulationWrapper) o);
            }
        }
        return list;
    }

    public List<IAttackerSimulation> getAttackerSimulationExtensions() throws CoreException {
        final List<IAttackerSimulation> list = new ArrayList<IAttackerSimulation>();
        final IConfigurationElement[] elements = this.registry
                .getConfigurationElementsFor(Constants.ATTACKER_SIMULATION_EXTENSION_POINT);
        for (final IConfigurationElement element : elements) {
            final Object o = element.createExecutableExtension("class");
            if (o instanceof IAttackerSimulation) {
                list.add((IAttackerSimulation) o);
            }
        }
        return list;
    }

    public List<ITerminationCondition> getTerminationConditionExtensions() throws CoreException {
        final List<ITerminationCondition> list = new ArrayList<ITerminationCondition>();
        final IConfigurationElement[] elements = this.registry
                .getConfigurationElementsFor(Constants.TERMINATION_CONDITION_EXTENSION_POINT);
        for (final IConfigurationElement element : elements) {
            final Object o = element.createExecutableExtension("class");
            if (o instanceof ITerminationCondition) {
                list.add((ITerminationCondition) o);
            }
        }
        return list;
    }

    public List<ITimeProgressor> getProgressorExtensions() throws CoreException {
        final List<ITimeProgressor> list = new ArrayList<ITimeProgressor>();
        final IConfigurationElement[] elements = this.registry
                .getConfigurationElementsFor(Constants.TIME_PROGRESSOR_EXTENSION_POINT);
        for (final IConfigurationElement element : elements) {
            final Object o = element.createExecutableExtension("class");
            if (o instanceof ITimeProgressor) {
                list.add((ITimeProgressor) o);
            }
        }
        return list;
    }

    public List<IImpactAnalysis> getImpactAnalysisExtensions() throws CoreException {
        final List<IImpactAnalysis> list = new ArrayList<IImpactAnalysis>();
        final IConfigurationElement[] elements = this.registry
                .getConfigurationElementsFor(Constants.IMPACT_ANALYSIS_EXTENSION_POINT);
        for (final IConfigurationElement element : elements) {
            final Object o = element.createExecutableExtension("class");
            if (o instanceof IImpactAnalysis) {
                list.add((IImpactAnalysis) o);
            }
        }
        return list;
    }

    public List<IKritisSimulationWrapper> getKritisSimulationExtensions() throws CoreException {
        final List<IKritisSimulationWrapper> list = new ArrayList<IKritisSimulationWrapper>();
        final IConfigurationElement[] elements = this.registry
                .getConfigurationElementsFor(Constants.KRITIS_SIMULATION_EXTENSION_POINT);
        for (final IConfigurationElement element : elements) {
            final Object o = element.createExecutableExtension("class");
            if (o instanceof IKritisSimulationWrapper) {
                list.add((IKritisSimulationWrapper) o);
            }
        }
        return list;
    }
}
