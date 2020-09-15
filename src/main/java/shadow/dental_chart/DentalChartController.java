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
import com.jfoenix.controls.JFXToggleButton;
import de.jensd.fx.glyphs.weathericons.WeatherIconView;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import java.util.Arrays;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import org.controlsfx.control.ToggleSwitch;
import shadow.dental_chart.entities.DentalChart;
import shadow.dental_chart.entities.MouthItem;
import static shadow.dental_chart.DentalChartUtils.styleRedCircles;
import static shadow.dental_chart.DentalChartUtils.drawForks;
import static shadow.dental_chart.DentalChartUtils.styleForkButton;
import static shadow.dental_chart.DentalChartUtils.styleBleedingSVG;
import static shadow.dental_chart.DentalChartUtils.stylePlaqueSVG;

public class DentalChartController implements Initializable {

    // item is referred to a single tooth/
    
    private double scale = 1.0;

    @FXML ScrollPane container;
    @FXML VBox vbox;
    @FXML Group parent;
    @FXML ScrollBar vertical;
    @FXML ScrollBar horizontal;
    @FXML private GridPane superior, inferior;
    private double zoomMoveX;
    private double zoomMoveY;
    

    private static final List<Integer> fork_1_Eligable = Arrays.asList(1, 2, 3, 15, 16, 17);
    private static final List<Integer> fork_2_3_Eligable = Arrays.asList(1, 2, 3, 5, 13, 15, 16, 17);
    private Integer COMUMN;

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

        });
        
                    
            vbox.boundsInParentProperty().addListener(new ChangeListener<Bounds>(){
                @Override
                public void changed(ObservableValue<? extends Bounds> ov, Bounds oldValue, Bounds newValue) {
                   zoomMoveX=newValue.getMinX();
                    zoomMoveY=-newValue.getMinY();
                }});
       

    }

    void initializeGridPane(GridPane gridPane, Map<Integer, MouthItem> userMap) {
        gridPane.getChildren().stream().forEach(cell -> {
            ObservableList<String> styles = cell.getStyleClass();
            if (!cell.getClass().equals(Label.class)) {// as the superior and inferior label got a null row index value
                checkAndAssign(cell, gridPane, userMap);
                //System.out.println("" + GridPane.getRowIndex(cell) + "\t" + cell);
            }

        });
    }

    void checkAndAssign(Node node, GridPane gridPane, Map<Integer, MouthItem> userMap) {

        int rowIndex = GridPane.getRowIndex(node);
        int columnIndex = GridPane.getColumnIndex(node);
        MouthItem item = userMap.get(columnIndex);//this will return a MouthItem
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
                };
                plaque1.setOnMouseClicked(mouseHandlerPlaque1);
                plaque2.setOnMouseClicked(mouseHandlerPlaque1);
                plaque3.setOnMouseClicked(mouseHandlerPlaque1);

                item.plaque1Property().addListener((ov, oldValue, newValue) -> stylePlaqueSVG(plaque1, (int) newValue));
                item.plaque2Property().addListener((ov, oldValue, newValue) -> stylePlaqueSVG(plaque2, (int) newValue));
                item.plaque3Property().addListener((ov, oldValue, newValue) -> stylePlaqueSVG(plaque3, (int) newValue));

                break;

            case 7://gum-width

                break;
            case 8://gingival-margin

                break;
            case 9://probing-depth

                break;
            case 10:
            case 12:

                //using extra properties for no need will cause a memory leak ... the listener will stop after few times
                //BooleanProperty crossPicProperty = new SimpleBooleanProperty(item.getAvailable());
                //BooleanProperty implantPicProperty = new SimpleBooleanProperty(item.getImplant());
                // crossPicProperty.bind(item.availableProperty());
                //implantPicProperty.bind(item.implantProperty());
                //PLUS+ don't use anonymous listeners here cause they also will be garbage collected
                StackPane pane = (StackPane) node;
                if (pane.getChildren().size() > 1) {
                    //some stackpanes contain only an ImageView like up_b and down_
                    Circle nerve1 = (Circle) pane.lookup(".circle-nerve1");
                    Circle nerve2 = (Circle) pane.lookup(".circle-nerve2");
                    Circle nerve3 = (Circle) pane.lookup(".circle-nerve3");
                    styleRedCircles(nerve1, item.getNerve1());
                    styleRedCircles(nerve2, item.getNerve2());
                    styleRedCircles(nerve3, item.getNerve3());

                    EventHandler<MouseEvent> mcEventHandler = new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent t) {
                            Circle circle = (Circle) t.getSource();
                            //fill:white -> clicked
                            //fill:firebrick -> unclicked
                            if (circle == nerve1) {
                                item.setNerve1(!item.getNerve1());
                                styleRedCircles(nerve1, item.getNerve1());
                            } else if (circle == nerve2) {
                                item.setNerve2(!item.getNerve2());
                                styleRedCircles(nerve2, item.getNerve2());
                            } else {
                                item.setNerve3(!item.getNerve3());
                                styleRedCircles(nerve3, item.getNerve3());
                            }

                        }
                    };
                    nerve1.setOnMouseClicked(mcEventHandler);
                    nerve2.setOnMouseClicked(mcEventHandler);
                    nerve3.setOnMouseClicked(mcEventHandler);
                }

                ImageView pic = (ImageView) pane.getChildren().get(0);
                String upOrDown = gridPane.getId().equals(superior.getId()) ? Images.UP : Images.DOWN;// It was superior.getId() and it took me hours to figure out the error
                String pic1OrPic2 = rowIndex == 10 ? Images.PRIMARY : Images.SECONDARY;
                ChangeListener<Boolean> avilableListener = (ov, oldValue, newValue) -> {
                    URL resource;
                    if (!newValue) {
                        MouthItem.resetForks(item);
                        MouthItem.resetBleedingAndPlaque(item);
                        item.setImplant(false);
                        resource = Images.class.getResource(Images.FOLDER + Images.CROSS + upOrDown + columnIndex + pic1OrPic2 + Images.EXTENSION);
                    } else if (item.getImplant()) {
                        MouthItem.resetForks(item);
                        resource = Images.class.getResource(Images.FOLDER + Images.IMPLANT + upOrDown + columnIndex + pic1OrPic2 + Images.EXTENSION);
                    } else
                        resource = Images.class.getResource(Images.FOLDER + upOrDown + columnIndex + pic1OrPic2 + Images.EXTENSION);
                    pic.setImage(new Image(resource.toExternalForm()));
                };

                ChangeListener<Boolean> implantListener = (ov, oldValue, newValue) -> {
                    URL resource;
                    if (newValue) {
                        MouthItem.resetForks(item);
                        resource = Images.class.getResource(Images.FOLDER + Images.IMPLANT + upOrDown + columnIndex + pic1OrPic2 + Images.EXTENSION);
                    } else
                        resource = Images.class.getResource(Images.FOLDER + upOrDown + columnIndex + pic1OrPic2 + Images.EXTENSION);
                    pic.setImage(new Image(resource.toExternalForm()));
                };
                if (rowIndex == 10 && fork_1_Eligable.contains(columnIndex)) {//only rows that has forks
                    ChangeListener<Number> fork1Listener = (ov, oldValue, newValue) -> drawForks(pane, newValue, 1);
                    item.fork1Property().addListener(fork1Listener);
                    fork1Listener.changed(item.fork1Property(), null, item.getFork1());
                }
                //as the inferior part doesn't have forks in item 5,13 
                List<Integer> whichOne = gridPane.getId().equals(superior.getId()) ? fork_2_3_Eligable : fork_1_Eligable;
                if (rowIndex == 12 && whichOne.contains(columnIndex)) {//only rows that has forks
                    int isFork2AloneOrWithFork3 = gridPane.getId().equals(superior.getId()) ? 2 : 1;
                    ChangeListener<Number> fork2Listener = (ov, oldValue, newValue) -> drawForks(pane, newValue, isFork2AloneOrWithFork3);
                    item.fork2Property().addListener(fork2Listener);
                    fork2Listener.changed(item.fork2Property(), null, item.getFork2());
                    if (gridPane.getId().equals(superior.getId())) {//as the inferior part only got fork1,fork2
                        ChangeListener<Number> fork3Listener = (ov, oldValue, newValue) -> drawForks(pane, newValue, 3);
                        fork3Listener.changed(item.fork3Property(), null, item.getFork3());
                        item.fork3Property().addListener(fork3Listener);
                    }
                }

                item.availableProperty().addListener(avilableListener);
                item.implantProperty().addListener(implantListener);
                // a hack to fire the mcListener to initialize the pic
                avilableListener.changed(item.availableProperty(), null, item.getAvailable());
                //firing this listener will remove the affect of the previous one
                //implantListener.changed(item.implantProperty(), null, item.getImplant());
                break;

            case 13://probing-depth

                break;
            case 14://gingival-margin

                break;
            case 15://plaque 4,5,6
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
                };
                plaque4.setOnMouseClicked(mouseHandlerPlaque2);
                plaque5.setOnMouseClicked(mouseHandlerPlaque2);
                plaque6.setOnMouseClicked(mouseHandlerPlaque2);

                item.plaque4Property().addListener((ov, oldValue, newValue) -> stylePlaqueSVG(plaque4, (int) newValue));
                item.plaque5Property().addListener((ov, oldValue, newValue) -> stylePlaqueSVG(plaque5, (int) newValue));
                item.plaque6Property().addListener((ov, oldValue, newValue) -> stylePlaqueSVG(plaque6, (int) newValue));

                break;
            case 16://bleeding/oozing
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
                };
                bleeding4.setOnMouseClicked(mouseHandlerBleeding2);
                bleeding5.setOnMouseClicked(mouseHandlerBleeding2);
                bleeding6.setOnMouseClicked(mouseHandlerBleeding2);
                item.bleeding4Property().addListener((ov, oldValue, newValue) -> styleBleedingSVG(bleeding4, (int) newValue));
                item.bleeding5Property().addListener((ov, oldValue, newValue) -> styleBleedingSVG(bleeding5, (int) newValue));
                item.bleeding6Property().addListener((ov, oldValue, newValue) -> styleBleedingSVG(bleeding6, (int) newValue));

                break;
            case 17://fork
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
            case 18://note

                break;

        }
    }

    void setDisableOtherNodes(int ColumnIndex, GridPane gridPane, boolean disable) {
        List<Integer> rowsToDisable = Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 13, 14, 15, 16, 17, 18);
        gridPane.getChildren().parallelStream().forEach(child -> {
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
            //System.out.println("" + scale);
            vbox.setScaleX(scale);
            vbox.setScaleY(scale);
            
           vbox.setTranslateX(zoomMoveX);
           vbox.setTranslateY(zoomMoveY);
            System.out.println("layoutX= " + vbox.getTranslateX());
            System.out.println("layoutY= " + vbox.getTranslateY());
        }
    }

    public void zoomIn(ActionEvent event) {
        if (scale >= 0.5 && scale < 1.0) {
            scale = scale + 0.1;
            //as there could be values like 0.7999999999999999
            scale = Math.floor(scale * 100) / 100;
            vbox.setScaleX(scale);
            vbox.setScaleY(scale);
            vbox.setMinSize(scale * vbox.getMinWidth(), scale * vbox.getMinHeight());
            vbox.setPrefSize(scale * vbox.getPrefWidth(), scale * vbox.getPrefHeight());

        }
    }

}
