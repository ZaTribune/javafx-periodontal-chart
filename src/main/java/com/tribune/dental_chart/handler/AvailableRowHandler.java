package com.tribune.dental_chart.handler;

import com.jfoenix.controls.JFXToggleButton;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import com.tribune.dental_chart.entity.MouthItem;
import java.util.Arrays;
import java.util.List;

/**
 * Handles configuration of the "Available" row (row index 1).
 * This row contains a toggle button that enables/disables all cells in the row.
 */
public class AvailableRowHandler implements RowHandler {

    @Override
    public void handle(Node node, MouthItem item, GridPane gridPane, RowHandlerContext context, int columnIndex) {
        JFXToggleButton available = (JFXToggleButton) node;

        // Listen for toggle changes to enable/disable other cells
        available.selectedProperty().addListener((ov, oldValue, newValue)
                -> setDisableOtherNodes(columnIndex, gridPane, newValue));

        // Bind the toggle button state to the item's available property
        available.selectedProperty().bindBidirectional(item.availableProperty());
    }

    /**
     * Disables or enables all other nodes in the row based on availability.
     * This prevents data entry for unavailable teeth.
     *
     * @param columnIndex The tooth column index
     * @param gridPane    The grid pane containing the nodes
     * @param disable     True to disable, false to enable other nodes
     */
    private void setDisableOtherNodes(int columnIndex, GridPane gridPane, boolean disable) {
        List<Integer> rowsToDisable = Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
        gridPane.getChildren().forEach(child -> {
            Integer rowIndex = GridPane.getRowIndex(child);
            Integer colIndex = GridPane.getColumnIndex(child);
            if (!child.getClass().equals(Label.class) &&
                colIndex != null && colIndex == columnIndex &&
                rowIndex != null && rowsToDisable.contains(rowIndex)) {
                // Some properties are bounded to disable property (like forks), skip those
                if (!child.disableProperty().isBound()) {
                    child.setDisable(!disable);
                }
            }
        });
    }
}

