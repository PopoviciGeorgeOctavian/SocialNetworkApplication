module com.socialnetwork.lab78 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    opens com.socialnetwork.lab78.controller to javafx.fxml;

    exports com.socialnetwork.lab78;
    exports com.socialnetwork.lab78.domain;
    exports com.socialnetwork.lab78.repository;
    exports com.socialnetwork.lab78.service;
    opens  com.socialnetwork.lab78.domain to javafx.base;
    opens com.socialnetwork.lab78 to javafx.fxml;
}