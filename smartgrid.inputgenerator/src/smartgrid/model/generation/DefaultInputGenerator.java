package smartgrid.model.generation;

import smartgridinput.EntityState;
import smartgridinput.PowerState;
import smartgridinput.ScenarioState;
import smartgridinput.SmartgridinputFactory;
import smartgridtopo.NetworkEntity;
import smartgridtopo.PowerGridNode;
import smartgridtopo.SmartGridTopology;

public class DefaultInputGenerator {

    public ScenarioState generateInput(SmartGridTopology topo) {
        final ScenarioState inputModel = SmartgridinputFactory.eINSTANCE.createScenarioState();
        inputModel.setScenario(topo);

        for (NetworkEntity entity : topo.getContainsNE()) {
            final EntityState state = SmartgridinputFactory.eINSTANCE.createEntityState();
            state.setOwner(entity);
            inputModel.getEntityStates().add(state);
        }

        for (PowerGridNode node : topo.getContainsPGN()) {
            final PowerState state = SmartgridinputFactory.eINSTANCE.createPowerState();
            state.setOwner(node);
            inputModel.getPowerStates().add(state);
        }

        return inputModel;
    }
}
