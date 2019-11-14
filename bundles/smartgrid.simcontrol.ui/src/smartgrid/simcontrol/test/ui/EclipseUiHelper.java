package smartgrid.simcontrol.test.ui;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * @author Misha
 */
public class EclipseUiHelper {

    private static final Logger LOG = Logger.getLogger(EclipseUiHelper.class);

    private EclipseUiHelper() {
    }

    public static Shell getShell() {
        return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
    }

    public static void prompt(String message, Shell shell) {
        Display.getDefault().asyncExec(() -> MessageDialog.openInformation(shell, "Smart Grid SimControl", message));
        LOG.info("Prompt: " + message);
    }
}
