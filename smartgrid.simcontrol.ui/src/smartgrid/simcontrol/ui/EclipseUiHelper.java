package smartgrid.simcontrol.ui;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class EclipseUiHelper {
    private EclipseUiHelper() {
    }

    public static Shell getShell() {
        return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
    }

    public static void prompt(String message, Shell shell) {
        MessageDialog.openInformation(shell, "Metamodel Assessor", message);
        System.out.println(message);
    }
}
