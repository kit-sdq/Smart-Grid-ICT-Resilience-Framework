package smartgridsecurity.graphiti.propertysheet;

import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.platform.AbstractPropertySectionFilter;

import smartgridtopo.NamedEntity;
import smartgridtopo.Scenario;

/**
 * Filter used to display only names in the name property sheet.
 * @author mario
 *
 */
public class NamedEntityPropertyFilter extends AbstractPropertySectionFilter {

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.ui.platform.AbstractPropertySectionFilter#accept(org.eclipse.graphiti.mm.pictograms.PictogramElement)
	 */
	@Override
	protected boolean accept(PictogramElement pictogramElement) {
		Object instance = Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(pictogramElement);
		
		if(instance instanceof NamedEntity && !(instance instanceof Scenario)) {
			return true;
		} else {
			return false;
		}
	}

}
