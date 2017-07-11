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
import org.eclipse.graphiti.features.IResizeShapeFeature;

import smartgridsecurity.graphiti.extensionpoint.definition.IResizeFeatureResolver;

/**
 * This class evaluates the resize feature extension point and adds all resize feature instances to
 * a list.
 *
 * @author mario
 *
 */
public class EvaluateResizeFeatures {
    private static final Logger LOG = Logger.getLogger(EvaluateResizeFeatures.class);

    private static final String RESIZE_FEATURES_ID = "smartgridsecurity.graphiti.extension.resizefeature";
    private final IFeatureProvider fp;

    public EvaluateResizeFeatures(final IFeatureProvider f) {
        fp = f;
    }

    /**
     * Evaluates all feature extensions.
     *
     * @param registry
     *            the extension registry of the current platform
     * @return all found feature extensions
     */
    public List<IResizeShapeFeature> evaluateFeatureExtension(final IExtensionRegistry registry) {
        return evaluate(registry);
    }

    /**
     * Private method to evaluate all feature extensions.
     *
     * @param registry
     *            the extension registry of the current platform
     * @return all found feature extensions
     */
    private List<IResizeShapeFeature> evaluate(final IExtensionRegistry registry) {
        final IConfigurationElement[] config = registry.getConfigurationElementsFor(RESIZE_FEATURES_ID);

        // define the thread pool
        final ExecutorService pool = Executors.newCachedThreadPool();
        final List<Future<List<IResizeShapeFeature>>> list = new LinkedList<>();
        final List<IResizeShapeFeature> resizeShapeFeatureList = new LinkedList<>();

        try {
            for (final IConfigurationElement e : config) {
                LOG.info("Evaluating resize shape extension");
                final Object o = e.createExecutableExtension("class");
                if (o instanceof IResizeFeatureResolver) {
                    // Executes the evaluation in a thread an returns the result in the future
                    final Callable<List<IResizeShapeFeature>> callable = new EvaluateAbstractPattern((IResizeFeatureResolver) o, fp);
                    final Future<List<IResizeShapeFeature>> future = pool.submit(callable);
                    list.add(future);
                }
            }

            // add all AbstractPattern to list
            for (final Future<List<IResizeShapeFeature>> buttn : list) {
                try {
                    for (final IResizeShapeFeature tba : buttn.get()) {
                        resizeShapeFeatureList.add(tba);
                    }
                } catch (final InterruptedException e1) {
                    e1.printStackTrace();
                } catch (final ExecutionException e1) {
                    e1.printStackTrace();
                }
            }
        } catch (final CoreException ex) {
            LOG.error("[EvaluateResizeFeatures]: CoreException occured, message is: " + ex.getMessage());
        }
        return resizeShapeFeatureList;
    }

    /**
     * Private class to evaluate features in a thread.
     *
     */
    private class EvaluateAbstractPattern implements Callable<List<IResizeShapeFeature>> {

        private final IResizeFeatureResolver resolver;
        private final IFeatureProvider provider;

        public EvaluateAbstractPattern(final IResizeFeatureResolver f, final IFeatureProvider fp) {
            resolver = f;
            provider = fp;
        }

        @Override
        public List<IResizeShapeFeature> call() throws Exception {
            return resolver.getResizeShapeFeature(provider);
        }

    }

}
