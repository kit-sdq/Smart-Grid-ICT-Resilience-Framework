/**
 *
 */
package smartgrid.simcontrol.mocks;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.simcontrol.baselib.ErrorCodeEnum;
import smartgrid.simcontrol.baselib.coupling.IPowerLoadSimulationWrapper;
import smartgrid.simcontrol.coupling.ISmartMeterState;
import smartgrid.simcontrol.coupling.PowerSpec;

/**
 * @author Christian
 *
 */
public class PowerLoadSimulationMock implements IPowerLoadSimulationWrapper {

    /**
     * {@inheritDoc}
     * <P>
     * Runs a mocked Power Load Simulation. This Mock does not change anything.
     */
    @Override
    public Map<String, Map<String, Double>> run(Map<String, Map<String, PowerSpec>> kritisDemands, Map<String, Map<String, ISmartMeterState>> smartMeterStates) {
        return new HashMap<String, Map<String,Double>>();
    }

    @Override
    public ErrorCodeEnum init(final ILaunchConfiguration config) throws CoreException {
        return ErrorCodeEnum.SUCCESS;
    }

    @Override
    public String getName() {
        return "Mock";
    }
}
