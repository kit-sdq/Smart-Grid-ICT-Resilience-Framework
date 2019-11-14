package smartgrid.helper;

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
    public static String generateUID() {
        final UUID idOne = UUID.randomUUID();
        String str = idOne.toString();
        final int uid = str.hashCode();
        final String filterStr = "" + uid;
        str = filterStr.replaceAll("-", "");
        return str;
    }

}
