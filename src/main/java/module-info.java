module com.demo.errorsimulator {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.demo.errorsimulator to javafx.fxml;
    exports com.demo.errorsimulator;
}