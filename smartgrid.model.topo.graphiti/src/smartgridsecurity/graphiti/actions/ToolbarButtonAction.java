package smartgridsecurity.graphiti.actions;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.graphiti.ui.editor.IDiagramContainerUI;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.internal.util.BundleUtility;
import org.osgi.framework.Bundle;

/**
 * Abstract class which has to be implemented by all toolbar actions of SGS editor.
 *
 * @author mario
 *
 */
@SuppressWarnings("restriction")
public abstract class ToolbarButtonAction extends Action {
    public String TOOL_TIP = "";
    public String TEXT = "";
    public String ACTION_ID = "";
    protected IDiagramContainerUI diagramContainer;

    public ToolbarButtonAction() {
    }

    public ToolbarButtonAction(final int style) {
        super("", style);
    }

    /**
     * Sets the current diagram container.
     *
     * @param container
     *            the current container
     */
    public abstract void setDiagramContainer(IDiagramContainerUI container);

    /**
     * Create save options.
     *
     * @return the save options
     */
    public static Map<?, ?> createSaveOptions() {
        final HashMap<String, Object> saveOptions = new HashMap<String, Object>();
        saveOptions.put(XMLResource.OPTION_ENCODING, "UTF-8"); //$NON-NLS-1$
        saveOptions.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER);
        return saveOptions;
    }

    /**
     * Creates an ImageDescriptor for a toolbar action.
     *
     * @param filename
     *            filename of the image
     * @param bundleid
     *            the current osgi plugin id
     * @return new ImageDescriptor
     */
    protected ImageDescriptor createImage(final String filename, final String bundleid) {
        final Bundle bundle = Platform.getBundle(bundleid);
        final URL fullPathString = BundleUtility.find(bundle, filename);
        final ImageDescriptor imageDcr = ImageDescriptor.createFromURL(fullPathString);
        return imageDcr;
    }

}
