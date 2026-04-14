package com.tribune.dental_chart.handler;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.IntegerProperty;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import com.tribune.dental_chart.DentalChartUtils;
import com.tribune.dental_chart.entity.MouthItem;

/**
 * RowHandler for Plaque buttons (rows 6 and 13).
 * Manages two sets of 3 plaque buttons each (set 1: plaque1-3, set 2: plaque4-6).
 * Handles click events, cycling through states (0→1→2→0), and SVG styling.
 */
public class PlaqueRowHandler implements RowHandler {
    private static final int MAX_PLAQUE_STATE = 2;

    /**
     * Configures plaque buttons for a specific tooth.
     *
     * @param node       The UI node (HBox containing 3 plaque buttons)
     * @param item       The MouthItem associated with this tooth
     * @param gridPane   The GridPane containing the node
     * @param context    The context with callbacks
     * @param columnIndex The column index (tooth number 1-17)
     */
    @Override
    public void handle(Node node, MouthItem item, GridPane gridPane, RowHandlerContext context, int columnIndex) {
        if (node == null || !(node instanceof HBox)) {
            return;
        }

        HBox plaqueBox = (HBox) node;
        if (plaqueBox.getChildren().size() < 3) {
            return; // Ensure we have 3 buttons
        }

        JFXButton button1 = (JFXButton) plaqueBox.getChildren().get(0);
        JFXButton button2 = (JFXButton) plaqueBox.getChildren().get(1);
        JFXButton button3 = (JFXButton) plaqueBox.getChildren().get(2);

        int rowIndex = GridPane.getRowIndex(node);

        if (rowIndex == 6) {
            // Plaque Set 1 (buttons 1, 2, 3)
            configurePlaqueSet(button1, button2, button3, item, context,
                    item.plaque1Property(), item.plaque2Property(), item.plaque3Property());
        } else if (rowIndex == 13) {
            // Plaque Set 2 (buttons 4, 5, 6)
            configurePlaqueSet(button1, button2, button3, item, context,
                    item.plaque4Property(), item.plaque5Property(), item.plaque6Property());
        }
    }

    /**
     * Configures a set of 3 plaque buttons with their respective properties.
     */
    private void configurePlaqueSet(JFXButton button1, JFXButton button2, JFXButton button3,
                                    MouthItem item, RowHandlerContext context,
                                    IntegerProperty prop1, IntegerProperty prop2, IntegerProperty prop3) {

        // Initial styling
        DentalChartUtils.stylePlaqueSVG(button1, prop1.get());
        DentalChartUtils.stylePlaqueSVG(button2, prop2.get());
        DentalChartUtils.stylePlaqueSVG(button3, prop3.get());

        // Unified event handler for all 3 buttons
        EventHandler<MouseEvent> plaqueClickHandler = event -> {
            if (event.getSource().equals(button1)) {
                int nextValue = cycleState(prop1.get());
                prop1.set(nextValue);
                DentalChartUtils.stylePlaqueSVG(button1, nextValue);
            } else if (event.getSource().equals(button2)) {
                int nextValue = cycleState(prop2.get());
                prop2.set(nextValue);
                DentalChartUtils.stylePlaqueSVG(button2, nextValue);
            } else if (event.getSource().equals(button3)) {
                int nextValue = cycleState(prop3.get());
                prop3.set(nextValue);
                DentalChartUtils.stylePlaqueSVG(button3, nextValue);
            }
            // Update calculations after any button click
            MouthItem.calculateTotalPlaque(item);
            context.updatePlaque(false);
        };

        // Attach event handlers
        button1.setOnMouseClicked(plaqueClickHandler);
        button2.setOnMouseClicked(plaqueClickHandler);
        button3.setOnMouseClicked(plaqueClickHandler);

        // Attach property listeners for styling
        prop1.addListener((ov, oldValue, newValue) ->
            DentalChartUtils.stylePlaqueSVG(button1, (int) newValue));
        prop2.addListener((ov, oldValue, newValue) ->
            DentalChartUtils.stylePlaqueSVG(button2, (int) newValue));
        prop3.addListener((ov, oldValue, newValue) ->
            DentalChartUtils.stylePlaqueSVG(button3, (int) newValue));
    }

    /**
     * Cycles the plaque state from 0 → 1 → 2 → 0
     */
    private int cycleState(int currentState) {
        return (currentState == MAX_PLAQUE_STATE) ? 0 : currentState + 1;
    }
}

