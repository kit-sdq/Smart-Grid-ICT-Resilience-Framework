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
        setTitle("Smartgrid Security Diagram");
        setDescription("Enter diagram name");
    }

    @Override
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        setPageComplete(false);

        setControl(container);
        container.setLayout(new GridLayout(3, false));

        Label lblDiagramName = new Label(container, SWT.NONE);
        lblDiagramName.setText("Diagram Name");

        diagramName = new Text(container, SWT.BORDER);
        GridData gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
        gridData.widthHint = 300;
        diagramName.setLayoutData(gridData);
        diagramName.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                String name = diagramName.getText();
                setPageComplete(name.matches("^[a-zA-Z0-9_]+$") ? true : false);

            }
        });

        diagramName.setFocus();

    }

    public String getFileName() {
        return diagramName.getText();
    }

}
