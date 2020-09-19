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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Pair;
import shadow.dental_chart.entities.DentalChart;
import shadow.dental_chart.entities.MouthItem;

public abstract class DentalChartUtils {

    public static void drawPath(Pane pane, List<Circle> circles, MouthItem item, int direction) {
        //todo originY basically is always 3 : so we can remove it entirely
        Path redPath = new Path();
        Circle redCircle1 = circles.get(0);
        Circle redCircle2 = circles.get(1);
        Circle redCircle3 = circles.get(2);
        int marginA;
        int marginB;
        int marginC;
        int depthA;
        int depthB;
        int depthC;
        if (direction == -1) {
            marginA = Integer.parseInt(item.getMargin1());
            marginB = Integer.parseInt(item.getMargin2());
            marginC = Integer.parseInt(item.getMargin3());
            depthA = Integer.parseInt(item.getDepth1());
            depthB = Integer.parseInt(item.getDepth2());
            depthC = Integer.parseInt(item.getDepth3());
        } else {
            marginA = Integer.parseInt(item.getMargin4());
            marginB = Integer.parseInt(item.getMargin5());
            marginC = Integer.parseInt(item.getMargin6());
            depthA = Integer.parseInt(item.getDepth4());
            depthB = Integer.parseInt(item.getDepth5());
            depthC = Integer.parseInt(item.getDepth6());
        }

        double redHeight1 = 5 * marginA * direction;
        double redHeight2 = 5 * marginB * direction;
        double redHeight3 = 5 * marginC * direction;

        redPath.getElements().addAll(
                new MoveTo(redCircle1.getLayoutX(), 0),
                new LineTo(redCircle1.getLayoutX(), redHeight1),
                new LineTo(redCircle2.getLayoutX(), redHeight2),
                new LineTo(redCircle3.getLayoutX(), redHeight3),
                new LineTo(redCircle3.getLayoutX(), 0)
        );
        redCircle1.setLayoutY(redHeight1);
        redCircle2.setLayoutY(redHeight2);
        redCircle3.setLayoutY(redHeight3);
        redCircle1.setFill(Color.FIREBRICK);
        redCircle2.setFill(Color.FIREBRICK);
        redCircle3.setFill(Color.FIREBRICK);
        redCircle1.setStroke(Color.CRIMSON);
        redCircle2.setStroke(Color.CRIMSON);
        redCircle3.setStroke(Color.CRIMSON);

        redPath.setStroke(Color.RED);
        redPath.setFill(Color.rgb(255, 33, 81, 0.4));

        // BLUE
        Path bluePath = new Path();
        double blueHeight1;
        double blueHeight2;
        double blueHeight3;

        blueHeight1 = 5 * (marginA + depthA) * direction;// as per chart the ratio
        blueHeight2 = 5 * (marginB + depthB) * direction;
        blueHeight3 = 5 * (marginC + depthC) * direction;

        bluePath.getElements().addAll(
                new MoveTo(redCircle1.getLayoutX(), 0),
                new LineTo(redCircle1.getLayoutX(), blueHeight1),
                new LineTo(redCircle2.getLayoutX(), blueHeight2),
                new LineTo(redCircle3.getLayoutX(), blueHeight3),
                new LineTo(redCircle3.getLayoutX(), 0)
        );
        Circle blueCircle1 = circles.get(3);
        Circle blueCircle2 = circles.get(4);
        Circle blueCircle3 = circles.get(5);
        blueCircle1.setFill(Color.DARKBLUE);
        blueCircle2.setFill(Color.DARKBLUE);
        blueCircle3.setFill(Color.DARKBLUE);
        blueCircle1.setStroke(Color.BLUE);
        blueCircle2.setStroke(Color.BLUE);
        blueCircle3.setStroke(Color.BLUE);
        bluePath.setStroke(Color.BLUE);
        bluePath.setFill(Color.rgb(81, 33, 255, 0.4));//just flip the value of blue with red

        blueCircle1.setLayoutX(redCircle1.getLayoutX());
        blueCircle1.setLayoutY(blueHeight1);
        blueCircle2.setLayoutX(redCircle2.getLayoutX());
        blueCircle2.setLayoutY(blueHeight2);
        blueCircle3.setLayoutX(redCircle3.getLayoutX());
        blueCircle3.setLayoutY(blueHeight3);
        pane.getChildren().clear();// to fix the overlay issue
        pane.getChildren().addAll(bluePath, redPath, blueCircle1, blueCircle2, blueCircle3, redCircle1, redCircle2, redCircle3);

    }

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
            item.setAvailable(Boolean.TRUE);
            item.setImplant(Boolean.FALSE);
            item.setMobility(0);
            item.setFork1(0);
            item.setBleeding1(0);
            item.setBleeding2(0);
            item.setBleeding3(0);
            item.setPlaque1(0);
            item.setPlaque2(0);
            item.setPlaque3(0);
            item.setPlaque4(0);
            item.setPlaque5(0);
            item.setPlaque6(0);
            item.setGum(50.0);
            item.setMargin1("0");
            item.setMargin2("0");
            item.setMargin3("0");
            item.setMargin4("0");
            item.setMargin5("0");
            item.setMargin6("0");
            item.setDepth1("0");
            item.setDepth2("0");
            item.setDepth3("0");
            item.setDepth4("0");
            item.setDepth5("0");
            item.setDepth6("0");
            item.setBleeding4(0);
            item.setBleeding5(0);
            item.setBleeding6(0);
            item.setFork2(0);
            item.setFork3(0);
            item.setNote("no");
            item.setSelectedCircle(-1);
            items.put(element, item);
        });
        return items;

    }

    public static void styleRedCircles(int index, List<Circle> circles) {
        circles.forEach(circle -> {
            if (circle.getStyleClass().size() > 1)
                circle.getStyleClass().remove(1);
            circle.getStyleClass().add(circles.indexOf(circle) == index ? "circle-nerve-clicked" : "circle-nerve-unclicked");
        });
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

    public static void drawForks(Pane parent, Number value, Integer fork) {
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

    public static int calculateDentalChartPlaque(DentalChart dentalChart) {
        AtomicInteger result = new AtomicInteger(0);
        dentalChart.getSuperiorMap().forEach((k, v) -> {
            result.getAndAdd(v.getTotalPlaque());
        });
        dentalChart.getInferiorMap().forEach((k, v) -> {
            result.getAndAdd(v.getTotalPlaque());
        });
        return result.get();
    }

    public static Pair<Integer, Integer> calculateDentalChartMeanProbingDepth(DentalChart dentalChart) {

        AtomicInteger result1 = new AtomicInteger(0);
        AtomicInteger result2 = new AtomicInteger(0);
        dentalChart.getSuperiorMap().forEach((k, v) -> {
            if (v.getAvailable()) {
                result1.getAndAdd(v.getTotalProbingDepth());
                result2.getAndAdd(v.getTotalGingivalMargin());
            }
        });
        dentalChart.getInferiorMap().forEach((k, v) -> {
            if (v.getAvailable()) {
                result1.getAndAdd(v.getTotalProbingDepth());
                result2.getAndAdd(v.getTotalGingivalMargin());
            }
        });
        return new Pair<>(result1.get(), result2.get());
    }

    public static int calculateDentalBleedingOnProbing(DentalChart dentalChart) {
        AtomicInteger result = new AtomicInteger(0);
        dentalChart.getSuperiorMap().forEach((k, v) -> {
            result.getAndAdd(v.getTotalBleedingOnProbing());
        });
        dentalChart.getInferiorMap().forEach((k, v) -> {
            result.getAndAdd(v.getTotalBleedingOnProbing());
        });
        return result.get();
    }

}
