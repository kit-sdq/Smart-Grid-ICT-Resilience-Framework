package smartgrid.helper;

import java.util.HashMap;

/**
 * Helper for reappearing {@link HashMap} operations
 * 
 * @author Misha
 */
public final class HashMapHelper {
    private HashMapHelper() {
    }

    /**
     * Creates a new HashMap instance, which is optimized for a constant size.
     * 
     * @param <K>
     *            type of keys
     * @param <V>
     *            type of values
     * @param capacity
     *            Expected max capacity
     * @return Empty HashMap
     */
    public static <K, V> HashMap<K, V> createOptimalFixedSizeHashMap(int capacity) {
        return new HashMap<>(capacity + 1, 1);
    }
}
