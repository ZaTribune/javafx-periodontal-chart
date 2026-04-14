package com.tribune.dental_chart.handler;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory for creating and managing RowHandler instances.
 * Maps row indices to their corresponding handlers using a registry pattern.
 * This class centralizes the logic for determining which handler to use for each row,
 * making it easy to add new row types or modify existing ones.
 */
public class RowHandlerFactory {
    private static final Map<Integer, RowHandler> handlers = new HashMap<>();

    static {
        // Initialize handlers for each row type
        handlers.put(1, new AvailableRowHandler());      // Available
        handlers.put(2, new ImplantRowHandler());        // Implant
        handlers.put(3, new MobilityRowHandler());       // Mobility
        handlers.put(4, new ForkRowHandler());           // Fork
        handlers.put(5, new BleedingRowHandler());       // Bleeding Set 1
        handlers.put(6, new PlaqueRowHandler());         // Plaque Set 1
        handlers.put(7, new GumWidthRowHandler());       // Gum Width
        handlers.put(8, new GingivalMarginRowHandler()); // Gingival Margin Set 1
        handlers.put(9, new ProbingDepthRowHandler());   // Probing Depth Set 1
        // handlers.put(10, new ToothImageRowHandler()); // Optional
        handlers.put(11, new ProbingDepthRowHandler());  // Probing Depth Set 2
        handlers.put(12, new GingivalMarginRowHandler()); // Gingival Margin Set 2
        handlers.put(13, new PlaqueRowHandler());        // Plaque Set 2
        handlers.put(14, new BleedingRowHandler());      // Bleeding Set 2
        handlers.put(15, new ForkRowHandler());          // Fork Set 2
        // handlers.put(16, new NoteRowHandler());       // Note (future enhancement)
    }

    /**
     * Gets the handler for a specific row index.
     *
     * @param rowIndex The row index (1-15)
     * @return The handler for this row, or null if not yet implemented
     */
    public static RowHandler getHandler(int rowIndex) {
        return handlers.get(rowIndex);
    }

    /**
     * Registers a custom handler for a specific row index.
     * Useful for testing or runtime configuration.
     *
     * @param rowIndex The row index (1-15)
     * @param handler  The handler implementation
     */
    public static void registerHandler(int rowIndex, RowHandler handler) {
        handlers.put(rowIndex, handler);
    }

    /**
     * Checks if a handler exists for the given row index.
     *
     * @param rowIndex The row index to check
     * @return true if a handler is registered, false otherwise
     */
    public static boolean hasHandler(int rowIndex) {
        return handlers.containsKey(rowIndex);
    }

    /**
     * Clears all handlers (useful for testing).
     */
    public static void clear() {
        handlers.clear();
    }
}

