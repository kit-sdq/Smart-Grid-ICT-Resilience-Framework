package smartgridsecurity.graphiti.helper;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.ui.editor.IDiagramContainerUI;

/**
 * Helper class which holds all important Graphiti instances.
 * 
 * @author mario
 *
 */
public class GraphitiHelper {

    private static GraphitiHelper instance = null;
    private IFeatureProvider featureProvider;
    private IDiagramContainerUI diagramContainer;
    private Diagram diagram;

    public IFeatureProvider getFeatureProvider() {
        return featureProvider;
    }

    public void setFeatureProvider(IFeatureProvider featureProvider) {
        this.featureProvider = featureProvider;
    }

    public IDiagramContainerUI getDiagramContainer() {
        return diagramContainer;
    }

    public void setDiagramContainer(IDiagramContainerUI diagramContainer) {
        this.diagramContainer = diagramContainer;
    }

    public Diagram getDiagram() {
        return diagram;
    }

    public void setDiagram(Diagram diagram) {
        this.diagram = diagram;
    }

    private GraphitiHelper() {
    };

    public static GraphitiHelper getInstance() {
        if (instance == null) {
            instance = new GraphitiHelper();
        }
        return instance;
    }

}
