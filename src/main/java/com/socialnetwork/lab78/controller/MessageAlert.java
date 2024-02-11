package com.socialnetwork.lab78.controller;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Utility class for displaying message alerts in a JavaFX application.
 */
public class MessageAlert {

    /**
     * Displays a message alert with the specified parameters.
     * @param owner The stage that owns the alert.
     * @param type The type of the alert (e.g., INFORMATION, WARNING).
     * @param header The header text of the alert.
     * @param text The main content text of the alert.
     */
    public static void showMessage(Stage owner, Alert.AlertType type, String header, String text){
        Alert message=new Alert(type);
        message.setHeaderText(header);
        message.setContentText(text);
        message.initOwner(owner);
        message.showAndWait();
    }


    /**
     * Displays an error message alert with the specified text.
     * @param owner The stage that owns the alert.
     * @param text The main content text of the error alert.
     */
    public static void showErrorMessage(Stage owner, String text){
        Alert message=new Alert(Alert.AlertType.ERROR);
        message.initOwner(owner);
        message.setTitle("Mesaj eroare");
        message.setContentText(text);
        message.showAndWait();
    }
}
