package smartgrid.simcontrol.test.baselib.coupling;

import java.util.Map;

import couplingToICT.initializer.InitializationMapKeys;

public interface ITimeProgressor extends ISimulationComponent {

    public void progress();

    /**
     * @param config
     *            behavior for the timeprog. as a map
     * @return true if Init was successful
     */
    public void init(final Map<InitializationMapKeys, String> initMap);
}
