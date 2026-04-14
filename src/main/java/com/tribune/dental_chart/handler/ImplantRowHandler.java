package com.tribune.dental_chart.handler;

import com.jfoenix.controls.JFXCheckBox;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import com.tribune.dental_chart.entity.MouthItem;

/**
 * Handles configuration of the "Implant" row (row index 2).
 * This row contains a checkbox to mark a tooth as an implant.
 */
public class ImplantRowHandler implements RowHandler {

    @Override
    public void handle(Node node, MouthItem item, GridPane gridPane, RowHandlerContext context, int columnIndex) {
        JFXCheckBox implant = (JFXCheckBox) node;
        implant.selectedProperty().bindBidirectional(item.implantProperty());
    }
}

