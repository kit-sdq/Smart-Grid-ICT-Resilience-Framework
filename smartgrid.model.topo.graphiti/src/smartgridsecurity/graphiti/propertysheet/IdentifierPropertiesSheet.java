package smartgridsecurity.graphiti.propertysheet;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.impl.CustomContext;
import org.eclipse.graphiti.features.impl.AbstractFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

import smartgridtopo.Identifier;

/**
 * Property sheet implementation to display id's.
 * @author mario
 *
 */
public class IdentifierPropertiesSheet extends GFPropertySection implements
		ITabbedPropertyConstants {

	// UI elements
	protected Text idWidget;
	private Label label;

	/**
	 * The business object.
	 */
	protected EObject bo = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#createControls
	 * (org.eclipse.swt.widgets.Composite,
	 * org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);

		final TabbedPropertySheetWidgetFactory factory = getWidgetFactory();
		final Composite composite = factory.createFlatFormComposite(parent);

		GridLayout layout = new GridLayout(2, false);
		composite.setLayout(layout);
		
		label = new Label(composite, SWT.NONE);
		label.setText("Identifier: ");
		
		idWidget = new Text(composite, SWT.BORDER);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		idWidget.setLayoutData(gridData);
		idWidget.setText("0");

		idWidget.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(final ModifyEvent e) {
				IFeature feature = new AbstractFeature(getDiagramTypeProvider().getFeatureProvider()) {

					@Override
					public void execute(IContext context) {
						final PictogramElement pe = getSelectedPictogramElement();
						if (pe != null) {
							Object bo = Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(pe);
							if (bo == null)
								return;

							Identifier identifier = (Identifier) bo;
							final String id = ((Text)idWidget).getText();
							if (!(id.equals(Integer.toString(identifier.getId())))) {
								identifier.setId(Integer.parseInt(id));
							}
						}
					}

					@Override
					public boolean canExecute(IContext context) {
						return true;
					}
				};
				execute(feature, new CustomContext());

			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#refresh()
	 */
	@Override
	public void refresh() {
		PictogramElement pe = getSelectedPictogramElement();
		if (pe != null) {

			EObject bo = Graphiti.getLinkService()
					.getBusinessObjectForLinkedPictogramElement(pe);
			// the filter assured, that it is a EClass

			if (bo == null) {
				return;
			}
			
            if (((Identifier)bo).getId() != 0) {
    			idWidget.setText(Integer.toString(((Identifier) bo).getId()));
            } else {
            	idWidget.setText("");
            }
		}
	}
}
