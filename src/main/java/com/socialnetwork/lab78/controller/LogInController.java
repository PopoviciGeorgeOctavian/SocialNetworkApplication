package com.socialnetwork.lab78.controller;

import com.socialnetwork.lab78.domain.User;
import com.socialnetwork.lab78.service.MessageService;
import com.socialnetwork.lab78.service.Service;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The `LogInController` class handles user authentication and navigation to the main application view.
 */
public class LogInController {
    @FXML
    private TextField emailUser;

    @FXML
    private PasswordField passwordUser;

    @FXML
    private Text emailErrorText;

    @FXML
    private Text passwordErrorText;

    private Service service;

    private MessageService messageService;

    /**
     * Sets the application service.
     * @param service The application service to be set.
     */
    public void setService(Service service) {
        this.service = service;
    }

    /**
     * Retrieves the application service.
     * @return The application service.
     */
    public Service getService() {
        return service;
    }

    /**
     * Retrieves the message service.
     * @return The message service.
     */
    public MessageService getMessageService() {
        return messageService;
    }


    /**
     * Sets the message service.
     * @param messageService The message service to be set.
     */
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * Handles the login button click event.
     *
     * @param event The ActionEvent triggered by the login button click.
     * @throws IOException if there is an issue loading the main application view.
     */
    @FXML
    protected void onLogInButtonCLick(ActionEvent event) throws IOException {
        User user = service.getUserByEmail(emailUser.getText());

        if (user == null) // show a message
        {
            emailErrorText.setVisible(true);
            passwordErrorText.setVisible(false);
        } else if (!passwordUser.getText().trim().equals(user.getPassword())) { // show a message
            passwordErrorText.setVisible(true);
            emailErrorText.setVisible(false);
        } else { // enter Application
            emailErrorText.setVisible(false);
            passwordErrorText.setVisible(false);

            FXMLLoader stageLoader = new FXMLLoader();
            stageLoader.setLocation(getClass().getResource("/com/socialnetwork/lab78/views/UserInterface.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            AnchorPane appLayout = stageLoader.load();
            Scene scene = new Scene(appLayout);
            stage.setScene(scene);

            UserController appController = stageLoader.getController();
            appController.setService(this.service);
            appController.setMessageService(messageService);
            appController.initApp(user);

            stage.show();
        }
    }

    /**
     * Handles the text change event in the email field.
     */
    public void onTextChanged() {
        emailErrorText.setVisible(false);
        passwordErrorText.setVisible(false);
    }

    /**
     * Handles the password change event.
     */
    public void onPasswordChanged() {
        passwordErrorText.setVisible(false);
    }


}
