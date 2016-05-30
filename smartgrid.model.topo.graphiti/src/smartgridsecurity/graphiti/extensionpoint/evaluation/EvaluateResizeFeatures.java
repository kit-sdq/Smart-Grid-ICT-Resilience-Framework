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
    private IFeatureProvider fp;

    public EvaluateResizeFeatures(IFeatureProvider f) {
        fp = f;
    }

    /**
     * Evaluates all feature extensions.
     * 
     * @param registry
     *            the extension registry of the current platform
     * @return all found feature extensions
     */
    public List<IResizeShapeFeature> evaluateFeatureExtension(IExtensionRegistry registry) {
        return evaluate(registry);
    }

    /**
     * Private method to evaluate all feature extensions.
     * 
     * @param registry
     *            the extension registry of the current platform
     * @return all found feature extensions
     */
    private List<IResizeShapeFeature> evaluate(IExtensionRegistry registry) {
        IConfigurationElement[] config = registry.getConfigurationElementsFor(RESIZE_FEATURES_ID);

        // define the thread pool
        ExecutorService pool = Executors.newCachedThreadPool();
        List<Future<List<IResizeShapeFeature>>> list = new LinkedList<Future<List<IResizeShapeFeature>>>();
        List<IResizeShapeFeature> resizeShapeFeatureList = new LinkedList<IResizeShapeFeature>();

        try {
            for (IConfigurationElement e : config) {
                LOG.info("Evaluating resize shape extension");
                final Object o = e.createExecutableExtension("class");
                if (o instanceof IResizeFeatureResolver) {
                    // Executes the evaluation in a thread an returns the result in the future
                    Callable<List<IResizeShapeFeature>> callable = new EvaluateAbstractPattern(
                            (IResizeFeatureResolver) o, fp);
                    Future<List<IResizeShapeFeature>> future = pool.submit(callable);
                    list.add(future);
                }
            }

            // add all AbstractPattern to list
            for (Future<List<IResizeShapeFeature>> buttn : list) {
                try {
                    for (IResizeShapeFeature tba : buttn.get()) {
                        resizeShapeFeatureList.add(tba);
                    }
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                } catch (ExecutionException e1) {
                    e1.printStackTrace();
                }
            }
        } catch (CoreException ex) {
            LOG.error("[EvaluateResizeFeatures]: CoreException occured, message is: " + ex.getMessage());
        }
        return resizeShapeFeatureList;
    }

    /**
     * Private class to evaluate features in a thread.
     *
     */
    private class EvaluateAbstractPattern implements Callable<List<IResizeShapeFeature>> {

        private IResizeFeatureResolver resolver;
        private IFeatureProvider provider;

        public EvaluateAbstractPattern(IResizeFeatureResolver f, IFeatureProvider fp) {
            this.resolver = f;
            this.provider = fp;
        }

        @Override
        public List<IResizeShapeFeature> call() throws Exception {
            return resolver.getResizeShapeFeature(provider);
        }

    }

}
