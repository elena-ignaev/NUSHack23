module com.example.nushack23 {
    requires javafx.controls;
    requires javafx.fxml;
            
        requires org.controlsfx.controls;
                        requires org.kordamp.bootstrapfx.core;
            
    opens com.example.nushack23 to javafx.fxml;
    exports com.example.nushack23;
}