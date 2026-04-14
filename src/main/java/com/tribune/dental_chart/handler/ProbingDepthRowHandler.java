package com.tribune.dental_chart.handler;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import com.tribune.dental_chart.entity.MouthItem;
import com.tribune.dental_chart.validator.NumericFieldValidator;

/**
 * RowHandler for Probing Depth text fields (rows 9 and 11).
 * Manages two sets of 3 probing depth input fields each (set 1: depth1-3, set 2: depth4-6).
 * Handles validation, bidirectional binding, and updates mean probing depth calculations.
 */
public class ProbingDepthRowHandler implements RowHandler {
    private static final String MINUS_SIGN = "-";

    /**
     * Configures probing depth text fields for a specific tooth.
     *
     * @param node       The UI node (HBox containing 3 depth text fields)
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

        HBox depthBox = (HBox) node;
        if (depthBox.getChildren().size() < 3) {
            return; // Ensure we have 3 text fields
        }

        TextField field1 = (TextField) depthBox.getChildren().get(0);
        TextField field2 = (TextField) depthBox.getChildren().get(1);
        TextField field3 = (TextField) depthBox.getChildren().get(2);

        int rowIndex = GridPane.getRowIndex(node);

        if (rowIndex == 9) {
            // Probing Depth Set 1 (depth 1, 2, 3)
            configureDepthSet(field1, field2, field3, item, context,
                    item.depth1Property(), item.depth2Property(), item.depth3Property());
        } else if (rowIndex == 11) {
            // Probing Depth Set 2 (depth 4, 5, 6)
            configureDepthSet(field1, field2, field3, item, context,
                    item.depth4Property(), item.depth5Property(), item.depth6Property());
        }
    }

    /**
     * Configures a set of 3 depth text fields with their respective properties.
     */
    private void configureDepthSet(TextField field1, TextField field2, TextField field3,
                                   MouthItem item, RowHandlerContext context,
                                   StringProperty prop1, StringProperty prop2, StringProperty prop3) {

        // Create change listener for validation
        ChangeListener<String> depthListener = createDepthListener(item, context);

        // Bind properties
        field1.textProperty().bindBidirectional(prop1);
        field2.textProperty().bindBidirectional(prop2);
        field3.textProperty().bindBidirectional(prop3);

        // Add validation listeners
        field1.textProperty().addListener(depthListener);
        field2.textProperty().addListener(depthListener);
        field3.textProperty().addListener(depthListener);
    }

    /**
     * Creates a change listener that validates probing depth input.
     */
    private ChangeListener<String> createDepthListener(MouthItem item, RowHandlerContext context) {
        return (ObservableValue<? extends String> ov, String oldValue, String newValue) -> {
            TextInputControl textField = (TextInputControl) ((StringProperty) ov).getBean();

            // Handle empty input
            if (newValue.isEmpty()) {
                textField.setText("0");
                return;
            }

            // Allow minus sign for in-progress input
            if (MINUS_SIGN.equals(newValue)) {
                return;
            }

            // Validate using NumericFieldValidator
            if (!NumericFieldValidator.isValid(newValue)) {
                textField.setText(oldValue);
                return;
            }

            // Update calculations after valid input
            MouthItem.calculateTotalProbingDepth(item);
            context.updateMeanProbingDepthAndMAL(false);
        };
    }
}

