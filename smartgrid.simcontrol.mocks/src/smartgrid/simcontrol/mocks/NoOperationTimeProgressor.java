package smartgrid.simcontrol.mocks;

import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.simcontrol.baselib.coupling.ITimeProgressor;

public class NoOperationTimeProgressor implements ITimeProgressor {

    /**
     * {@inheritDoc}
     * <p>
     * This mock does nothing at all
     */
    @Override
    public void progress() {
    }

    @Override
    public void init(final ILaunchConfiguration config) {
        // Nothing to do here..
    }

    @Override
    public String getName() {
        return "No Operation";
    }
}
