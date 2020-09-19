/**
 *
 * @author Muhammad Ali Arafah
 *    https://github.com/ZaTribune
 */
package shadow.dental_chart;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXToggleButton;
import de.jensd.fx.glyphs.weathericons.WeatherIconView;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import java.util.Arrays;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.print.JobSettings;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.PaperSource;
import javafx.print.PrintQuality;
import javafx.print.PrintResolution;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Group;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Scale;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.util.Pair;
import org.controlsfx.control.MaskerPane;
import shadow.dental_chart.entities.DentalChart;
import shadow.dental_chart.entities.MouthItem;
import static shadow.dental_chart.DentalChartUtils.styleRedCircles;
import static shadow.dental_chart.DentalChartUtils.drawForks;
import static shadow.dental_chart.DentalChartUtils.styleForkButton;
import static shadow.dental_chart.DentalChartUtils.styleBleedingSVG;
import static shadow.dental_chart.DentalChartUtils.stylePlaqueSVG;
import static shadow.dental_chart.DentalChartUtils.calculateDentalChartPlaque;
import static shadow.dental_chart.DentalChartUtils.calculateDentalChartMeanProbingDepth;
import static shadow.dental_chart.DentalChartUtils.calculateDentalBleedingOnProbing;
import static shadow.dental_chart.DentalChartUtils.drawPath;

public class DentalChartController implements Initializable {

    // item is referred to a single tooth/
    private double scale = 1.0;

    @FXML ScrollPane container;
    @FXML VBox vbox;
    @FXML Group parent;
    @FXML ScrollBar vertical;
    @FXML ScrollBar horizontal;
    @FXML private GridPane superior, inferior;
    @FXML private Label plaquePercentage;
    @FXML private Label meanProbingDepth;
    @FXML private Label meanAttachmentLevel;
    @FXML private Label bleedingOnProbing;
    //initially -> as it'll be calculated based on the availability of MouthItem
    private double currentItems = 0;
    private boolean skipUpdatingGlobalInfo = true;
    private boolean skipUpdatingBleedingOnProbing = true;
    private boolean skipUpdatingPlaque = true;

    private static final List<Integer> fork_1_Eligable = Arrays.asList(1, 2, 3, 15, 16, 17);
    private static final List<Integer> fork_2_3_Eligable = Arrays.asList(1, 2, 3, 5, 13, 15, 16, 17);

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

        String s = DentalChartUtils.initializeDentalChart();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            setChart(objectMapper.readValue(s, DentalChart.class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Platform.runLater(() -> {
            initializeGridPane(superior, chart.getSuperiorMap());
            initializeGridPane(inferior, chart.getInferiorMap());

            //the KEY represents the value of overall probing depth wheres the VALUE represents the overall value of gingival margin 
            updatePlaque(false);
            update_MPD_MAL(false);
            updateBleedingOnProbing(false);
            //in the end
            skipUpdatingGlobalInfo = false;
            skipUpdatingPlaque = false;
            skipUpdatingBleedingOnProbing = false;
        });

    }

    void initializeGridPane(GridPane gridPane, Map<Integer, MouthItem> userMap) {
        gridPane.getChildren().stream().forEach(cell -> {
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

        switch (rowIndex) {
            case 1:// available
                JFXToggleButton available = (JFXToggleButton) node;
                available.selectedProperty().addListener((ov, oldValue, newValue) -> {
                    //disable or enable all cells on this row
                    //the default for ToggleSwitch in the FXML file must be selected
                    setDisableOtherNodes(columnIndex, gridPane, newValue);
                });
                available.selectedProperty().bindBidirectional(item.availableProperty());

                break;
            case 2://implant
                JFXCheckBox implant = (JFXCheckBox) node;
                implant.selectedProperty().bindBidirectional(item.implantProperty());
                break;
            case 3://mobility
                ComboBox<Integer> mobility = (ComboBox<Integer>) node;
                mobility.setItems(FXCollections.observableArrayList(-3, -2, -1, 0, 1, 2, 3));
                mobility.getSelectionModel().selectedItemProperty().addListener((ov, oldValue, newValue) -> {
                    item.setMobility(newValue);
                });
                mobility.getSelectionModel().select(item.getMobility() > 0 ? item.getMobility() + 4 : item.getMobility() + 3);
                break;
            case 4://fork
                if (node != null) {
                    JFXButton fork1 = (JFXButton) node;
                    WeatherIconView icon = (WeatherIconView) fork1.getGraphic();
                    styleForkButton(icon, item.getFork1());
                    fork1.setOnMouseClicked(event -> {
                        int nextValue;
                        if (item.getFork1() == 3)
                            nextValue = 0;
                        else
                            nextValue = item.getFork1() + 1;
                        item.setFork1(nextValue);
                        styleForkButton(icon, nextValue);
                    });
                    //to link this button to the image drawen case the MouthItem is disabled
                    item.fork1Property().addListener((ov, oldValue, newValue) -> styleForkButton(icon, (int) newValue));
                    fork1.disableProperty().bind(item.implantProperty());
                }

                break;
            case 5://bleeding/oozing
                HBox boxBleeding1 = (HBox) node;
                JFXButton bleeding1 = (JFXButton) boxBleeding1.getChildren().get(0);
                JFXButton bleeding2 = (JFXButton) boxBleeding1.getChildren().get(1);
                JFXButton bleeding3 = (JFXButton) boxBleeding1.getChildren().get(2);
                styleBleedingSVG(bleeding1, item.getBleeding1());
                styleBleedingSVG(bleeding2, item.getBleeding2());
                styleBleedingSVG(bleeding3, item.getBleeding3());
                EventHandler<MouseEvent> mouseHandlerBleeding1 = (MouseEvent t) -> {
                    if (t.getSource().equals(bleeding1)) {
                        item.setBleeding1(item.getBleeding1() + 1);
                        styleBleedingSVG(bleeding1, item.getBleeding1());
                    } else if (t.getSource().equals(bleeding2)) {
                        item.setBleeding2(item.getBleeding2() + 1);
                        styleBleedingSVG(bleeding2, item.getBleeding2());
                    } else {
                        item.setBleeding3(item.getBleeding3() + 1);
                        styleBleedingSVG(bleeding3, item.getBleeding3());
                    }
                    MouthItem.calculateTotalBleedingOnProbing(item);
                    updateBleedingOnProbing(false);

                };
                bleeding1.setOnMouseClicked(mouseHandlerBleeding1);
                bleeding2.setOnMouseClicked(mouseHandlerBleeding1);
                bleeding3.setOnMouseClicked(mouseHandlerBleeding1);

                item.bleeding1Property().addListener((ov, oldValue, newValue) -> styleBleedingSVG(bleeding1, (int) newValue));
                item.bleeding2Property().addListener((ov, oldValue, newValue) -> styleBleedingSVG(bleeding2, (int) newValue));
                item.bleeding3Property().addListener((ov, oldValue, newValue) -> styleBleedingSVG(bleeding3, (int) newValue));
                break;
            case 6://plaque1,2,3
                HBox boxPlaque1 = (HBox) node;
                JFXButton plaque1 = (JFXButton) boxPlaque1.getChildren().get(0);
                JFXButton plaque2 = (JFXButton) boxPlaque1.getChildren().get(1);
                JFXButton plaque3 = (JFXButton) boxPlaque1.getChildren().get(2);
                stylePlaqueSVG(plaque1, item.getPlaque1());
                stylePlaqueSVG(plaque2, item.getPlaque2());
                stylePlaqueSVG(plaque3, item.getPlaque3());
                EventHandler<MouseEvent> mouseHandlerPlaque1 = (MouseEvent t) -> {
                    if (t.getSource().equals(plaque1)) {
                        item.setPlaque1(item.getPlaque1() + 1);
                        stylePlaqueSVG(plaque1, item.getPlaque1());
                    } else if (t.getSource().equals(plaque2)) {
                        item.setPlaque2(item.getPlaque2() + 1);
                        stylePlaqueSVG(plaque2, item.getPlaque2());
                    } else {
                        item.setPlaque3(item.getPlaque3() + 1);
                        stylePlaqueSVG(plaque3, item.getPlaque3());
                    }
                    MouthItem.calculateTotalPlaque(item);
                    updatePlaque(false);
                };
                plaque1.setOnMouseClicked(mouseHandlerPlaque1);
                plaque2.setOnMouseClicked(mouseHandlerPlaque1);
                plaque3.setOnMouseClicked(mouseHandlerPlaque1);
                item.plaque1Property().addListener((ov, oldValue, newValue) -> stylePlaqueSVG(plaque1, (int) newValue));
                item.plaque2Property().addListener((ov, oldValue, newValue) -> stylePlaqueSVG(plaque2, (int) newValue));
                item.plaque3Property().addListener((ov, oldValue, newValue) -> stylePlaqueSVG(plaque3, (int) newValue));
                break;
            case 7://gum-width
                JFXSlider gumWidth = (JFXSlider) node;
                gumWidth.valueChangingProperty().addListener(((o) -> {
                    item.setGum(gumWidth.getValue());
                }));
                gumWidth.setValue(item.getGum());

                break;
            case 8://gingival-margin-set-1
                ChangeListener<String> marginListener1 = (ObservableValue<? extends String> ov, String oldValue, String newValue) -> {
                    TextInputControl textField = (TextInputControl) ((StringProperty) ov).getBean();
                    if (newValue.length() == 0) {
                        textField.setText("0");
                        return;
                    }
                    if (newValue.equals("-")) {
                        return;
                    }
                    if (!newValue.matches("^[-+]?\\d{0,3}") || Integer.parseInt(textField.getText()) > 10 || Integer.parseInt(textField.getText()) < - 10) {
                        textField.setText(oldValue);
                        return;
                    }
                    MouthItem.calculateTotalGingivalMargin(item);
                    update_MPD_MAL(false);
                };
                HBox marginBox1 = (HBox) node;
                TextField margin1 = (TextField) marginBox1.getChildren().get(0);
                TextField margin2 = (TextField) marginBox1.getChildren().get(1);
                TextField margin3 = (TextField) marginBox1.getChildren().get(2);
                margin1.textProperty().bindBidirectional(item.margin1Property());
                margin2.textProperty().bindBidirectional(item.margin2Property());
                margin3.textProperty().bindBidirectional(item.margin3Property());
                margin1.textProperty().addListener(marginListener1);
                margin2.textProperty().addListener(marginListener1);
                margin3.textProperty().addListener(marginListener1);

                break;
            case 9://probing-depth-set-1
                ChangeListener<String> probingDepthListener1 = (ObservableValue<? extends String> ov, String oldValue, String newValue) -> {
                    TextInputControl textField = (TextInputControl) ((StringProperty) ov).getBean();
                    if (newValue.length() == 0) {
                        textField.setText("0");
                        return;
                    }
                    if (newValue.equals("-"))
                        return;
                    if (!newValue.matches("^[-+]?\\d{0,3}") || Integer.parseInt(textField.getText()) > 10 || Integer.parseInt(textField.getText()) < - 10) {
                        textField.setText(oldValue);
                        return;
                    }
                    MouthItem.calculateTotalProbingDepth(item);
                    update_MPD_MAL(false);
                };
                HBox depthBox1 = (HBox) node;
                TextField depth1 = (TextField) depthBox1.getChildren().get(0);
                TextField depth2 = (TextField) depthBox1.getChildren().get(1);
                TextField depth3 = (TextField) depthBox1.getChildren().get(2);
                depth1.textProperty().bindBidirectional(item.depth1Property());
                depth2.textProperty().bindBidirectional(item.depth2Property());
                depth3.textProperty().bindBidirectional(item.depth3Property());
                depth1.textProperty().addListener(probingDepthListener1);
                depth2.textProperty().addListener(probingDepthListener1);
                depth3.textProperty().addListener(probingDepthListener1);
                break;
            case 10:
                //using extra properties for no need will cause a memory leak ... the listener will stop after few times
                //BooleanProperty crossPicProperty = new SimpleBooleanProperty(item.getAvailable());
                //BooleanProperty implantPicProperty = new SimpleBooleanProperty(item.getImplant());
                // crossPicProperty.bind(item.availableProperty());
                //implantPicProperty.bind(item.implantProperty());
                //PLUS+ don't use anonymous listeners here cause they also will be garbage collected
                VBox vbox = (VBox) node;
                if (vbox.getChildren().size() > 1) {
                    //some stackpanes contain only an ImageView like up_b and down_
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
                        } else
                            item.setSelectedCircle(circlesList1.indexOf(circle));
                        styleRedCircles(item.getSelectedCircle(), circlesList1);
                    };
                    EventHandler<MouseEvent> mcEventHandler2 = (MouseEvent t) -> {
                        Circle circle = (Circle) t.getSource();
                        if (circle.getStyleClass().contains("circle-nerve-clicked")) {
                            item.setSelectedCircle(-1);
                            item.setSelectedCircle(-1);
                        } else
                            item.setSelectedCircle(circlesList1.indexOf(circle));
                        styleRedCircles(item.getSelectedCircle(), circlesList2);
                    };

                    circlesList1.forEach(circle -> circle.setOnMouseClicked(mcEventHandler1));
                    circlesList2.forEach(circle -> circle.setOnMouseClicked(mcEventHandler2));
                }
                StackPane containerUpPic = (StackPane) vbox.getChildren().get(0);
                StackPane containerDownPic = (StackPane) vbox.getChildren().get(2);

                ImageView pic1 = (ImageView) containerUpPic.getChildren().get(0);
                ImageView pic2 = (ImageView) containerDownPic.getChildren().get(0);
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

                    pic1.setImage(new Image(resource1.toExternalForm()));
                    pic2.setImage(new Image(resource2.toExternalForm()));
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
                    pic1.setImage(new Image(resource1.toExternalForm()));
                    pic2.setImage(new Image(resource2.toExternalForm()));
                };
                if (fork_1_Eligable.contains(columnIndex)) {//only rows that has forks
                    ChangeListener<Number> fork1Listener = (ov, oldValue, newValue) -> drawForks(containerUpPic, newValue, 1);
                    item.fork1Property().addListener(fork1Listener);
                    fork1Listener.changed(item.fork1Property(), null, item.getFork1());
                }
                //as the inferior part doesn't have forks in item 5,13 
                List<Integer> whichOne = gridPane.getId().equals(superior.getId()) ? fork_2_3_Eligable : fork_1_Eligable;
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

            case 11://probing-depth-set2
                ChangeListener<String> probingDepthListener2 = (ObservableValue<? extends String> ov, String oldValue, String newValue) -> {
                    TextInputControl textField = (TextInputControl) ((StringProperty) ov).getBean();
                    if (newValue.length() == 0) {
                        textField.setText("0");//to avoid the number format exception
                        return;
                    }
                    if (newValue.equals("-")) {
                        return;
                    }
                    if (!newValue.matches("^[-+]?\\d{0,3}") || Integer.parseInt(textField.getText()) > 10 || Integer.parseInt(textField.getText()) < - 10) {
                        textField.setText(oldValue);///the whole value not just a single number input 
                        return;
                    }

                    MouthItem.calculateTotalProbingDepth(item);
                    update_MPD_MAL(false);
                };
                HBox depthBox2 = (HBox) node;
                TextField depth4 = (TextField) depthBox2.getChildren().get(0);
                TextField depth5 = (TextField) depthBox2.getChildren().get(1);
                TextField depth6 = (TextField) depthBox2.getChildren().get(2);
                depth4.textProperty().bindBidirectional(item.depth4Property());
                depth5.textProperty().bindBidirectional(item.depth5Property());
                depth6.textProperty().bindBidirectional(item.depth6Property());
                depth4.textProperty().addListener(probingDepthListener2);
                depth5.textProperty().addListener(probingDepthListener2);
                depth6.textProperty().addListener(probingDepthListener2);
                break;
            case 12://gingival-margin-set-2
                ChangeListener<String> marginListener2 = (ObservableValue<? extends String> ov, String oldValue, String newValue) -> {
                    TextInputControl textField = (TextInputControl) ((StringProperty) ov).getBean();
                    if (newValue.length() == 0) {
                        textField.setText("0");//to avoid the number format exception
                        return;
                    }
                    if (newValue.equals("-"))
                        return;
                    if (!newValue.matches("^[-+]?\\d{0,3}") || Integer.parseInt(textField.getText()) > 10 || Integer.parseInt(textField.getText()) < - 10) {
                        textField.setText(oldValue);///the whole value not just a single number input 
                        return;
                    }
                    MouthItem.calculateTotalGingivalMargin(item);
                    update_MPD_MAL(false);
                };
                HBox marginBox2 = (HBox) node;
                TextField margin4 = (TextField) marginBox2.getChildren().get(0);
                TextField margin5 = (TextField) marginBox2.getChildren().get(1);
                TextField margin6 = (TextField) marginBox2.getChildren().get(2);
                margin4.textProperty().bindBidirectional(item.margin4Property());
                margin5.textProperty().bindBidirectional(item.margin5Property());
                margin6.textProperty().bindBidirectional(item.margin6Property());
                margin4.textProperty().addListener(marginListener2);
                margin5.textProperty().addListener(marginListener2);
                margin6.textProperty().addListener(marginListener2);

                break;
            case 13://plaque 4,5,6
                HBox boxPlaque2 = (HBox) node;
                JFXButton plaque4 = (JFXButton) boxPlaque2.getChildren().get(0);
                JFXButton plaque5 = (JFXButton) boxPlaque2.getChildren().get(1);
                JFXButton plaque6 = (JFXButton) boxPlaque2.getChildren().get(2);
                stylePlaqueSVG(plaque4, item.getPlaque4());
                stylePlaqueSVG(plaque5, item.getPlaque5());
                stylePlaqueSVG(plaque6, item.getPlaque6());
                EventHandler<MouseEvent> mouseHandlerPlaque2 = (MouseEvent t) -> {
                    if (t.getSource().equals(plaque4)) {
                        item.setPlaque4(item.getPlaque4() + 1);
                        stylePlaqueSVG(plaque4, item.getPlaque4());
                    } else if (t.getSource().equals(plaque5)) {
                        item.setPlaque5(item.getPlaque5() + 1);
                        stylePlaqueSVG(plaque5, item.getPlaque5());
                    } else {
                        item.setPlaque6(item.getPlaque6() + 1);
                        stylePlaqueSVG(plaque6, item.getPlaque6());
                    }
                    MouthItem.calculateTotalPlaque(item);
                    updatePlaque(false);
                };
                plaque4.setOnMouseClicked(mouseHandlerPlaque2);
                plaque5.setOnMouseClicked(mouseHandlerPlaque2);
                plaque6.setOnMouseClicked(mouseHandlerPlaque2);

                item.plaque4Property().addListener((ov, oldValue, newValue) -> stylePlaqueSVG(plaque4, (int) newValue));
                item.plaque5Property().addListener((ov, oldValue, newValue) -> stylePlaqueSVG(plaque5, (int) newValue));
                item.plaque6Property().addListener((ov, oldValue, newValue) -> stylePlaqueSVG(plaque6, (int) newValue));

                break;
            case 14://bleeding/oozing
                HBox boxBleeding2 = (HBox) node;
                JFXButton bleeding4 = (JFXButton) boxBleeding2.getChildren().get(0);

                JFXButton bleeding5 = (JFXButton) boxBleeding2.getChildren().get(1);
                JFXButton bleeding6 = (JFXButton) boxBleeding2.getChildren().get(2);
                styleBleedingSVG(bleeding4, item.getBleeding4());
                styleBleedingSVG(bleeding5, item.getBleeding5());
                styleBleedingSVG(bleeding6, item.getBleeding6());
                EventHandler<MouseEvent> mouseHandlerBleeding2 = (MouseEvent t) -> {
                    if (t.getSource().equals(bleeding4)) {
                        item.setBleeding4(item.getBleeding4() + 1);
                        styleBleedingSVG(bleeding4, item.getBleeding4());
                    } else if (t.getSource().equals(bleeding5)) {
                        item.setBleeding5(item.getBleeding5() + 1);
                        styleBleedingSVG(bleeding5, item.getBleeding5());
                    } else {
                        item.setBleeding6(item.getBleeding6() + 1);
                        styleBleedingSVG(bleeding6, item.getBleeding6());
                    }
                    MouthItem.calculateTotalBleedingOnProbing(item);
                    updateBleedingOnProbing(false);
                };
                bleeding4.setOnMouseClicked(mouseHandlerBleeding2);
                bleeding5.setOnMouseClicked(mouseHandlerBleeding2);
                bleeding6.setOnMouseClicked(mouseHandlerBleeding2);
                item.bleeding4Property().addListener((ov, oldValue, newValue) -> styleBleedingSVG(bleeding4, (int) newValue));
                item.bleeding5Property().addListener((ov, oldValue, newValue) -> styleBleedingSVG(bleeding5, (int) newValue));
                item.bleeding6Property().addListener((ov, oldValue, newValue) -> styleBleedingSVG(bleeding6, (int) newValue));

                break;
            case 15://fork
                if (node != null) {
                    HBox box = (HBox) node;
                    JFXButton fork2 = (JFXButton) box.getChildren().get(0);
                    WeatherIconView fork2_icon = (WeatherIconView) fork2.getGraphic();
                    styleForkButton(fork2_icon, item.getFork2());
                    fork2.setOnMouseClicked(event -> {
                        int nextValue;
                        if (item.getFork2() == 3)
                            nextValue = 0;
                        else
                            nextValue = item.getFork2() + 1;
                        item.setFork2(nextValue);
                        styleForkButton(fork2_icon, nextValue);
                    });
                    //to link this button to the image drawen case the MouthItem is disabled
                    item.fork2Property().addListener((ov, oldValue, newValue) -> styleForkButton(fork2_icon, (int) newValue));
                    fork2.disableProperty().bind(item.implantProperty());
                    //only the superior part got 3 forks
                    if (gridPane.getId().equals(superior.getId())) {
                        JFXButton fork3 = (JFXButton) box.getChildren().get(1);
                        WeatherIconView fork3_icon = (WeatherIconView) fork3.getGraphic();
                        styleForkButton(fork3_icon, item.getFork3());
                        fork3.setOnMouseClicked(event -> {
                            int nextValue;
                            if (item.getFork3() == 3)
                                nextValue = 0;
                            else
                                nextValue = item.getFork3() + 1;
                            item.setFork3(nextValue);
                            styleForkButton(fork3_icon, nextValue);
                        });
                        item.fork3Property().addListener((ov, oldValue, newValue) -> styleForkButton(fork3_icon, (int) newValue));
                        fork3.disableProperty().bind(item.implantProperty());
                    }

                }
                break;
            case 16://note

                break;
        }
    }

    void setDisableOtherNodes(int ColumnIndex, GridPane gridPane, boolean disable) {
        List<Integer> rowsToDisable = Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);// 16 : note to be added
        gridPane.getChildren().stream().forEach(child -> {
            if (!child.getClass().equals(Label.class) && GridPane.getColumnIndex(child) == ColumnIndex && rowsToDisable.contains(GridPane.getRowIndex(child))) {
                //cause some properties are bounded to some node disableProperty like forks
                if (!child.disableProperty().isBound())
                    child.setDisable(!disable);
            }
        });

    }

    public void zoomOut(ActionEvent event) {
        if (scale > 0.5 && scale <= 1.0) {
            scale = scale - 0.1;
            scale = Math.floor(scale * 100) / 100;
            vbox.setScaleX(scale);
            vbox.setScaleY(scale);
        }
    }

    public void zoomIn(ActionEvent event) {
        if (scale >= 0.5 && scale < 1.0) {
            scale = scale + 0.1;
            //as there could be values like 0.7999999999999999
            scale = Math.floor(scale * 100) / 100;
            vbox.setScaleX(scale);
            vbox.setScaleY(scale);
        }
    }

    public void print(ActionEvent event) {
        PrinterJob job = PrinterJob.createPrinterJob();
        Node header = vbox.getChildren().get(0);
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
                //called even if cancelled the "Save As" dialog
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
        gridPane.getChildren().stream().forEach(child -> {
            if (!(child instanceof Label)) {
                int row = GridPane.getRowIndex(child);
                if (row == 1 || row == 2 || row == 4 || row == 15) {
                    child.setVisible(hide);
                }
            }
        });
    }

    public void infoForCategory(MouseEvent event) {
        System.out.println("clicked");

    }

    private void updatePlaque(boolean skipUpdatingPlaque) {
        if (!skipUpdatingPlaque)
            plaquePercentage.setText(String.format("%.2f", calculateDentalChartPlaque(chart) / (currentItems * 6) * 100) + "%");
    }

    private void update_MPD_MAL(boolean skipUpdatingGlobalInfo) {
        //int availableItems=totalItems-disabledItems;
        if (!skipUpdatingGlobalInfo) {
            Pair<Integer, Integer> pair = calculateDentalChartMeanProbingDepth(chart);
            meanProbingDepth.setText(String.format("%.2f", (pair.getKey() / (currentItems * 6))) + " mm");
            meanAttachmentLevel.setText(String.format("%.2f", ((pair.getKey() + pair.getValue()) / (currentItems * 6))) + " mm");
        }
    }

    private void updateBleedingOnProbing(boolean skipUpdatingBleedingOnProbing) {
        if (!skipUpdatingBleedingOnProbing)
            bleedingOnProbing.setText(String.format("%.2f", calculateDentalBleedingOnProbing(chart) / (currentItems * 6) * 100) + "%");
    }

}
