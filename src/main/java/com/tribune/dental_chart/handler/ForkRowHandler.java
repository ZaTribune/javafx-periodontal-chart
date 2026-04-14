package com.tribune.dental_chart.handler;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.weathericons.WeatherIconView;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import com.tribune.dental_chart.DentalChartUtils;
import com.tribune.dental_chart.entity.MouthItem;

/**
 * RowHandler for Fork symbols (rows 4 and 15).
 * Row 4: Manages fork1 button (single)
 * Row 15: Manages fork2 button (always) and fork3 button (superior only)
 * Handles cycling through fork states (0→1→2→3→0) and styling.
 */
public class ForkRowHandler implements RowHandler {
    private static final int MAX_FORK_STATE = 3;

    /**
     * Configures the fork button(s) for a specific tooth.
     *
     * @param node       The UI node (JFXButton or HBox containing fork button(s))
     * @param item       The MouthItem associated with this tooth
     * @param gridPane   The GridPane containing the node
     * @param context    The context with eligibility lists and methods
     * @param columnIndex The column index (tooth number 1-17)
     */
    @Override
    public void handle(Node node, MouthItem item, GridPane gridPane, RowHandlerContext context, int columnIndex) {
        if (node == null) {
            return;
        }

        int rowIndex = GridPane.getRowIndex(node);

        if (rowIndex == 4) {
            // Row 4: Single fork1 button
            if (node instanceof JFXButton forkButton) {
                handleFork1(forkButton, item);
            }
        } else if (rowIndex == 15) {
            // Row 15: fork2 and optionally fork3 (superior only)
            if (node instanceof HBox box) {
                handleFork2And3(box, item, gridPane, context);
            }
        }
    }

    /**
     * Handles fork1 button (row 4).
     */
    private void handleFork1(JFXButton forkButton, MouthItem item) {
        WeatherIconView icon = (WeatherIconView) forkButton.getGraphic();

        // Initial styling based on current fork state
        DentalChartUtils.styleForkButton(icon, item.getFork1());

        // Click handler: cycle through fork states 0 → 1 → 2 → 3 → 0
        forkButton.setOnMouseClicked(event -> {
            int nextValue = (item.getFork1() == MAX_FORK_STATE) ? 0 : item.getFork1() + 1;
            item.setFork1(nextValue);
            DentalChartUtils.styleForkButton(icon, nextValue);
        });

        // Property listener: update styling when fork state changes
        item.fork1Property().addListener((ov, oldValue, newValue) ->
            DentalChartUtils.styleForkButton(icon, (int) newValue)
        );

        // Disable fork button when tooth is marked as implant
        forkButton.disableProperty().bind(item.implantProperty());
    }

    /**
     * Handles fork2 and fork3 buttons (row 15).
     */
    private void handleFork2And3(HBox box, MouthItem item, GridPane gridPane, RowHandlerContext context) {
        if (box.getChildren().isEmpty()) {
            return;
        }

        // Fork2 button (always present)
        JFXButton fork2Button = (JFXButton) box.getChildren().getFirst();
        WeatherIconView fork2Icon = (WeatherIconView) fork2Button.getGraphic();

        DentalChartUtils.styleForkButton(fork2Icon, item.getFork2());

        fork2Button.setOnMouseClicked(event -> {
            int nextValue = (item.getFork2() == MAX_FORK_STATE) ? 0 : item.getFork2() + 1;
            item.setFork2(nextValue);
            DentalChartUtils.styleForkButton(fork2Icon, nextValue);
        });

        item.fork2Property().addListener((ov, oldValue, newValue) ->
            DentalChartUtils.styleForkButton(fork2Icon, (int) newValue)
        );

        fork2Button.disableProperty().bind(item.implantProperty());

        // Fork3 button (only in superior grid, second button in HBox)
        if (box.getChildren().size() > 1 && gridPane.getId().equals(context.getSuperior().getId())) {
            JFXButton fork3Button = (JFXButton) box.getChildren().get(1);
            WeatherIconView fork3Icon = (WeatherIconView) fork3Button.getGraphic();

            DentalChartUtils.styleForkButton(fork3Icon, item.getFork3());

            fork3Button.setOnMouseClicked(event -> {
                int nextValue = (item.getFork3() == MAX_FORK_STATE) ? 0 : item.getFork3() + 1;
                item.setFork3(nextValue);
                DentalChartUtils.styleForkButton(fork3Icon, nextValue);
            });

            item.fork3Property().addListener((ov, oldValue, newValue) ->
                DentalChartUtils.styleForkButton(fork3Icon, (int) newValue)
            );

            fork3Button.disableProperty().bind(item.implantProperty());
        }
    }
}

