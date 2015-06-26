package smartgridsecurity.graphiti;

import org.eclipse.graphiti.ui.platform.AbstractImageProvider;

/**
 * The SGS image provider to load all needed images.
 * @author mario
 *
 */
public class SGSImageProvider extends AbstractImageProvider {

	// The prefix for all identifiers of this image provider
    protected static final String PREFIX = "smartgrid.security.images.";
 
    // The image identifier for an EReference.
    public static final String IMG_BROKEN= PREFIX + "broken";
    public static final String IMG_BROKEN_NEW= PREFIX + "brokennew";
    public static final String IMG_DISABLE= PREFIX + "diable";
    public static final String IMG_ENABLE= PREFIX + "enable";
    public static final String IMG_REPAIR= PREFIX + "repair";
 
    @Override
    protected void addAvailableImages() {
        // register the path for each image identifier
        addImageFilePath(IMG_BROKEN, "icons/broken.gif");
        addImageFilePath(IMG_BROKEN_NEW, "icons/broken.png");
        addImageFilePath(IMG_DISABLE, "icons/disable.png");
        addImageFilePath(IMG_ENABLE, "icons/enable.png");
        addImageFilePath(IMG_REPAIR, "icons/repair.png");
    }

}
