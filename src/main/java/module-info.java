module yourpackage.visualization {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.media;
    requires javafx.swing;

    // Export packages if needed
    exports yourpackage.visualization;
    exports yourpackage.app;

    // Opens packages if you need to reflectively access them
    opens yourpackage.app to javafx.fxml;
    opens yourpackage.visualization to javafx.fxml;

    // Add any additional exports or opens directives as necessary
}
