module com.tribune.dental_chart {
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
    requires org.slf4j;
    opens com.tribune.dental_chart to javafx.fxml;
    exports com.tribune.dental_chart;
    exports com.tribune.dental_chart.entity;
}
