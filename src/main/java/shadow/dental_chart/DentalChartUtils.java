/**
 *
 * @author Muhammad Ali Arafah
 *    https://github.com/ZaTribune
 */
package shadow.dental_chart;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.weathericons.WeatherIcon;
import de.jensd.fx.glyphs.weathericons.WeatherIconView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import shadow.dental_chart.entities.DentalChart;
import shadow.dental_chart.entities.MouthItem;

public abstract class DentalChartUtils {

    
    //to be moved to Listeners class
    public static ChangeListener<Number> boxBleedingListener = (ov, oldValue, newValue) -> {
        System.out.println("" + ov);
        System.out.println("" + ((IntegerProperty) ov));
        System.out.println("" + ((IntegerProperty) ov).getBean());
        styleBleedingSVG((JFXButton) ((IntegerProperty) ov).getBean(), (int) newValue);
    };

    public static String initializeDentalChart() {

        //a JSON String that contains 
        List<Integer> columnsList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 10, 11, 12, 13, 14, 15, 16, 17);

        DentalChart chart = new DentalChart();
        chart.setSuperiorMap(initializeMap(columnsList));
        chart.setInferiorMap(initializeMap(columnsList));

        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;
        try {
            json = objectMapper.writeValueAsString(chart);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;

    }

    private static Map<Integer, MouthItem> initializeMap(List<Integer> columnsList) {
        Map<Integer, MouthItem> items = new HashMap<>();
        columnsList.forEach(element -> {
            MouthItem item = new MouthItem();
            if (element == 1 || element == 5 || element == 10) {
                item.setAvailable(Boolean.FALSE);
            }
            if (element == 2 || element == 6 || element == 11) {
                item.setImplant(Boolean.TRUE);
            }
            item.setMobility(2);
            item.setFork1(1);
            item.setBleeding1(0);
            item.setBleeding2(1);
            item.setBleeding3(2);
            item.setPlaque1(0);
            item.setPlaque2(1);
            item.setPlaque3(0);
            item.setGum(5);
            item.setMargin1(1);
            item.setMargin2(2);
            item.setMargin3(3);
            item.setDepth1(10);
            item.setDepth2(11);
            item.setDepth3(12);
            item.setDepth4(10);
            item.setDepth5(11);
            item.setDepth6(12);
            item.setMargin4(2);
            item.setMargin5(1);
            item.setMargin6(0);
            item.setPlaque4(1);
            item.setPlaque5(0);
            item.setPlaque6(1);
            item.setBleeding4(0);
            item.setBleeding5(1);
            item.setBleeding6(2);
            item.setFork2(2);
            item.setFork3(3);
            item.setNote("no");
            item.setNerve1(Boolean.TRUE);
            item.setNerve2(Boolean.TRUE);
            item.setNerve3(Boolean.FALSE);
            items.put(element, item);
        });
        return items;

    }

    public static void styleRedCircles(Circle circle, boolean value) {
        if (circle.getStyleClass().size() > 1)
            circle.getStyleClass().remove(1);
        circle.getStyleClass().add(value ? "circle-nerve-clicked" : "circle-nerve-unclicked");

    }

    public static void styleForkButton(WeatherIconView icon, int value) {
        icon.setGlyphSize(20.0);
        switch (value) {
            case 0:
                icon.setGlyphSize(1.0);
                break;
            case 1:
                icon.setGlyphName(WeatherIcon.MOON_NEW.name());
                break;
            case 2:
                icon.setGlyphName(WeatherIcon.MOON_ALT_FIRST_QUARTER.name());
                break;
            case 3:
                icon.setGlyphName(WeatherIcon.MOON_FULL.name());
                break;
        }

    }

    public static void drawForks(StackPane parent, Number value, Integer fork) {
        // if fork2 is alone it will be centered as well be rendered as fork-icon1
        WeatherIconView icon = (WeatherIconView) parent.lookup(".fork-icon" + fork);
        if (icon == null) {
            icon = new WeatherIconView();
            icon.getStyleClass().add("fork-icon" + fork);
            icon.setGlyphSize(20.0);
            parent.getChildren().add(icon);

        }
        switch ((int) value) {
            case 0:
                parent.getChildren().remove(icon);
                return;
            case 1:
                icon.setGlyphName(WeatherIcon.MOON_NEW.name());
                break;
            case 2:
                icon.setGlyphName(WeatherIcon.MOON_ALT_FIRST_QUARTER.name());
                break;
            case 3:
                icon.setGlyphName(WeatherIcon.MOON_FULL.name());
                break;
        }
    }

    public static void styleBleedingSVG(JFXButton button, int value) {
        Canvas canvas = (Canvas) button.getGraphic();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        switch (value) {
            case 0:
            case 3:
                gc.setFill(Color.WHITE);
                gc.fillRect(1, 1, 14, 22);
                break;
            case 1:
                gc.setFill(Color.RED);
                gc.fillRect(1, 1, 14, 22);
                break;

            case 2:
                gc.setFill(Color.RED);
                gc.fillRect(1, 1, 14, 11);
                gc.setFill(Color.ORANGE);
                gc.fillRect(1, 12, 14, 11);
                break;
        }
//        Bounds bounds = svg.getBoundsInParent();
//        double scale = Math.min(20 / bounds.getWidth(), 20 / bounds.getHeight());
//        svg.setScaleX(scale);
//        svg.setScaleY(scale);
//        button.setGraphic(svg);
    }

    public static void stylePlaqueSVG(JFXButton button, int value) {
        Canvas canvas = (Canvas) button.getGraphic();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        switch (value) {
            case 0:
            case 2:
                gc.setFill(Color.WHITE);
                gc.fillRect(1, 1, 14, 22);
                break;
            case 1:
                gc.setFill(Color.CORNFLOWERBLUE);
                gc.fillRect(1, 1, 14, 22);
                break;
        }
//        Bounds bounds = svg.getBoundsInParent();
//        double scale = Math.min(20 / bounds.getWidth(), 20 / bounds.getHeight());
//        svg.setScaleX(scale);
//        svg.setScaleY(scale);
//        button.setGraphic(svg);
    }

}
