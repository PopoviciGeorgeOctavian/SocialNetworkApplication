package com.socialnetwork.lab78.controller;

import com.socialnetwork.lab78.Paging.Page;
import com.socialnetwork.lab78.Paging.Pageable;
import com.socialnetwork.lab78.domain.User;
import com.socialnetwork.lab78.service.MessageService;
import com.socialnetwork.lab78.service.Service;
import com.socialnetwork.lab78.utils.observer.Observable;
import com.socialnetwork.lab78.utils.observer.Observer;
import com.socialnetwork.lab78.utils.observer.UserChangeEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.socialnetwork.lab78.controller.MessageAlert.showMessage;

public class UserController implements Observer<UserChangeEvent>{

   Service service;

    private MessageService messageService;

    private int currentPage = 0;

    private int pageSize = 10;

    private int totalNumberOfElements = 0;

    ObservableList<User> users = FXCollections.observableArrayList();


    public MessageService getMessageService() {
        return messageService;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }



    @FXML
    private TableView<User> usersTable;
    @FXML
    private TableColumn<User, UUID> idColumn;
    @FXML
    private TableColumn<User, String> firstNameColumn;
    @FXML
    private TableColumn<User, String> lastNameColumn;
    @FXML
    private TextField firstNameField, lastNameField;

    @FXML
    private Button addButton, deleteButton, nextButton, previousButton;

    @FXML
    public void setService(Service serviceUsers){
        this.service = serviceUsers;
        service.addObserver(this);
        initModel();

    }

    private void initModel()
    {

        Page<User> page = service.findAll(new Pageable(currentPage, pageSize));

        int maxPage = (int) Math.ceil((double) page.getTotalElementCount() / pageSize)-1;
        if(maxPage == -1)
        {
            maxPage = 0;
        }
        if(currentPage > maxPage){
            currentPage = maxPage;
            page = service.findAll(new Pageable(currentPage, pageSize));
        }

        users.setAll(StreamSupport.stream(page.getElementsOnPage().spliterator(),
                false).collect(Collectors.toList()));
        totalNumberOfElements=page.getTotalElementCount();

        previousButton.setDisable(currentPage == 0);
        nextButton.setDisable((currentPage+1)*pageSize >+ totalNumberOfElements);
        usersTable.refresh();
    }

   /* private void loadUsers() {
        users.clear();
        Iterable<User> userIterable = service.getAllUseri();
        for (User user : userIterable) {
            users.add(user);
        }
    }
*/
    @FXML
    private void initialize() {
        usersTable.setItems(users);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
    }

    @FXML
    private void addUser(ActionEvent actionEvent) {
        try {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();

            // Validate inputs
            if (firstName.isEmpty() || lastName.isEmpty()) {
                MessageAlert.showErrorMessage(null, "First name and last name cannot be empty.");
                return;
            }

            service.addUser(firstName, lastName);
            //loadUsers();
            clearForm();
            usersTable.refresh();
            initModel();
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "User Added", "User successfully added.");
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, "An error occurred: " + e.getMessage());
        }
        usersTable.refresh();

    }



    private void clearForm() {
        firstNameField.clear();
        lastNameField.clear();
    }


    @FXML
    private void deleteUser(ActionEvent actionEvent) {
        try {
            User selectedUser = usersTable.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                String firstName = selectedUser.getFirstName();
                String lastName = selectedUser.getLastName();
                service.deleteUser(firstName,lastName);
                //loadUsers();
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "User Deleted", "User successfully deleted.");
                usersTable.refresh();
                initModel();

            }
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, "An error occurred: " + e.getMessage());
        }
        initialize();
    }

    @FXML
    private void updateUser(ActionEvent actionEvent) {
        try {
            User selectedUser = usersTable.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                String newFirstName = firstNameField.getText();
                String newLastName = lastNameField.getText();
                UUID id=selectedUser.getId();
                service.updateUser(id, newFirstName, newLastName);
                //loadUsers();
                initModel();
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "User Updated", "User successfully updated.");
                usersTable.refresh();

            }
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, "An error occurred: " + e.getMessage());
        }
        usersTable.refresh();
    }

    @FXML
    private void generareUseri(ActionEvent actionEvent) {
        try {
            // Confirm user's intention to delete all data
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation Dialog");
            confirmationAlert.setHeaderText("Confirm Deletion");
            confirmationAlert.setContentText("Are you sure you want to initialize data?");

            Optional<ButtonType> result = confirmationAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                // User confirmed, delete all data and add new entities
                for (User user : users) {
                    service.deleteUser(user.getFirstName(), user.getLastName());
                }

                service.addEntities();
                initModel();
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Data Cleared", "All data successfully deleted.");
            }
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, "An error occurred: " + e.getMessage());
        }
    }


    @Override
    public void update(UserChangeEvent t) {
        initModel();
    }

    public void onPrevious(ActionEvent actionEvent){
        currentPage--;
        initModel();
    }

    public void onNext(ActionEvent actionEvent){
        currentPage++;
        initModel();
    }
}
