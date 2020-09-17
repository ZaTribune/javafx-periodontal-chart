package shadow.dental_chart;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.DefaultProperty;
import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.util.Duration;

import java.util.function.Function;

@DefaultProperty(value = "content")
public class MyScrollPane extends StackPane {

    private static final String DEFAULT_STYLE_CLASS = "my-scroll-pane";

    private final VBox contentContainer = new VBox();

    private Transform oldSceneTransform = null;

    public MyScrollPane() {
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);

        // clip content
        Rectangle clip = new Rectangle();
        this.setClip(clip);
        clip.widthProperty().bind(this.widthProperty());
        clip.heightProperty().bind(this.heightProperty());

        contentContainer.localToSceneTransformProperty().addListener((o, oldVal, newVal) -> oldSceneTransform = oldVal);
        final ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(contentContainer);
        scrollPane.setFitToWidth(true);

        scrollPane.setPannable(true);
        getChildren().setAll(scrollPane);
    }

    private double map(double val, double min1, double max1, double min2, double max2) {
        return min2 + (max2 - min2) * ((val - min1) / (max1 - min1));
    }

    public void setContent(Node content) {
        switch (contentContainer.getChildren().size()) {
            case 2:
                contentContainer.getChildren().set(1, content);
                break;
            case 1:
                contentContainer.getChildren().add(content);
                break;
            default:
                contentContainer.getChildren().setAll(content);
                break;
        }
        VBox.setVgrow(content, Priority.ALWAYS);
    }

    public Node getContent() {
        return contentContainer.getChildren().size() == 2 ? contentContainer.getChildren().get(1) : null;
    }

    private static void customScrolling(ScrollPane scrollPane, DoubleProperty scrollDriection, Function<Bounds, Double> sizeFunc) {
        final double[] frictions = {0.99, 0.1, 0.05, 0.04, 0.03, 0.02, 0.01, 0.04, 0.01, 0.008, 0.008, 0.008, 0.008, 0.0006, 0.0005, 0.00003, 0.00001};
        final double[] pushes = {1};
        final double[] derivatives = new double[frictions.length];

        Timeline timeline = new Timeline();
        final EventHandler<MouseEvent> dragHandler = event -> timeline.stop();
        final EventHandler<ScrollEvent> scrollHandler = event -> {
            if (event.getEventType() == ScrollEvent.SCROLL) {
                int direction = event.getDeltaY() > 0 ? -1 : 1;
                for (int i = 0; i < pushes.length; i++) {
                    derivatives[i] += direction * pushes[i];
                }
                if (timeline.getStatus() == Animation.Status.STOPPED) {
                    timeline.play();
                }
                event.consume();
            }
        };
        if (scrollPane.getContent().getParent() != null) {
            scrollPane.getContent().getParent().addEventHandler(MouseEvent.DRAG_DETECTED, dragHandler);
            scrollPane.getContent().getParent().addEventHandler(ScrollEvent.ANY, scrollHandler);
        }
        scrollPane.getContent().parentProperty().addListener((o,oldVal, newVal)->{
            if (oldVal != null) {
                oldVal.removeEventHandler(MouseEvent.DRAG_DETECTED, dragHandler);
                oldVal.removeEventHandler(ScrollEvent.ANY, scrollHandler);
            }
            if (newVal != null) {
                newVal.addEventHandler(MouseEvent.DRAG_DETECTED, dragHandler);
                newVal.addEventHandler(ScrollEvent.ANY, scrollHandler);
            }
        });
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(3), (event) -> {
            for (int i = 0; i < derivatives.length; i++) {
                derivatives[i] *= frictions[i];
            }
            for (int i = 1; i < derivatives.length; i++) {
                derivatives[i] += derivatives[i - 1];
            }
            double dy = derivatives[derivatives.length - 1];
            double size = sizeFunc.apply(scrollPane.getContent().getLayoutBounds());
            scrollDriection.set(Math.min(Math.max(scrollDriection.get() + dy / size, 0), 1));
            if (Math.abs(dy) < 0.001) {
                timeline.stop();
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    public static void smoothScrolling(ScrollPane scrollPane) {
        customScrolling(scrollPane, scrollPane.vvalueProperty(), bounds -> bounds.getHeight());
    }

    public static void smoothHScrolling(ScrollPane scrollPane) {
        customScrolling(scrollPane, scrollPane.hvalueProperty(), bounds -> bounds.getWidth());
    }

}

