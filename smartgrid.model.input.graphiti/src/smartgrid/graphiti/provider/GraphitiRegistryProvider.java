package smartgrid.graphiti.provider;

import org.eclipse.graphiti.features.IFeatureProvider;

/**
 * This class provides important Graphiti features to the input model plugin.
 * 
 * @author mario
 *
 */
public class GraphitiRegistryProvider {
    private IFeatureProvider featureProvider;
    private static GraphitiRegistryProvider instance;

    private GraphitiRegistryProvider() {
    }

    public static GraphitiRegistryProvider getInstance() {
        if (instance == null) {
            instance = new GraphitiRegistryProvider();
        }
        return instance;
    }

    public void setFeatureProvider(IFeatureProvider fp) {
        featureProvider = fp;
    }

    public IFeatureProvider getFeatureProvider() {
        return featureProvider;
    }

}
