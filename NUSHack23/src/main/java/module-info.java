module com.example.nushack23 {
    requires javafx.controls;
    requires javafx.fxml;
            
        requires org.controlsfx.controls;
                        requires org.kordamp.bootstrapfx.core;

    requires redis.clients.jedis;
    requires static kafka.clients;
    requires java.net.http;
    requires org.apache.httpcomponents.client5.httpclient5;
    requires org.apache.httpcomponents.core5.httpcore5.h2;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.httpcomponents.core5.httpcore5;
    requires org.json;

    opens com.example.nushack23 to javafx.fxml;
    exports com.example.nushack23;

    opens com.example.nushack23.Controller to javafx.fxml;
    exports com.example.nushack23.Controller;

    opens com.example.nushack23.Model to javafx.graphics;
    exports com.example.nushack23.Model;
}