package com.tribune.dental_chart.handler;

import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import com.tribune.dental_chart.entity.MouthItem;

/**
 * Handles configuration of the "Mobility" row (row index 3).
 * This row contains a dropdown to select tooth mobility level (-3 to +3).
 */
public class MobilityRowHandler implements RowHandler {

    @Override
    public void handle(Node node, MouthItem item, GridPane gridPane, RowHandlerContext context, int columnIndex) {
        ComboBox<Integer> mobility = (ComboBox<Integer>) node;

        // Populate the dropdown with values from -3 to +3
        mobility.setItems(FXCollections.observableArrayList(-3, -2, -1, 0, 1, 2, 3));

        // Listen for selection changes and update the item
        mobility.getSelectionModel().selectedItemProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue != null) {
                item.setMobility(newValue);
            }
        });

        // Select the current value from the item (with index offset)
        mobility.getSelectionModel().select(item.getMobility() > 0 ? item.getMobility() + 4 : item.getMobility() + 3);
    }
}

