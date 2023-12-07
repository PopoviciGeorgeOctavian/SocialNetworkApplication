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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class LogInController {
    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private Text  firstNameErrorText;

    @FXML
    private Text lastNameErrorText;

    private Service service;

    private MessageService messageService;

    public void setService(Service service) {
        this.service = service;
    }

    public Service getService() {
        return service;
    }

    public MessageService getMessageService() {
        return messageService;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @FXML
    protected void onLogInButtonCLick(ActionEvent event) throws IOException {
        User user = service.getUserByNumePrenume(firstName.getText(),lastName.getText());
        System.out.println(user);

        if(user == null) // show a message
        {
            firstNameErrorText.setVisible(true);
            lastNameErrorText.setVisible(true);
        }

        else { // enter Application
            firstNameErrorText.setVisible(false);
            lastNameErrorText.setVisible(false);

            FXMLLoader stageLoader = new FXMLLoader();
            stageLoader.setLocation(getClass().getResource("/com/socialnetwork/lab78/views/UserInterface.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

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

    public void onTextChanged(KeyEvent evt) {
        firstNameErrorText.setVisible(false);
        lastNameErrorText.setVisible(false);
    }


}
