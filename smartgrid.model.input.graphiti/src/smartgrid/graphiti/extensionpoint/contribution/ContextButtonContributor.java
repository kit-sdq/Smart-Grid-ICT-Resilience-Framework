package smartgrid.graphiti.extensionpoint.contribution;

import java.util.Arrays;
import java.util.List;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;

import smartgrid.graphiti.customfeatures.NodeDestoryedFeature;
import smartgrid.graphiti.customfeatures.PowerEnabledFeature;
import smartgrid.graphiti.provider.GraphitiRegistryProvider;
import smartgridsecurity.graphiti.extensionpoint.definition.IContextButtonResolver;

/**
 * Contributor class to contribute all context buttons to the main editor.
 * 
 * @author mario
 *
 */
public class ContextButtonContributor implements IContextButtonResolver {

    @Override
    public List<AbstractCustomFeature> getContextButtons(IFeatureProvider fp) {
        AbstractCustomFeature[] actions = { new NodeDestoryedFeature(fp), new PowerEnabledFeature(fp) };
        GraphitiRegistryProvider.getInstance().setFeatureProvider(fp);
        return Arrays.asList(actions);
    }

}
