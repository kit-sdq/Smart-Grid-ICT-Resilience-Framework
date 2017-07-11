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
 *
 * @author mario
 *
 */
public class IdentifierPropertiesSheet extends GFPropertySection implements ITabbedPropertyConstants {

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
     * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#createControls
     * (org.eclipse.swt.widgets.Composite,
     * org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
     */
    @Override
    public void createControls(final Composite parent, final TabbedPropertySheetPage tabbedPropertySheetPage) {
        super.createControls(parent, tabbedPropertySheetPage);

        final TabbedPropertySheetWidgetFactory factory = this.getWidgetFactory();
        final Composite composite = factory.createFlatFormComposite(parent);

        final GridLayout layout = new GridLayout(2, false);
        composite.setLayout(layout);

        this.label = new Label(composite, SWT.NONE);
        this.label.setText("Identifier: ");

        this.idWidget = new Text(composite, SWT.BORDER);
        final GridData gridData = new GridData();
        gridData.horizontalAlignment = SWT.FILL;
        gridData.grabExcessHorizontalSpace = true;
        this.idWidget.setLayoutData(gridData);
        this.idWidget.setText("0");

        this.idWidget.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                final IFeature feature = new AbstractFeature(IdentifierPropertiesSheet.this.getDiagramTypeProvider().getFeatureProvider()) {

                    @Override
                    public void execute(final IContext context) {
                        final PictogramElement pe = IdentifierPropertiesSheet.this.getSelectedPictogramElement();
                        if (pe != null) {
                            final Object bo = Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(pe);
                            if (bo == null) {
                                return;
                            }

                            final Identifier identifier = (Identifier) bo;
                            final String id = IdentifierPropertiesSheet.this.idWidget.getText();
                            if (!(id.equals(Integer.toString(identifier.getId())))) {
                                identifier.setId(Integer.parseInt(id));
                            }
                        }
                    }

                    @Override
                    public boolean canExecute(final IContext context) {
                        return true;
                    }
                };
                IdentifierPropertiesSheet.this.execute(feature, new CustomContext());

            }
        });
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#refresh()
     */
    @Override
    public void refresh() {
        final PictogramElement pe = this.getSelectedPictogramElement();
        if (pe != null) {

            final EObject bo = Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(pe);
            // the filter assured, that it is a EClass

            if (bo == null) {
                return;
            }

            if (((Identifier) bo).getId() != 0) {
                this.idWidget.setText(Integer.toString(((Identifier) bo).getId()));
            } else {
                this.idWidget.setText("");
            }
        }
    }
}
