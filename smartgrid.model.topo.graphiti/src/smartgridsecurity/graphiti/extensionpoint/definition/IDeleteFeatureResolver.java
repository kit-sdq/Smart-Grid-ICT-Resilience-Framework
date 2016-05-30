package smartgridsecurity.graphiti.extensionpoint.definition;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

public interface IDeleteFeatureResolver {

    /**
     * this method provides parameters in order to be able to delete business elements from specific
     * models
     *
     * @param boFromDiagram
     *            a list of business objects contained in the current diagram
     * @param owner
     *            the business element in the topo model that should be deleted
     */
    public void deleteBusinessObjects(List<EObject> boFromDiagram, EObject owner, TransactionalEditingDomain domain);
}
