package smartgridsecurity.graphiti.deletefeatures;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.RegistryFactory;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.ui.features.DefaultDeleteFeature;

import smartgridsecurity.graphiti.extensionpoint.definition.IDeleteFeatureResolver;
import smartgridtopo.CommunicatingEntity;
import smartgridtopo.NetworkEntity;

public class SGSDeleteFeature extends DefaultDeleteFeature {

    public SGSDeleteFeature(IFeatureProvider fp) {
        super(fp);
    }

    @Override
    public void delete(IDeleteContext context) {
        // Execute delete feature of extension point to remove business objects from input and
        // output models
        IExtensionRegistry registry = RegistryFactory.getRegistry();
        IConfigurationElement[] elements = registry
                .getConfigurationElementsFor("smartgridsecurity.graphiti.extension.deletefeature");

        for (IConfigurationElement ele : elements) {
            try {
                final Object o = ele.createExecutableExtension("deleteFeature");
                if (o instanceof IDeleteFeatureResolver) {
                    Diagram diagram = (Diagram) context.getPictogramElement().eContainer();
                    ((IDeleteFeatureResolver) o).deleteBusinessObjects(diagram.getLink().getBusinessObjects(), context
                            .getPictogramElement().getLink().getBusinessObjects().get(0), this.getDiagramBehavior()
                            .getEditingDomain());
                }
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }
        // If it's a NetworkEntity, delete all incoming and outgoing connections
        if (context.getPictogramElement().getLink().getBusinessObjects().get(0) instanceof NetworkEntity) {
            NetworkEntity node = (NetworkEntity) context.getPictogramElement().getLink().getBusinessObjects().get(0);
            this.deleteBusinessObjects(node.getLinkedBy().toArray());
            if (context.getPictogramElement().getLink().getBusinessObjects().get(0) instanceof CommunicatingEntity) {
                CommunicatingEntity commEntity = (CommunicatingEntity) context.getPictogramElement().getLink()
                        .getBusinessObjects().get(0);
                this.deleteBusinessObjects(commEntity.getCommunicatesBy().toArray());
            }
        }

        super.delete(context);
    }
}
