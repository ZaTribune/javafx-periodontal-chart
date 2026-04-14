package com.tribune.dental_chart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;

import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.print.JobSettings;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.PrintQuality;
import javafx.print.PrintResolution;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Scale;
import javafx.stage.Popup;
import javafx.util.Pair;
import org.controlsfx.control.MaskerPane;
import com.tribune.dental_chart.entity.DentalChart;
import com.tribune.dental_chart.entity.MouthItem;
import com.tribune.dental_chart.handler.RowHandler;
import com.tribune.dental_chart.handler.RowHandlerContext;
import com.tribune.dental_chart.handler.RowHandlerFactory;

import static com.tribune.dental_chart.DentalChartUtils.styleRedCircles;
import static com.tribune.dental_chart.DentalChartUtils.drawForks;
import static com.tribune.dental_chart.DentalChartUtils.calculateDentalChartPlaque;
import static com.tribune.dental_chart.DentalChartUtils.calculateDentalChartMeanProbingDepth;
import static com.tribune.dental_chart.DentalChartUtils.calculateDentalBleedingOnProbing;
import static com.tribune.dental_chart.DentalChartUtils.drawPath;

public class DentalChartController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(DentalChartController.class);

    // item is referred to a single tooth/
    private double scale = 1.0;

    // Handler context for row configuration (used by RowHandler implementations)
    private RowHandlerContext rowHandlerContext;

    @FXML
    ScrollPane container;
    @FXML
    VBox vbox;
    @FXML
    Group parent;
    @FXML
    ScrollBar vertical;
    @FXML
    ScrollBar horizontal;
    @FXML
    private GridPane superior, inferior;
    @FXML
    private Label plaquePercentage;
    @FXML
    private Label meanProbingDepth;
    @FXML
    private Label meanAttachmentLevel;
    @FXML
    private Label bleedingOnProbing;
    //initially -> as it'll be calculated based on the availability of MouthItem
    private double currentItems = 0;
    private boolean skipUpdatingGlobalInfo = true;
    private boolean skipUpdatingBleedingOnProbing = true;
    private boolean skipUpdatingPlaque = true;

    private static final List<Integer> fork_1_Eligible = Arrays.asList(1, 2, 3, 15, 16, 17);
    private static final List<Integer> fork_2_3_Eligible = Arrays.asList(1, 2, 3, 5, 13, 15, 16, 17);

    //private Map<Integer, MouthItem> userMap = new HashMap<>();
    DentalChart chart;

    public DentalChart getChart() {
        return chart;
    }

    public void setChart(DentalChart chart) {
        this.chart = chart;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Load dental chart data - choose one:
        // Option 1: Production data with all teeth initialized to defaults
//        String s = DentalChartUtils.initializeDentalChart();
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            setChart(objectMapper.readValue(s, DentalChart.class));
//        } catch (JsonProcessingException e) {
//            logger.error("Failed to initialize dental chart from JSON", e);
//        }

        // Option 2: Dummy data from pre-existing JSON string (uncomment to use)
         DentalChart dummyChart = DentalChartUtils.getDummyDentalChart();
         setChart(dummyChart);

        Platform.runLater(() -> {
            // Initialize the handler context for all row handlers
            rowHandlerContext = new RowHandlerContext(this, superior, inferior);
            rowHandlerContext.setSkipUpdatingPlaque(skipUpdatingPlaque);
            rowHandlerContext.setSkipUpdatingGlobalInfo(skipUpdatingGlobalInfo);
            rowHandlerContext.setSkipUpdatingBleedingOnProbing(skipUpdatingBleedingOnProbing);

            initializeGridPane(superior, chart.getSuperiorMap());
            initializeGridPane(inferior, chart.getInferiorMap());

            //the KEY represents the value of overall probing depth whereas the VALUE represents the overall value of gingival margin
            updatePlaque(false);
            update_MPD_MAL(false);
            updateBleedingOnProbing(false);
            //in the end
            skipUpdatingGlobalInfo = false;
            skipUpdatingPlaque = false;
            skipUpdatingBleedingOnProbing = false;

            // Update context with final skip flags
            rowHandlerContext.setSkipUpdatingPlaque(skipUpdatingPlaque);
            rowHandlerContext.setSkipUpdatingGlobalInfo(skipUpdatingGlobalInfo);
            rowHandlerContext.setSkipUpdatingBleedingOnProbing(skipUpdatingBleedingOnProbing);
        });

    }

    void initializeGridPane(GridPane gridPane, Map<Integer, MouthItem> userMap) {
        gridPane.getChildren().forEach(cell -> {
            if (!(cell instanceof Label)) // as the superior and inferior label got a null row index value
                checkAndAssign(cell, gridPane, userMap);
        });
    }

    void checkAndAssign(Node node, GridPane gridPane, Map<Integer, MouthItem> userMap) {

        int rowIndex = GridPane.getRowIndex(node);
        int columnIndex = GridPane.getColumnIndex(node);
        MouthItem item = userMap.get(columnIndex);//this will return a MouthItem

        // just once for each item initially , later it will be based on specific field changes
        MouthItem.calculateTotalPlaque(item);
        MouthItem.calculateTotalProbingDepth(item);
        MouthItem.calculateTotalGingivalMargin(item);
        MouthItem.calculateTotalBleedingOnProbing(item);

        // Try to use a handler for this row type
        RowHandler handler = RowHandlerFactory.getHandler(rowIndex);
        if (handler != null) {
            handler.handle(node, item, gridPane, rowHandlerContext, columnIndex);
            return; // Handler successfully processed this row
        }

        // Fallback for complex rows not yet converted to handlers (row 10 - tooth image)
        switch (rowIndex) {
            case 10:
                //using extra properties for no need will cause a memory leak ... the listener will stop after few times
                //BooleanProperty crossPicProperty = new SimpleBooleanProperty(item.getAvailable());
                //BooleanProperty implantPicProperty = new SimpleBooleanProperty(item.getImplant());
                // crossPicProperty.bind(item.availableProperty());
                //implantPicProperty.bind(item.implantProperty());
                //PLUS+ don't use anonymous listeners here cause they also will be garbage collected
                VBox vbox = (VBox) node;
                if (vbox.getChildren().size() > 1) {
                    //some stack-panes contain only an ImageView like up_b and down_
                    //here We'll use already drawen red circles to adjust rendering 
                    Pane circlesContainer1 = (Pane) vbox.lookup(".circles-container1");
                    Pane circlesContainer2 = (Pane) vbox.lookup(".circles-container2");

                    Circle circle1 = (Circle) circlesContainer1.lookup(".red-circle1");
                    Circle circle2 = (Circle) circlesContainer1.lookup(".red-circle2");
                    Circle circle3 = (Circle) circlesContainer1.lookup(".red-circle3");
                    Circle circle4 = (Circle) circlesContainer2.lookup(".red-circle1");
                    Circle circle5 = (Circle) circlesContainer2.lookup(".red-circle2");
                    Circle circle6 = (Circle) circlesContainer2.lookup(".red-circle3");
                    Circle circle1B = new Circle(2);
                    circle1B.getStyleClass().add("blue-circle1");
                    Circle circle2B = new Circle(2);
                    circle2B.getStyleClass().add("blue-circle2");
                    Circle circle3B = new Circle(2);
                    circle3B.getStyleClass().add("blue-circle3");
                    Circle circle4B = new Circle(2);
                    circle4B.getStyleClass().add("blue-circle1");
                    Circle circle5B = new Circle(2);
                    circle5B.getStyleClass().add("blue-circle2");
                    Circle circle6B = new Circle(2);
                    circle6B.getStyleClass().add("blue-circle3");
                    List<Circle> circlesList1 = Arrays.asList(circle1, circle2, circle3, circle1B, circle2B, circle3B);
                    List<Circle> circlesList2 = Arrays.asList(circle4, circle5, circle6, circle4B, circle5B, circle6B);
                    ChangeListener<String> set1Listener = (ov, t, t1) -> {
                        if (t1.isEmpty() || t1.equals("-"))
                            return;//to avoid Exception in thread "JavaFX Application Thread" java.lang.NumberFormatException: For input string: "-"  or ""
                        drawPath(circlesContainer1, circlesList1, item, -1);
                    };
                    ChangeListener<String> set2Listener = (ov, t, t1) -> {
                        if (t1.isEmpty() || t1.equals("-"))
                            return;//to avoid Exception in thread "JavaFX Application Thread" java.lang.NumberFormatException: For input string: "-"  or ""
                        drawPath(circlesContainer2, circlesList2, item, 1);
                    };
                    MouthItem.connectSet1CirclesListeners(item, set1Listener);
                    MouthItem.connectSet2CirclesListeners(item, set2Listener);
                    drawPath(circlesContainer1, circlesList1, item, -1);
                    drawPath(circlesContainer2, circlesList2, item, 1);
                    styleRedCircles(item.getSelectedCircle(), circlesList1);
                    styleRedCircles(item.getSelectedCircle(), circlesList2);
                    EventHandler<MouseEvent> mcEventHandler1 = (MouseEvent t) -> {
                        Circle circle = (Circle) t.getSource();
                        //fill:white -> clicked
                        //fill:firebrick -> unclicked
                        if (circle.getStyleClass().contains("circle-nerve-clicked")) {
                            item.setSelectedCircle(-1);
                            item.setSelectedCircle(-1);
                        } else item.setSelectedCircle(circlesList1.indexOf(circle));
                        styleRedCircles(item.getSelectedCircle(), circlesList1);
                    };
                    EventHandler<MouseEvent> mcEventHandler2 = (MouseEvent t) -> {
                        Circle circle = (Circle) t.getSource();
                        if (circle.getStyleClass().contains("circle-nerve-clicked")) {
                            item.setSelectedCircle(-1);
                            item.setSelectedCircle(-1);
                        } else item.setSelectedCircle(circlesList1.indexOf(circle));
                        styleRedCircles(item.getSelectedCircle(), circlesList2);
                    };

                    circlesList1.forEach(circle -> circle.setOnMouseClicked(mcEventHandler1));
                    circlesList2.forEach(circle -> circle.setOnMouseClicked(mcEventHandler2));
                }
                StackPane containerUpPic = (StackPane) vbox.getChildren().get(0);
                StackPane containerDownPic = (StackPane) vbox.getChildren().get(2);

                ImageView pic1 = (ImageView) containerUpPic.getChildren().getFirst();
                ImageView pic2 = (ImageView) containerDownPic.getChildren().getFirst();
                String upOrDown = gridPane.getId().equals(superior.getId()) ? Images.UP : Images.DOWN;// It was superior.getId() and it took me hours to figure out the error
                ChangeListener<Boolean> avilableListener = (ov, oldValue, newValue) -> {
                    currentItems++;
                    URL resource1;
                    URL resource2;
                    if (!newValue) {
                        MouthItem.resetForks(item);
                        MouthItem.resetValues(item);
                        item.setImplant(false);
                        resource1 = Images.class.getResource(Images.FOLDER + Images.CROSS + upOrDown + columnIndex + Images.PRIMARY + Images.EXTENSION);
                        resource2 = Images.class.getResource(Images.FOLDER + Images.CROSS + upOrDown + columnIndex + Images.SECONDARY + Images.EXTENSION);
                        currentItems--;
                    } else if (item.getImplant()) {
                        MouthItem.resetForks(item);
                        resource1 = Images.class.getResource(Images.FOLDER + Images.IMPLANT + upOrDown + columnIndex + Images.PRIMARY + Images.EXTENSION);
                        resource2 = Images.class.getResource(Images.FOLDER + Images.IMPLANT + upOrDown + columnIndex + Images.SECONDARY + Images.EXTENSION);
                    } else {
                        resource1 = Images.class.getResource(Images.FOLDER + upOrDown + columnIndex + Images.PRIMARY + Images.EXTENSION);
                        resource2 = Images.class.getResource(Images.FOLDER + upOrDown + columnIndex + Images.SECONDARY + Images.EXTENSION);
                    }
                    updatePlaque(skipUpdatingPlaque);
                    update_MPD_MAL(skipUpdatingGlobalInfo);
                    updateBleedingOnProbing(skipUpdatingBleedingOnProbing);
                    Optional.ofNullable(resource1).ifPresent(res -> pic1.setImage(new Image(res.toExternalForm())));
                    Optional.ofNullable(resource2).ifPresent(res -> pic2.setImage(new Image(res.toExternalForm())));
                };

                ChangeListener<Boolean> implantListener = (ov, oldValue, newValue) -> {
                    URL resource1;
                    URL resource2;
                    if (newValue) {
                        MouthItem.resetForks(item);
                        resource1 = Images.class.getResource(Images.FOLDER + Images.IMPLANT + upOrDown + columnIndex + Images.PRIMARY + Images.EXTENSION);
                        resource2 = Images.class.getResource(Images.FOLDER + Images.IMPLANT + upOrDown + columnIndex + Images.SECONDARY + Images.EXTENSION);
                    } else {
                        resource1 = Images.class.getResource(Images.FOLDER + upOrDown + columnIndex + Images.PRIMARY + Images.EXTENSION);
                        resource2 = Images.class.getResource(Images.FOLDER + upOrDown + columnIndex + Images.SECONDARY + Images.EXTENSION);
                    }
                    Optional.ofNullable(resource1).ifPresent(res -> pic1.setImage(new Image(res.toExternalForm())));
                    Optional.ofNullable(resource2).ifPresent(res -> pic2.setImage(new Image(res.toExternalForm())));
                };
                if (fork_1_Eligible.contains(columnIndex)) {//only rows that has forks
                    ChangeListener<Number> fork1Listener = (ov, oldValue, newValue) -> drawForks(containerUpPic, newValue, 1);
                    item.fork1Property().addListener(fork1Listener);
                    fork1Listener.changed(item.fork1Property(), null, item.getFork1());
                }
                //as the inferior part doesn't have forks in item 5,13 
                List<Integer> whichOne = gridPane.getId().equals(superior.getId()) ? fork_2_3_Eligible : fork_1_Eligible;
                if (whichOne.contains(columnIndex)) {//only rows that has forks
                    int isFork2AloneOrWithFork3 = gridPane.getId().equals(superior.getId()) ? 2 : 1;
                    ChangeListener<Number> fork2Listener = (ov, oldValue, newValue) -> drawForks(containerDownPic, newValue, isFork2AloneOrWithFork3);
                    item.fork2Property().addListener(fork2Listener);
                    fork2Listener.changed(item.fork2Property(), null, item.getFork2());
                    if (gridPane.getId().equals(superior.getId())) {//as the inferior part only got fork1,fork2
                        ChangeListener<Number> fork3Listener = (ov, oldValue, newValue) -> drawForks(containerDownPic, newValue, 3);
                        fork3Listener.changed(item.fork3Property(), null, item.getFork3());
                        item.fork3Property().addListener(fork3Listener);
                    }
                }

                item.availableProperty().addListener(avilableListener);
                item.implantProperty().addListener(implantListener);
                // a hack to fire the mcListener to initialize the pic1
                avilableListener.changed(item.availableProperty(), null, item.getAvailable());
                //firing this listener will remove the affect of the previous one
                //implantListener.changed(item.implantProperty(), null, item.getImplant());
                break;

            case 16://note

                break;
        }
    }

    void setDisableOtherNodes(int ColumnIndex, GridPane gridPane, boolean disable) {
        List<Integer> rowsToDisable = Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);// 16 : note to be added
        gridPane.getChildren().forEach(child -> {
            if (!child.getClass().equals(Label.class) && GridPane.getColumnIndex(child) == ColumnIndex && rowsToDisable.contains(GridPane.getRowIndex(child))) {
                //cause some properties are bounded to some node disableProperty like forks
                if (!child.disableProperty().isBound()) child.setDisable(!disable);
            }
        });

    }

    @FXML
    public void zoomOut() {
        if (scale > 0.5 && scale <= 1.0) {
            scale = scale - 0.1;
            scale = Math.floor(scale * 100) / 100;
            vbox.setScaleX(scale);
            vbox.setScaleY(scale);
        }
    }

    @FXML
    public void zoomIn() {
        if (scale >= 0.5 && scale < 1.0) {
            scale = scale + 0.1;
            //as there could be values like 0.7999999999999999
            scale = Math.floor(scale * 100) / 100;
            vbox.setScaleX(scale);
            vbox.setScaleY(scale);
        }
    }

    @FXML
    public void print() {
        PrinterJob job = PrinterJob.createPrinterJob();
        Node header = vbox.getChildren().getFirst();
        Label label = new Label("Patient name :Muhammad Ali Arafah - " + DateTimeFormatter.ofPattern("hh:mm - dd/MM/yyyy").format(LocalDateTime.now()));
        vbox.getChildren().add(label);
        label.setStyle("-fx-text-fill:white;-fx-min-height:50;-fx-font-weight: bold;-fx-font-size:15;-fx-padding:0 0 0 20;");
        hideForPrint(superior, false);
        hideForPrint(inferior, false);
        Popup popup = new Popup();
        popup.getContent().add(new MaskerPane());
        popup.setAutoHide(false);

        Scale scale = new Scale();
        if (job != null && job.showPrintDialog(vbox.getScene().getWindow())) {
            popup.show(vbox.getScene().getWindow());
            Printer printer = job.getPrinter();
            PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.LANDSCAPE, Printer.MarginType.HARDWARE_MINIMUM);
            JobSettings settings = job.getJobSettings();
            settings.setPrintQuality(PrintQuality.HIGH);
            header.setVisible(false);
            header.setManaged(false);

            double width = vbox.getWidth();
            double height = vbox.getHeight();

            PrintResolution resolution = job.getJobSettings().getPrintResolution();

            width /= resolution.getFeedResolution();

            height /= resolution.getCrossFeedResolution();

            double scaleX = pageLayout.getPrintableWidth() / width / 600;
            double scaleY = pageLayout.getPrintableHeight() / height / 600;

            scale = new Scale(scaleX, scaleY);

            vbox.getTransforms().add(scale);

            boolean success = job.printPage(pageLayout, vbox);
            if (success) {
                job.endJob();
                //called even if canceled the "Save As" dialog
            }
        }
        vbox.getTransforms().remove(scale);
        vbox.getChildren().remove(label);
        header.setManaged(true);
        header.setVisible(true);
        hideForPrint(superior, true);
        hideForPrint(inferior, true);
        popup.hide();
    }

    public void hideForPrint(GridPane gridPane, boolean hide) {
        gridPane.getChildren().forEach(child -> {
            if (!(child instanceof Label)) {
                int row = GridPane.getRowIndex(child);
                if (row == 1 || row == 2 || row == 4 || row == 15) {
                    child.setVisible(hide);
                }
            }
        });
    }

    @FXML
    public void infoForCategory() {
        logger.info("Clicked!");
    }

    public void updatePlaque(boolean skipUpdatingPlaque) {
        if (!skipUpdatingPlaque)
            plaquePercentage.setText(String.format("%.2f", calculateDentalChartPlaque(chart) / (currentItems * 6) * 100) + "%");
    }

    public void update_MPD_MAL(boolean skipUpdatingGlobalInfo) {
        //int availableItems=totalItems-disabledItems;
        if (!skipUpdatingGlobalInfo) {
            Pair<Integer, Integer> pair = calculateDentalChartMeanProbingDepth(chart);
            meanProbingDepth.setText(String.format("%.2f", (pair.getKey() / (currentItems * 6))) + " mm");
            meanAttachmentLevel.setText(String.format("%.2f", ((pair.getKey() + pair.getValue()) / (currentItems * 6))) + " mm");
        }
    }

    public void updateBleedingOnProbing(boolean skipUpdatingBleedingOnProbing) {
        if (!skipUpdatingBleedingOnProbing)
            bleedingOnProbing.setText(String.format("%.2f", calculateDentalBleedingOnProbing(chart) / (currentItems * 6) * 100) + "%");
    }


}
