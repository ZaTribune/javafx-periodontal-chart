package com.tribune.dental_chart.handler;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import com.tribune.dental_chart.entity.MouthItem;

/**
 * Interface for handling configuration of individual rows in the dental chart.
 * Each row type (available, implant, bleeding, plaque, etc.) has a dedicated handler
 * that sets up UI components, bindings, and event listeners.
 */
public interface RowHandler {
    /**
     * Configures the UI node for the given tooth item.
     *
     * @param node       The UI node to configure (row component)
     * @param item       The MouthItem associated with this tooth
     * @param gridPane   The GridPane containing the node (superior or inferior)
     * @param context    The context containing controller state and callbacks
     * @param columnIndex The column index (tooth number)
     */
    void handle(Node node, MouthItem item, GridPane gridPane, RowHandlerContext context, int columnIndex);
}

