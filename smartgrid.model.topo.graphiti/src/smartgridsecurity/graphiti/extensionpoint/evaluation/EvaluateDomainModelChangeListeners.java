package smartgridsecurity.graphiti.extensionpoint.evaluation;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.graphiti.ui.editor.DiagramBehavior;

import smartgridsecurity.graphiti.extensionpoint.definition.IDomainModelChangeListenerResolver;

/**
 * This class evaluates the resource listener extension point and adds all
 * listener instances to a list.
 * 
 * @author mario
 *
 */
public class EvaluateDomainModelChangeListeners {
	private static final Logger LOG = Logger.getLogger(EvaluateDomainModelChangeListeners.class);

	private static final String RESIZE_FEATURES_ID = "smartgridsecurity.graphiti.extension.domain.changed";
	private DiagramBehavior db;

	public EvaluateDomainModelChangeListeners(DiagramBehavior db) {
		this.db = db;
	}

	/**
	 * Evaluates all feature extensions.
	 * 
	 * @param registry
	 *            the extension registry of the current platform
	 * @return all found feature extensions
	 */
	public List<ResourceSetListener> evaluateFeatureExtension(IExtensionRegistry registry) {
		return evaluate(registry);
	}

	/**
	 * Private method to evaluate all feature extensions.
	 * 
	 * @param registry
	 *            the extension registry of the current platform
	 * @return all found feature extensions
	 */
	private List<ResourceSetListener> evaluate(IExtensionRegistry registry) {
		IConfigurationElement[] config = registry.getConfigurationElementsFor(RESIZE_FEATURES_ID);

		// define the thread pool
		ExecutorService pool = Executors.newCachedThreadPool();
		List<Future<List<ResourceSetListener>>> list = new LinkedList<Future<List<ResourceSetListener>>>();
		List<ResourceSetListener> listenerList = new LinkedList<ResourceSetListener>();

		try {
			for (IConfigurationElement e : config) {
				LOG.info("Evaluating resource listener extension");
				final Object o = e.createExecutableExtension("class");
				if (o instanceof IDomainModelChangeListenerResolver) {
					// Executes the evaluation in a thread an returns the result
					// in the future
					Callable<List<ResourceSetListener>> callable = new EvaluateAbstractPattern(
							(IDomainModelChangeListenerResolver) o, db);
					Future<List<ResourceSetListener>> future = pool.submit(callable);
					list.add(future);
				}
			}

			// add all AbstractPattern to list
			for (Future<List<ResourceSetListener>> buttn : list) {
				try {
					for (ResourceSetListener tba : buttn.get()) {
						listenerList.add(tba);
					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				} catch (ExecutionException e1) {
					e1.printStackTrace();
				}
			}
		} catch (CoreException ex) {
			LOG.error("[EvaluateDomainModelChangeListeners]: CoreException occured, message is: " + ex.getMessage());
		}
		return listenerList;
	}

	/**
	 * Private class to evaluate features in a thread.
	 *
	 */
	private class EvaluateAbstractPattern implements Callable<List<ResourceSetListener>> {

		private IDomainModelChangeListenerResolver resolver;
		private DiagramBehavior behavior;

		public EvaluateAbstractPattern(IDomainModelChangeListenerResolver f, DiagramBehavior fp) {
			this.resolver = f;
			this.behavior = fp;
		}

		@Override
		public List<ResourceSetListener> call() throws Exception {
			return resolver.getDomainModelChangeListener(behavior);
		}

	}

}
