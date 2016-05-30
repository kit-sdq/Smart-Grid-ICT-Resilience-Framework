package smartgridsecurity.graphiti;

import org.eclipse.graphiti.dt.AbstractDiagramTypeProvider;
import org.eclipse.graphiti.tb.IToolBehaviorProvider;

/**
 * Specific SG Editor diagram type provider.
 * 
 * @author mario
 *
 */
public class SGSDiagramTypeProvider extends AbstractDiagramTypeProvider {

    private IToolBehaviorProvider[] toolBehaviorProviders;

    public SGSDiagramTypeProvider() {
        super();
        this.setFeatureProvider(new SGSFeatureProvider(this));
    }

    @Override
    public IToolBehaviorProvider[] getAvailableToolBehaviorProviders() {
        if (toolBehaviorProviders == null) {
            toolBehaviorProviders = new IToolBehaviorProvider[] { new SGSToolBehaviorProvider(this) };
        }
        return toolBehaviorProviders;
    }

    @Override
    public boolean isAutoUpdateAtRuntime() {
        return true;
    }

    @Override
    public boolean isAutoUpdateAtRuntimeWhenEditorIsSaved() {
        return true;
    }

    @Override
    public boolean isAutoUpdateAtStartup() {
        return false;
    }

    @Override
    public boolean isAutoUpdateAtReset() {
        return true;
    }

}
