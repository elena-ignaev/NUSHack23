module com.example.nushack23 {
    requires javafx.controls;
    requires javafx.fxml;
            
        requires org.controlsfx.controls;
                        requires org.kordamp.bootstrapfx.core;

    requires redis.clients.jedis;
    requires static kafka.clients;
    //requires deeplearning4j.core;

    opens com.example.nushack23 to javafx.fxml;
    exports com.example.nushack23;

    opens com.example.nushack23.Controller to javafx.fxml;
    exports com.example.nushack23.Controller;

    opens com.example.nushack23.Model to javafx.graphics;
    exports com.example.nushack23.Model;
}