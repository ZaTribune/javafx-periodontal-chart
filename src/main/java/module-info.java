module shadow.dental_chart {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;

    opens shadow.dental_chart to javafx.fxml;
    exports shadow.dental_chart;
}