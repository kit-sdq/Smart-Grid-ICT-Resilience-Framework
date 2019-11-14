package smartgrid.model.helper.sirius;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.diagram.ui.tools.api.editor.DDiagramEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

public abstract class LoadExtensionModel extends AbstractHandler {

    protected FileDialog dialog;
    protected DDiagramEditor editor;

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        dialog = new FileDialog(shell, SWT.OPEN);

        setFileDialogExtension();

        final String fileSelected = dialog.open();
        if (fileSelected != null) {
            File f = new File(fileSelected);

            ResourceSet set = new ResourceSetImpl();
            final Resource r = set.createResource(URI.createFileURI(f.getAbsolutePath()));

            try {
                r.load(null);
                EcoreUtil.resolveAll(set);

                IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                        .getActiveEditor();
                editor = (DDiagramEditor) part;
                final Session session = editor.getSession();
                TransactionalEditingDomain domain = (TransactionalEditingDomain) editor.getEditingDomain();
                final RecordingCommand cmd = new RecordingCommand(domain) {

                    @Override
                    protected void doExecute() {
                        session.addSemanticResource(r.getURI(), new NullProgressMonitor());
                        // Refresh editor in case certain layer is already
                        // active
                        //editor.getRepresentation().refresh();
                        //test
                    }
                };
                domain.getCommandStack().execute(cmd);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    protected abstract void setFileDialogExtension();
}
