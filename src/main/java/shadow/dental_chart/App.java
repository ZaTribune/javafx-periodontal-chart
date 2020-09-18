/**
 *
 * @author Muhammad Ali Arafah
 *    https://github.com/ZaTribune
 */
package shadow.dental_chart;

import com.jfoenix.controls.JFXScrollPane;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.scene.control.Button;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML(DentalChartController.class.getSimpleName()), 1024, 768);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String name) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fxml/"+name + ".fxml"));
        /* I had performance and lag issues  using the usuall javafx ScrollPane specially after wrapping the chart content inside a Group
          ScrollPane
               -> Group
                         -> VBox
                                   -> ....chart content

        */
        MyScrollPane scrollPane=new MyScrollPane();
        scrollPane.setContent(fxmlLoader.load());        
        scrollPane.getStylesheets().add(App.class.getResource("/css/chart.css").toExternalForm());
        return scrollPane;
    }

    public static void main(String[] args) {
        launch();
    }

}