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

    public SGSDeleteFeature(final IFeatureProvider fp) {
        super(fp);
    }

    @Override
    public void delete(final IDeleteContext context) {
        // Execute delete feature of extension point to remove business objects from input and
        // output models
        final IExtensionRegistry registry = RegistryFactory.getRegistry();
        final IConfigurationElement[] elements = registry.getConfigurationElementsFor("smartgridsecurity.graphiti.extension.deletefeature");

        for (final IConfigurationElement ele : elements) {
            try {
                final Object o = ele.createExecutableExtension("deleteFeature");
                if (o instanceof IDeleteFeatureResolver) {
                    final Diagram diagram = (Diagram) context.getPictogramElement().eContainer();
                    ((IDeleteFeatureResolver) o).deleteBusinessObjects(diagram.getLink().getBusinessObjects(), context.getPictogramElement().getLink().getBusinessObjects().get(0),
                            getDiagramBehavior().getEditingDomain());
                }
            } catch (final CoreException e) {
                e.printStackTrace();
            }
        }
        // If it's a NetworkEntity, delete all incoming and outgoing connections
        if (context.getPictogramElement().getLink().getBusinessObjects().get(0) instanceof NetworkEntity) {
            final NetworkEntity node = (NetworkEntity) context.getPictogramElement().getLink().getBusinessObjects().get(0);
            deleteBusinessObjects(node.getLinkedBy().toArray());
            if (context.getPictogramElement().getLink().getBusinessObjects().get(0) instanceof CommunicatingEntity) {
                final CommunicatingEntity commEntity = (CommunicatingEntity) context.getPictogramElement().getLink().getBusinessObjects().get(0);
                deleteBusinessObjects(commEntity.getCommunicatesBy().toArray());
            }
        }

        super.delete(context);
    }
}
