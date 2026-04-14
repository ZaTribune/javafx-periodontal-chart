package com.tribune.dental_chart.handler;

import javafx.scene.layout.GridPane;
import com.tribune.dental_chart.DentalChartController;
import java.util.Arrays;
import java.util.List;

/**
 * Context object that holds shared state and callbacks needed by row handlers.
 * This eliminates the need to pass the controller directly to every handler,
 * following the Facade pattern for cleaner dependency management.
 */
public class RowHandlerContext {
    private final DentalChartController controller;
    private final GridPane superior;
    private final GridPane inferior;

    // Fork eligibility lists - defines which teeth can show fork symbols
    private static final List<Integer> fork_1_Eligible = Arrays.asList(1, 2, 3, 15, 16, 17);
    private static final List<Integer> fork_2_3_Eligible = Arrays.asList(1, 2, 3, 5, 13, 15, 16, 17);

    // Mutable state shared across handlers
    private double currentItems = 0;
    private boolean skipUpdatingPlaque = true;
    private boolean skipUpdatingGlobalInfo = true;
    private boolean skipUpdatingBleedingOnProbing = true;

    public RowHandlerContext(DentalChartController controller, GridPane superior, GridPane inferior) {
        this.controller = controller;
        this.superior = superior;
        this.inferior = inferior;
    }

    // ==================== Accessors ====================

    public DentalChartController getController() {
        return controller;
    }

    public GridPane getSuperior() {
        return superior;
    }

    public GridPane getInferior() {
        return inferior;
    }

    public double getCurrentItems() {
        return currentItems;
    }

    public void setCurrentItems(double currentItems) {
        this.currentItems = currentItems;
    }

    public void incrementCurrentItems() {
        this.currentItems++;
    }

    public void decrementCurrentItems() {
        this.currentItems--;
    }

    public boolean isSkipUpdatingPlaque() {
        return skipUpdatingPlaque;
    }

    public void setSkipUpdatingPlaque(boolean skip) {
        this.skipUpdatingPlaque = skip;
    }

    public boolean isSkipUpdatingGlobalInfo() {
        return skipUpdatingGlobalInfo;
    }

    public void setSkipUpdatingGlobalInfo(boolean skip) {
        this.skipUpdatingGlobalInfo = skip;
    }

    public boolean isSkipUpdatingBleedingOnProbing() {
        return skipUpdatingBleedingOnProbing;
    }

    public void setSkipUpdatingBleedingOnProbing(boolean skip) {
        this.skipUpdatingBleedingOnProbing = skip;
    }

    // ==================== Callback Methods ====================

    /**
     * Updates the plaque percentage display.
     *
     * @param skipUpdate if true, skips the update (used during initialization)
     */
    public void updatePlaque(boolean skipUpdate) {
        controller.updatePlaque(skipUpdate);
    }

    /**
     * Updates the mean probing depth and mean attachment level.
     *
     * @param skipUpdate if true, skips the update
     */
    public void updateMeanProbingDepthAndMAL(boolean skipUpdate) {
        controller.update_MPD_MAL(skipUpdate);
    }

    /**
     * Updates the bleeding on probing display.
     *
     * @param skipUpdate if true, skips the update
     */
    public void updateBleedingOnProbing(boolean skipUpdate) {
        controller.updateBleedingOnProbing(skipUpdate);
    }

    // ==================== Fork Eligibility Lists ====================

    /**
     * Gets the list of teeth eligible for fork 1 symbol.
     *
     * @return list of tooth column indices (1-17) that can show fork 1
     */
    public static List<Integer> getFork1EligibleList() {
        return fork_1_Eligible;
    }

    /**
     * Gets the list of teeth eligible for fork 2/3 symbols.
     *
     * @return list of tooth column indices (1-17) that can show fork 2/3
     */
    public static List<Integer> getFork2_3EligibleList() {
        return fork_2_3_Eligible;
    }

    /**
     * Checks if a tooth can display fork 1 symbol.
     *
     * @param toothNumber the tooth column index
     * @return true if fork 1 can be displayed for this tooth
     */
    public static boolean isFork1Eligible(int toothNumber) {
        return fork_1_Eligible.contains(toothNumber);
    }

    /**
     * Checks if a tooth can display fork 2/3 symbols.
     *
     * @param toothNumber the tooth column index
     * @return true if fork 2/3 can be displayed for this tooth
     */
    public static boolean isFork2_3Eligible(int toothNumber) {
        return fork_2_3_Eligible.contains(toothNumber);
    }
}

