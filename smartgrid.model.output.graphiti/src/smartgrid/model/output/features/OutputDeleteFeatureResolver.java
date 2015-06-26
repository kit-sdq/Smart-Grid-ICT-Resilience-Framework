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
    public void deleteBusinessObjects(List<EObject> boFromDiagram, EObject owner, TransactionalEditingDomain domain) {
        ScenarioResult result = null;
        for (EObject ob : boFromDiagram) {
            if (ob instanceof ScenarioResult) {
                result = (ScenarioResult) ob;
            }
        }
        if (result != null) {
            int position = -1;
            if (owner instanceof NetworkEntity) {
                for (int i = 0; i < result.getEntityStates().size(); i++) {
                    if (result.getEntityStates().get(i).getOwner().getId() == ((NetworkEntity) owner).getId()) {
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

    private void removeElement(ScenarioResult result, int position, TransactionalEditingDomain domain) {
        domain.getCommandStack().execute(new RecordingCommand(domain) {
            @Override
            protected void doExecute() {
                result.getEntityStates().remove(position);
            }
        });
    }

}
