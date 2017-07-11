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
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;

import smartgridsecurity.graphiti.extensionpoint.definition.IContextButtonResolver;

/**
 * This class evaluates the context button extension point and adds all context button instances to
 * a list.
 *
 * @author mario
 *
 */
public class EvaluateContextButtons {
    private static final Logger LOG = Logger.getLogger(EvaluateContextButtons.class);

    private static final String CONTEXT_BUTTON_ID = "smartgridsecurity.graphiti.extension.contextbutton";
    private final IFeatureProvider fp;

    public EvaluateContextButtons(final IFeatureProvider f) {
        this.fp = f;
    }

    /**
     * Evaluates all feature extensions.
     *
     * @param registry
     *            the extension registry of the current platform
     * @return all found feature extensions
     */
    public List<AbstractCustomFeature> evaluateFeatureExtension(final IExtensionRegistry registry) {
        return this.evaluate(registry);
    }

    /**
     * Private method to evaluate all feature extensions.
     *
     * @param registry
     *            the extension registry of the current platform
     * @return all found feature extensions
     */
    private List<AbstractCustomFeature> evaluate(final IExtensionRegistry registry) {
        final IConfigurationElement[] config = registry.getConfigurationElementsFor(CONTEXT_BUTTON_ID);

        // define the thread pool
        final ExecutorService pool = Executors.newCachedThreadPool();
        final List<Future<List<AbstractCustomFeature>>> list = new LinkedList<Future<List<AbstractCustomFeature>>>();
        final List<AbstractCustomFeature> AbstractCustomFeatureList = new LinkedList<AbstractCustomFeature>();

        try {
            for (final IConfigurationElement e : config) {
                LOG.debug("Evaluating custom button extension");
                final Object o = e.createExecutableExtension("class");
                if (o instanceof IContextButtonResolver) {
                    // Executes the evaluation in a thread an returns the result
                    // in the future
                    final Callable<List<AbstractCustomFeature>> callable = new EvaluateAbstractPattern((IContextButtonResolver) o, this.fp);
                    final Future<List<AbstractCustomFeature>> future = pool.submit(callable);
                    list.add(future);
                }
            }

            // add all AbstractPattern to list
            for (final Future<List<AbstractCustomFeature>> buttn : list) {
                try {
                    for (final AbstractCustomFeature tba : buttn.get()) {
                        AbstractCustomFeatureList.add(tba);
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
        return AbstractCustomFeatureList;
    }

    /**
     * Private class to evaluate features in a thread.
     *
     */
    private class EvaluateAbstractPattern implements Callable<List<AbstractCustomFeature>> {

        private final IContextButtonResolver resolver;
        private final IFeatureProvider provider;

        public EvaluateAbstractPattern(final IContextButtonResolver f, final IFeatureProvider fp) {
            this.resolver = f;
            this.provider = fp;
        }

        @Override
        public List<AbstractCustomFeature> call() throws Exception {
            return this.resolver.getContextButtons(this.provider);
        }

    }

}
