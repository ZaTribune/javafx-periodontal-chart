package com.tribune.dental_chart.handler;

import com.jfoenix.controls.JFXSlider;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import com.tribune.dental_chart.entity.MouthItem;

/**
 * RowHandler for Gum Width slider (row 7).
 * Manages a single slider control for adjusting gum width for each tooth.
 */
public class GumWidthRowHandler implements RowHandler {

    /**
     * Configures the gum width slider for a specific tooth.
     *
     * @param node       The UI node (JFXSlider)
     * @param item       The MouthItem associated with this tooth
     * @param gridPane   The GridPane containing the node
     * @param context    The context with callbacks
     * @param columnIndex The column index (tooth number 1-17)
     */
    @Override
    public void handle(Node node, MouthItem item, GridPane gridPane, RowHandlerContext context, int columnIndex) {
        if (node == null || !(node instanceof JFXSlider)) {
            return;
        }

        JFXSlider gumWidthSlider = (JFXSlider) node;

        // Add listener for value changes
        gumWidthSlider.valueChangingProperty().addListener((observable) -> {
            item.setGum(gumWidthSlider.getValue());
        });

        // Set initial value from item
        gumWidthSlider.setValue(item.getGum() != null ? item.getGum() : 0.0);
    }
}

