package smartgridsecurity.graphiti;

import org.eclipse.graphiti.util.ColorConstant;

/**
 * This class provides global used constants.
 * @author mario
 *
 */
public class ConstantProvider {
    public static final int shapeLineWidth = 3;
    public static final int connectionLineWidth = 2;
    public static final int questionMarkLineWidth = 3;
    
    public static final ColorConstant FOREGROUND_BLACK = new ColorConstant(0, 0, 0);
    public static final ColorConstant CONTROL_CENTER_BACKGROUND = new ColorConstant(51, 102, 0);
    public static final ColorConstant GENERIC_CONTROLLER_FOREGROUND = new ColorConstant(210, 60, 0);
    public static final ColorConstant GENERIC_CONTROLLER_BACKGROUND = new ColorConstant(255, 140, 0);
    public static final ColorConstant POWER_GRID_NODE_BACKGROUND = new ColorConstant(255, 255, 0);
    public static final ColorConstant INTER_COM_FOREGROUND = new ColorConstant(0, 139, 0);
    public static final ColorConstant INTER_COM_BACKGROUND = new ColorConstant(127, 255, 0);
    public static final ColorConstant NETWORK_NODE_FOREGROUND = new ColorConstant(0, 51, 102);
    public static final ColorConstant NETWORK_NODE_BACKGROUND = new ColorConstant(0, 102, 204);
    public static final ColorConstant SMART_METER_FOREGROUND = new ColorConstant(140, 0, 0);
    public static final ColorConstant SMART_METER_BACKGROUND = new ColorConstant(255, 0, 0);
}
