package smartgrid.model.input.sirius;

import org.eclipse.gmf.runtime.diagram.ui.services.editpart.AbstractEditPartProvider;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.sirius.diagram.CustomStyle;

public class NodeBrokenEditPartProvider extends AbstractEditPartProvider {

	private static final String BROKEN_NODE_ID = "smartgrid.model.input.destroyedStyle";
	
	@Override
	protected Class<?> getNodeEditPartClass(View view) {
        if (view.getElement() instanceof CustomStyle) {
            CustomStyle customStyle = (CustomStyle) view.getElement();
            if (customStyle.getId().equals(NodeBrokenEditPartProvider.BROKEN_NODE_ID)) {
                return BrokenNodeEditPart.class;
            }
        }
        return super.getNodeEditPartClass(view);
    }
}
