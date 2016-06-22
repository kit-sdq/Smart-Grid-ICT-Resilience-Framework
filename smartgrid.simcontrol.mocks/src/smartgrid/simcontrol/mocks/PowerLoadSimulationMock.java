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
import smartgrid.simcontrol.baselib.coupling.PowerSpec;
import smartgridoutput.EntityState;

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
    public Map<String, Double> run(Map<String, PowerSpec> kritisDemands, Map<String, EntityState> smartMeterStates) {
        // TODO Auto-generated method stub
        return new HashMap<String, Double>();
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
