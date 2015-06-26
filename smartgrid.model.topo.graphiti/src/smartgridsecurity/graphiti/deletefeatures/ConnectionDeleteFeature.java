package smartgridsecurity.graphiti.deletefeatures;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.ui.features.DefaultDeleteFeature;

import smartgridtopo.CommunicatingEntity;
import smartgridtopo.NetworkEntity;

public class ConnectionDeleteFeature extends DefaultDeleteFeature {

    public ConnectionDeleteFeature(IFeatureProvider fp) {
        super(fp);
    }
    
    @Override
    public boolean canDelete(final IDeleteContext context) {
        return true;
    }

    @Override
    public void delete(IDeleteContext context) {
        // Delete outgoing or incoming connections
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
