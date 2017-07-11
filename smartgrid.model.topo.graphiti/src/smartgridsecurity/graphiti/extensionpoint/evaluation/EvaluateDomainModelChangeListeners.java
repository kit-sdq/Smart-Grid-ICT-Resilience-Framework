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
 * This class evaluates the resource listener extension point and adds all listener instances to a
 * list.
 *
 * @author mario
 *
 */
public class EvaluateDomainModelChangeListeners {
    private static final Logger LOG = Logger.getLogger(EvaluateDomainModelChangeListeners.class);

    private static final String RESIZE_FEATURES_ID = "smartgridsecurity.graphiti.extension.domain.changed";
    private final DiagramBehavior db;

    public EvaluateDomainModelChangeListeners(final DiagramBehavior db) {
        this.db = db;
    }

    /**
     * Evaluates all feature extensions.
     *
     * @param registry
     *            the extension registry of the current platform
     * @return all found feature extensions
     */
    public List<ResourceSetListener> evaluateFeatureExtension(final IExtensionRegistry registry) {
        return this.evaluate(registry);
    }

    /**
     * Private method to evaluate all feature extensions.
     *
     * @param registry
     *            the extension registry of the current platform
     * @return all found feature extensions
     */
    private List<ResourceSetListener> evaluate(final IExtensionRegistry registry) {
        final IConfigurationElement[] config = registry.getConfigurationElementsFor(RESIZE_FEATURES_ID);

        // define the thread pool
        final ExecutorService pool = Executors.newCachedThreadPool();
        final List<Future<List<ResourceSetListener>>> list = new LinkedList<Future<List<ResourceSetListener>>>();
        final List<ResourceSetListener> listenerList = new LinkedList<ResourceSetListener>();

        try {
            for (final IConfigurationElement e : config) {
                LOG.debug("Evaluating resource listener extension");
                final Object o = e.createExecutableExtension("class");
                if (o instanceof IDomainModelChangeListenerResolver) {
                    // Executes the evaluation in a thread an returns the result
                    // in the future
                    final Callable<List<ResourceSetListener>> callable = new EvaluateAbstractPattern((IDomainModelChangeListenerResolver) o, this.db);
                    final Future<List<ResourceSetListener>> future = pool.submit(callable);
                    list.add(future);
                }
            }

            // add all AbstractPattern to list
            for (final Future<List<ResourceSetListener>> buttn : list) {
                try {
                    for (final ResourceSetListener tba : buttn.get()) {
                        listenerList.add(tba);
                    }
                } catch (final InterruptedException e1) {
                    e1.printStackTrace();
                } catch (final ExecutionException e1) {
                    e1.printStackTrace();
                }
            }
        } catch (final CoreException ex) {
            LOG.error("CoreException occured, message is: " + ex.getMessage());
        }
        return listenerList;
    }

    /**
     * Private class to evaluate features in a thread.
     *
     */
    private class EvaluateAbstractPattern implements Callable<List<ResourceSetListener>> {

        private final IDomainModelChangeListenerResolver resolver;
        private final DiagramBehavior behavior;

        public EvaluateAbstractPattern(final IDomainModelChangeListenerResolver f, final DiagramBehavior fp) {
            this.resolver = f;
            this.behavior = fp;
        }

        @Override
        public List<ResourceSetListener> call() throws Exception {
            return this.resolver.getDomainModelChangeListener(this.behavior);
        }

    }

}
