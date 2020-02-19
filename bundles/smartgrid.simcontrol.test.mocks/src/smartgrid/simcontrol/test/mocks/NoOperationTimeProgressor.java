package smartgrid.simcontrol.test.mocks;

import java.util.Map;


import couplingToICT.initializer.InitializationMapKeys;
import smartgrid.simcontrol.test.baselib.coupling.ITimeProgressor;

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
    public void init(final Map<InitializationMapKeys, String> initMap) {
        // Nothing to do here..
    }

    @Override
    public String getName() {
        return "No Operation";
    }
}
