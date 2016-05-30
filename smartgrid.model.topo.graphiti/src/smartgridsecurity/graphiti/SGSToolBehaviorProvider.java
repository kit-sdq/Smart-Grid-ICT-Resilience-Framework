package smartgridsecurity.graphiti;

import java.util.List;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.context.IPictogramElementContext;
import org.eclipse.graphiti.features.context.impl.CustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.features.custom.ICustomFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.tb.ContextButtonEntry;
import org.eclipse.graphiti.tb.DefaultToolBehaviorProvider;
import org.eclipse.graphiti.tb.IContextButtonPadData;

import smartgridsecurity.graphiti.helper.ExtensionPointRegistry;

/**
 * Specific SGS tool behavior provider. Adds all custom actions to the context menu.
 *
 * @author mario
 *
 */
public class SGSToolBehaviorProvider extends DefaultToolBehaviorProvider {

    public SGSToolBehaviorProvider(final IDiagramTypeProvider diagramTypeProvider) {
        super(diagramTypeProvider);
    }

    @Override
    public IContextButtonPadData getContextButtonPad(final IPictogramElementContext context) {
        final IContextButtonPadData data = super.getContextButtonPad(context);

        final CustomContext cc = new CustomContext(new PictogramElement[] { context.getPictogramElement() });
        final List<AbstractCustomFeature> features = ExtensionPointRegistry.getInstance().getAllContextButtons();
        for (final ICustomFeature iCustomFeature : features) {
            final ContextButtonEntry button = new ContextButtonEntry(iCustomFeature, cc);
            button.setText(iCustomFeature.getName());
            button.setIconId(iCustomFeature.getImageId());
            data.getGenericContextButtons().add(button);
        }
        return data;
    }

}
