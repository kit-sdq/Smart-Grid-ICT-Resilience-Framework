package smartgrid.simcontrol.ui.toolbar;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import smartgrid.simcontrol.rmi.BlockingKritisDataExchanger;
import smartgrid.simcontrol.ui.EclipseUiHelper;

public class InterruptButtonHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) {
        boolean threadsFreed = BlockingKritisDataExchanger.freeAll();
        String message;
        if (threadsFreed) {
            message = "Threads were freed from the synchronization.";
        } else {
            message = "There were no threads waiting.";
        }
        Shell shell = HandlerUtil.getActiveShell(event);
        EclipseUiHelper.prompt(message, shell);
        return null;
    }
}
