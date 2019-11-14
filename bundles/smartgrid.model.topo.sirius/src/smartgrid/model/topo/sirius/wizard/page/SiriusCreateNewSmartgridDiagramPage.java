package smartgrid.model.topo.sirius.wizard.page;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Custom page to create new smartgrid diagramms.
 *
 * @author mw
 *
 */
public class SiriusCreateNewSmartgridDiagramPage extends WizardPage {

    private Text topologyName;

    public SiriusCreateNewSmartgridDiagramPage() {
        super("wizardPage");
        setTitle("Smartgrid project");
        setDescription("Enter diagram name");
    }

    @Override
    public void createControl(final Composite parent) {
        final Composite container = new Composite(parent, SWT.NULL);
        setPageComplete(false);

        setControl(container);
        container.setLayout(new GridLayout(3, false));

        final Label lblTopologyName = new Label(container, SWT.NONE);
        lblTopologyName.setText("Topology Name");

        
        
        topologyName = new Text(container, SWT.BORDER);
        final GridData gridData2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
        gridData2.widthHint = 300;
        topologyName.setLayoutData(gridData2);
        topologyName.addModifyListener(e -> {
            final String name = topologyName.getText();
            SiriusCreateNewSmartgridDiagramPage.this.setPageComplete(name.matches("^[a-zA-Z0-9_]+$") ? true : false);

        });


        topologyName.setFocus();

    }

    public String getTopologyName() {
        return topologyName.getText();
    }
}
