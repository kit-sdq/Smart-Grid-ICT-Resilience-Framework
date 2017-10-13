package smartgrid.simcontrol.ui.toolbar;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;

import smartgrid.simcontrol.rmi.BlockingKritisDataExchanger;
import smartgrid.simcontrol.rmi.RmiServer;

public class InterruptButtonHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) {
        RmiServer.resetState();
        BlockingKritisDataExchanger.freeAll();

        /* This code shows a popup that is not really necessary anymore */
//        boolean threadsFreed = BlockingKritisDataExchanger.freeAll();
//        String message;
//        if (threadsFreed) {
//            message = "Threads were freed from the synchronization.";
//        } else {
//            message = "There were no threads waiting.";
//        }
//        Shell shell = HandlerUtil.getActiveShell(event);
//        EclipseUiHelper.prompt(message, shell);
        return null;
    }
}
