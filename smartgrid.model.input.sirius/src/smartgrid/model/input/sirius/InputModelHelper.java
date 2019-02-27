package smartgrid.model.input.sirius;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import smartgridinput.ScenarioState;

/**
 * @deprecated
 * 
 * 			Loading models currently works with accessing the sirius session
 *             and not by creating a proeprties file
 */
public class InputModelHelper {

	private static ScenarioState state = null;
	private static final String PROPERTIES_PATH = "models.properties";
	private static final String BASE_PROPERTY = "base";
	private static final String INPUT_PROPERTY = "input";

	public static URI getStateURI() {
		Properties prop = new Properties();
		IFile f = getPropertiesFile();
		try {
			prop.load(f.getContents());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		URI uri = URI.createURI((String) prop.get(INPUT_PROPERTY));
		return uri;
	}

	public static ScenarioState getScenarioState() {
		if (state == null) {
			try {
				loadScenarioState();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		return state;
	}

	public static void saveNewScenarioState(Resource base, Resource input) {
		IFile file = getPropertiesFile();

		// Delete file if it already exists
		if (file.exists()) {
			try {
				file.delete(IResource.FORCE, null);
			} catch (CoreException e1) {
				e1.printStackTrace();
			}
		}

		try {
			// Write file's content
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream out = new PrintStream(baos);
			out.println(BASE_PROPERTY + "=" + base.getURI().toString());
			out.println(INPUT_PROPERTY + "=" + input.getURI().toString());

			ByteArrayInputStream bin = new ByteArrayInputStream(baos.toByteArray());
			try {
				file.create(bin, true, null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
			baos.flush();
			baos.close();
			refreshProject();
			loadScenarioState();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	public static void loadScenarioState() throws IOException, CoreException {
		Properties prop = new Properties();
		IFile f = getPropertiesFile();
		prop.load(f.getContents());
		URI uri = URI.createURI((String) prop.get(INPUT_PROPERTY));
		Resource input = new ResourceSetImpl().createResource(uri);
		input.load(null);
		state = (ScenarioState) input.getContents().get(0);
	}

	private static IFile getPropertiesFile() {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("smartgrid.model.input.sirius");
		return project.getFile(PROPERTIES_PATH);
	}

	private static void refreshProject() throws CoreException {
		ResourcesPlugin.getWorkspace().getRoot().getProject("smartgrid.model.input.sirius").refreshLocal(1, null);
	}

	public static void callMessageDialog(String message) {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		final MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR);
		messageBox.setMessage(message);
		messageBox.open();
	}
}
