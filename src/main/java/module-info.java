module shadow.dental_chart {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.base;
    requires de.jensd.fx.glyphs.weathericons;
       requires de.jensd.fx.glyphs.materialdesignicons;
       
    requires jfxtras.common;
    requires com.jfoenix;
    requires com.fasterxml.jackson.databind;
    requires org.controlsfx.controls;
    opens shadow.dental_chart to javafx.fxml;
    exports shadow.dental_chart;
    exports shadow.dental_chart.entities;
}
