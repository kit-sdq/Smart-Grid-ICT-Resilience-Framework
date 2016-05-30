package smartgridsecurity.graphiti.wizard.page;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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
public class CreateNewSmartgridDiagramPage extends WizardPage {

    private Text diagramName;

    public CreateNewSmartgridDiagramPage() {
        super("wizardPage");
        this.setTitle("Smartgrid Security Diagram");
        this.setDescription("Enter diagram name");
    }

    @Override
    public void createControl(final Composite parent) {
        final Composite container = new Composite(parent, SWT.NULL);
        this.setPageComplete(false);

        this.setControl(container);
        container.setLayout(new GridLayout(3, false));

        final Label lblDiagramName = new Label(container, SWT.NONE);
        lblDiagramName.setText("Diagram Name");

        this.diagramName = new Text(container, SWT.BORDER);
        final GridData gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
        gridData.widthHint = 300;
        this.diagramName.setLayoutData(gridData);
        this.diagramName.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                final String name = CreateNewSmartgridDiagramPage.this.diagramName.getText();
                CreateNewSmartgridDiagramPage.this.setPageComplete(name.matches("^[a-zA-Z0-9_]+$") ? true : false);

            }
        });

        this.diagramName.setFocus();

    }

    public String getFileName() {
        return this.diagramName.getText();
    }

}
