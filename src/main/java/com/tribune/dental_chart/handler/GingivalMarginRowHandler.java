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
 * RowHandler for Gingival Margin text fields (rows 8 and 12).
 * Manages two sets of 3 margin input fields each (set 1: margin1-3, set 2: margin4-6).
 * Handles validation, bidirectional binding, and updates mean margin calculations.
 */
public class GingivalMarginRowHandler implements RowHandler {
    private static final String MINUS_SIGN = "-";

    /**
     * Configures gingival margin text fields for a specific tooth.
     *
     * @param node       The UI node (HBox containing 3 margin text fields)
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

        HBox marginBox = (HBox) node;
        if (marginBox.getChildren().size() < 3) {
            return; // Ensure we have 3 text fields
        }

        TextField field1 = (TextField) marginBox.getChildren().get(0);
        TextField field2 = (TextField) marginBox.getChildren().get(1);
        TextField field3 = (TextField) marginBox.getChildren().get(2);

        int rowIndex = GridPane.getRowIndex(node);

        if (rowIndex == 8) {
            // Gingival Margin Set 1 (margin 1, 2, 3)
            configureMarginSet(field1, field2, field3, item, context,
                    item.margin1Property(), item.margin2Property(), item.margin3Property());
        } else if (rowIndex == 12) {
            // Gingival Margin Set 2 (margin 4, 5, 6)
            configureMarginSet(field1, field2, field3, item, context,
                    item.margin4Property(), item.margin5Property(), item.margin6Property());
        }
    }

    /**
     * Configures a set of 3 margin text fields with their respective properties.
     */
    private void configureMarginSet(TextField field1, TextField field2, TextField field3,
                                    MouthItem item, RowHandlerContext context,
                                    StringProperty prop1, StringProperty prop2, StringProperty prop3) {

        // Create change listener for validation
        ChangeListener<String> marginListener = createMarginListener(item, context);

        // Bind properties
        field1.textProperty().bindBidirectional(prop1);
        field2.textProperty().bindBidirectional(prop2);
        field3.textProperty().bindBidirectional(prop3);

        // Add validation listeners
        field1.textProperty().addListener(marginListener);
        field2.textProperty().addListener(marginListener);
        field3.textProperty().addListener(marginListener);
    }

    /**
     * Creates a change listener that validates margin input.
     */
    private ChangeListener<String> createMarginListener(MouthItem item, RowHandlerContext context) {
        return (ObservableValue<? extends String> ov, String oldValue, String newValue) -> {
            TextInputControl textField = (TextInputControl) ((StringProperty) ov).getBean();

            // Handle empty input
            if (newValue.isEmpty()) {
                textField.setText("0");
                return;
            }

            // Allow minus sign for in-progress input
            if (NumericFieldValidator.MINUS_SIGN.equals(newValue)) {
                return;
            }

            // Validate using NumericFieldValidator
            if (!NumericFieldValidator.isValid(newValue)) {
                textField.setText(oldValue);
                return;
            }

            // Update calculations after valid input
            MouthItem.calculateTotalGingivalMargin(item);
            context.updateMeanProbingDepthAndMAL(false);
        };
    }
}

