package smartgrid.model.input.sirius;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.gmf.runtime.gef.ui.figures.DefaultSizeNodeFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.sirius.diagram.CustomStyle;
import org.eclipse.sirius.diagram.DNode;
import org.eclipse.sirius.diagram.ui.edit.api.part.AbstractNotSelectableShapeNodeEditPart;
import org.eclipse.sirius.diagram.ui.edit.api.part.IStyleEditPart;
import org.eclipse.sirius.diagram.ui.tools.api.figure.AirStyleDefaultSizeNodeFigure;
import org.eclipse.sirius.viewpoint.SiriusPlugin;
import org.eclipse.swt.graphics.Image;

public class BrokenNodeEditPart extends AbstractNotSelectableShapeNodeEditPart implements IStyleEditPart {

	private ImageFigure primaryShape;

	public BrokenNodeEditPart(View view) {
		super(view);
	}

	@Override
	protected NodeFigure createNodeFigure() {
		NodeFigure figure = createNodePlate();
		figure.setLayoutManager(new XYLayout());
		IFigure shape = createNodeShape();
		figure.add(shape);
		return figure;
	}

	private NodeFigure createNodePlate() {
		DefaultSizeNodeFigure result = new AirStyleDefaultSizeNodeFigure(getMapMode().DPtoLP(40),
				getMapMode().DPtoLP(40));
		return result;
	}

	/**
	 * Create the instance role figure.
	 *
	 * @return the created figure.
	 */
	protected ImageFigure createNodeShape() {
		if (primaryShape == null) {
			primaryShape = new ImageFigure();
		}
		return primaryShape;
	}

	protected void refreshVisuals() {
		CustomStyle customStyle = (CustomStyle) this.resolveSemanticElement();
		if (customStyle.eContainer() instanceof DNode) {
			this.primaryShape
					.setImage((Image) SiriusPlugin.getDefault().getImage(((DNode) customStyle.eContainer()).getName()));
		}
	}

	protected void createDefaultEditPolicies() {
		// empty.
	}

}
