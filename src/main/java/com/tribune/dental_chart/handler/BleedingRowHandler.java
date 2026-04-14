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
 * RowHandler for Bleeding/Oozing buttons (rows 5 and 14).
 * Manages two sets of 3 bleeding buttons each (set 1: bleeding1-3, set 2: bleeding4-6).
 * Handles click events, cycling through states (0→1→2→3→0), and SVG styling.
 */
public class BleedingRowHandler implements RowHandler {
    private static final int MAX_BLEEDING_STATE = 3;

    /**
     * Configures bleeding buttons for a specific tooth.
     *
     * @param node       The UI node (HBox containing 3 bleeding buttons)
     * @param item       The MouthItem associated with this tooth
     * @param gridPane   The GridPane containing the node
     * @param context    The context with callbacks
     * @param columnIndex The column index (tooth number 1-17)
     */
    @Override
    public void handle(Node node, MouthItem item, GridPane gridPane, RowHandlerContext context, int columnIndex) {
        if (!(node instanceof HBox bleedingBox)) {
            return;
        }

        if (bleedingBox.getChildren().size() < 3) {
            return; // Ensure we have 3 buttons
        }

        JFXButton button1 = (JFXButton) bleedingBox.getChildren().get(0);
        JFXButton button2 = (JFXButton) bleedingBox.getChildren().get(1);
        JFXButton button3 = (JFXButton) bleedingBox.getChildren().get(2);

        int rowIndex = GridPane.getRowIndex(node);

        if (rowIndex == 5) {
            // Bleeding Set 1 (buttons 1, 2, 3)
            configureBleedingSet(button1, button2, button3, item, context,
                    item.bleeding1Property(), item.bleeding2Property(), item.bleeding3Property());
        } else if (rowIndex == 14) {
            // Bleeding Set 2 (buttons 4, 5, 6)
            configureBleedingSet(button1, button2, button3, item, context,
                    item.bleeding4Property(), item.bleeding5Property(), item.bleeding6Property());
        }
    }

    /**
     * Configures a set of 3 bleeding buttons with their respective properties.
     */
    private void configureBleedingSet(JFXButton button1, JFXButton button2, JFXButton button3,
                                      MouthItem item, RowHandlerContext context,
                                      IntegerProperty prop1, IntegerProperty prop2, IntegerProperty prop3) {

        // Initial styling
        DentalChartUtils.styleBleedingSVG(button1, prop1.get());
        DentalChartUtils.styleBleedingSVG(button2, prop2.get());
        DentalChartUtils.styleBleedingSVG(button3, prop3.get());

        // Unified event handler for all 3 buttons
        EventHandler<MouseEvent> bleedingClickHandler = event -> {
            if (event.getSource().equals(button1)) {
                int nextValue = cycleState(prop1.get());
                prop1.set(nextValue);
                DentalChartUtils.styleBleedingSVG(button1, nextValue);
            } else if (event.getSource().equals(button2)) {
                int nextValue = cycleState(prop2.get());
                prop2.set(nextValue);
                DentalChartUtils.styleBleedingSVG(button2, nextValue);
            } else if (event.getSource().equals(button3)) {
                int nextValue = cycleState(prop3.get());
                prop3.set(nextValue);
                DentalChartUtils.styleBleedingSVG(button3, nextValue);
            }
            // Update calculations after any button click
            MouthItem.calculateTotalBleedingOnProbing(item);
            context.updateBleedingOnProbing(false);
        };

        // Attach event handlers
        button1.setOnMouseClicked(bleedingClickHandler);
        button2.setOnMouseClicked(bleedingClickHandler);
        button3.setOnMouseClicked(bleedingClickHandler);

        // Attach property listeners for styling
        prop1.addListener((ov, oldValue, newValue) ->
            DentalChartUtils.styleBleedingSVG(button1, (int) newValue));
        prop2.addListener((ov, oldValue, newValue) ->
            DentalChartUtils.styleBleedingSVG(button2, (int) newValue));
        prop3.addListener((ov, oldValue, newValue) ->
            DentalChartUtils.styleBleedingSVG(button3, (int) newValue));
    }

    /**
     * Cycles the bleeding state from 0 → 1 → 2 → 3 → 0
     */
    private int cycleState(int currentState) {
        return (currentState == MAX_BLEEDING_STATE) ? 0 : currentState + 1;
    }
}

