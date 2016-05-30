package smartgridsecurity.graphiti.helper;

import java.util.UUID;

/**
 * Helper class to generate unique id's.
 *
 * @author mario
 *
 */
public final class UIDHelper {

    /**
     * Generate unique id.
     *
     * @return the new id
     */
    public static int generateUID() {
        final UUID idOne = UUID.randomUUID();
        String str = "" + idOne;
        final int uid = str.hashCode();
        final String filterStr = "" + uid;
        str = filterStr.replaceAll("-", "");
        return Integer.parseInt(str);
    }

}
