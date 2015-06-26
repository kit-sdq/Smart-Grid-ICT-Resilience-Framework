package smartgridsecurity.graphiti.extensionpoint.evaluation;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;

import smartgridsecurity.graphiti.actions.ToolbarButtonAction;
import smartgridsecurity.graphiti.extensionpoint.definition.IToolbarButtonActionResolver;

/**
 * This class evaluates the toolbar action extension point and adds all toolbar action instances to a list.
 * @author mario
 *
 */
public class EvaluateToolbarActions {
	private static final String TOOLBAR_ACTION_ID = "smartgridsecurity.graphiti.extension.toolbaraction";
	
	/**
	 * Evaluates all feature extensions.
	 * @param registry the extension registry of the current platform
	 * @return all found feature extensions
	 */
	public List<ToolbarButtonAction> evaluateFeatureExtension(IExtensionRegistry registry) {
		return evaluate(registry);
	}

	/**
	 * Private method to evaluate all feature extensions.
	 * @param registry the extension registry of the current platform
	 * @return all found feature extensions
	 */
	private List<ToolbarButtonAction> evaluate(IExtensionRegistry registry) {
		IConfigurationElement[] config = registry
				.getConfigurationElementsFor(TOOLBAR_ACTION_ID);
		
		//define the thread pool
		ExecutorService pool = Executors.newCachedThreadPool();
	    List<Future<List<ToolbarButtonAction>>> list = new LinkedList<Future<List<ToolbarButtonAction>>>();
		List<ToolbarButtonAction> toolbarButtonActionList = new LinkedList<ToolbarButtonAction>();
		
		try {
			for (IConfigurationElement e : config) {
				System.out.println("Evaluating extension");
				final Object o = e.createExecutableExtension("class");
				if (o instanceof IToolbarButtonActionResolver) {
					//Executes the evaluation in a thread an returns the result in the future 
				      Callable<List<ToolbarButtonAction>> callable = new EvaluateAbstractPattern((IToolbarButtonActionResolver) o);
				      Future<List<ToolbarButtonAction>> future = pool.submit(callable);
				      list.add(future);
				}
			}

			// add all AbstractPattern to list
			for (Future<List<ToolbarButtonAction>> buttn : list) {
				try {
					for (ToolbarButtonAction tba : buttn.get()) {
						toolbarButtonActionList.add(tba);
					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				} catch (ExecutionException e1) {
					e1.printStackTrace();
				}
			}
		} catch (CoreException ex) {
			System.out.println(ex.getMessage());
		}
		return toolbarButtonActionList;
	}

	/**
	 * Private class to evaluate features in a thread.
	 *
	 */
	private class EvaluateAbstractPattern implements Callable<List<ToolbarButtonAction>> {
		
		private IToolbarButtonActionResolver buttons;
		
		public EvaluateAbstractPattern(IToolbarButtonActionResolver f) {
			this.buttons = f;
		}

		@Override
		public List<ToolbarButtonAction> call() throws Exception {
			return buttons.getToolbarButtons();
		}
		
	}

}
