package smartgrid.model.output.features;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import smartgridoutput.ScenarioResult;
import smartgridsecurity.graphiti.extensionpoint.definition.IDeleteFeatureResolver;
import smartgridtopo.NetworkEntity;

public class OutputDeleteFeatureResolver implements IDeleteFeatureResolver {

    public OutputDeleteFeatureResolver() {
    }

    @Override
    public void deleteBusinessObjects(final List<EObject> boFromDiagram, final EObject owner, final TransactionalEditingDomain domain) {
        ScenarioResult result = null;
        for (final EObject ob : boFromDiagram) {
            if (ob instanceof ScenarioResult) {
                result = (ScenarioResult) ob;
            }
        }
        if (result != null) {
            int position = -1;
            if (owner instanceof NetworkEntity) {
                for (int i = 0; i < result.getStates().size(); i++) {
                    if (result.getStates().get(i).getOwner().getId() == ((NetworkEntity) owner).getId()) {
                        position = i;
                        break;
                    }
                }
                if (position > -1) {
                    removeElement(result, position, domain);
                }
            }
        }
    }

    private void removeElement(final ScenarioResult result, final int position, final TransactionalEditingDomain domain) {
        domain.getCommandStack().execute(new RecordingCommand(domain) {
            @Override
            protected void doExecute() {
                result.getStates().remove(position);
            }
        });
    }

}
