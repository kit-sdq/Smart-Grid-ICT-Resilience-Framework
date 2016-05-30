package smartgridsecurity.graphiti.propertysheet;

import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.platform.AbstractPropertySectionFilter;

import smartgridtopo.Identifier;
import smartgridtopo.SmartGridTopology;

/**
 * Filter class to display only id's in the id property sheet.
 *
 * @author mario
 *
 */
public class IdentifierPropertyFilter extends AbstractPropertySectionFilter {

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.graphiti.ui.platform.AbstractPropertySectionFilter#accept(org
     * .eclipse.graphiti.mm.pictograms.PictogramElement)
     */
    @Override
    protected boolean accept(final PictogramElement pictogramElement) {
        final Object instance = Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(pictogramElement);

        if (instance instanceof Identifier && !(instance instanceof SmartGridTopology)) {
            return true;
        } else {
            return false;
        }
    }

}
