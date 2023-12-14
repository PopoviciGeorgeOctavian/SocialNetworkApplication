package com.socialnetwork.lab78.controller;

import com.socialnetwork.lab78.Paging.Page;
import com.socialnetwork.lab78.Paging.Pageable;
import com.socialnetwork.lab78.domain.FriendShip;
import com.socialnetwork.lab78.domain.Message;
import com.socialnetwork.lab78.domain.User;
import com.socialnetwork.lab78.service.MessageService;
import com.socialnetwork.lab78.service.Service;
import com.socialnetwork.lab78.utils.observer.Event;
import com.socialnetwork.lab78.utils.observer.Observable;
import com.socialnetwork.lab78.utils.observer.Observer;
import com.socialnetwork.lab78.utils.observer.UserChangeEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.socialnetwork.lab78.controller.MessageAlert.showMessage;

public class UserController implements Observer<UserChangeEvent>{

    Service service;

    @FXML
    private Text username;


    private MessageService messageService;

    private int currentPage = 0;

    private int pageSize = 10;

    private int totalNumberOfElements = 0;

    private User user;

    @FXML
    private TableView<User> usersTable;

    @FXML
    private TableColumn<User, UUID> idColumn;
    @FXML
    private TableColumn<User, String> firstNameColumn;
    @FXML
    private TableColumn<User, String> lastNameColumn;
    @FXML
    private TextField firstNameField, lastNameField, getMesajField;

   /* @FXML
    private TableView<FriendShip> friendshipsTable;
    @FXML
    private TableColumn<User, UUID> idColumn;
    @FXML
    private TableColumn<User, String> firstNameU1Column;
    @FXML
    private TableColumn<User, String> lastNameColumn;
    @FXML
    private TextField firstNameField, lastNameField;

    */

    @FXML
    private ListView<String> friendRequestsSent;
    @FXML
    private ListView<String> friendsRequestList;
    @FXML
    private ListView<String> friendsList;

    @FXML
    private ListView<String> messagesFriendList;

    @FXML
    private Button addButton, deleteButton, nextButton, previousButton,sendRequestButton;

    @FXML
    public Button btnAdMesaj, btnafisareConversatii, acceptButton, declineButton, cancelButton;

    private final ObservableList<User> users = FXCollections.observableArrayList();

    private final ObservableList<String> listaMesaje = FXCollections.observableArrayList();

    private final ObservableList<String> friendsObs = FXCollections.observableArrayList();

    private final ObservableList<String> friendsReqObs = FXCollections.observableArrayList();

    private final ObservableList<String> friendsReqSentObs = FXCollections.observableArrayList();


    @FXML
    public void setService(Service serviceUsers){
        this.service = serviceUsers;
        service.addObserver(this);
        initModel();


    }

    public MessageService getMessageService() {
        return messageService;
    }

    public Service getService() {
        return service;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public void initApp(User user) {
        this.user = user;
        username.setText(user.getFirstName() + " " + user.getLastName());

        listaMesaje.clear();
        listaMesaje.addAll(messageService.getAllMessages().stream()
                .filter(s -> s != null) // Filter out null elements, if any
                .map(String::valueOf)   // Convert elements to strings
                .collect(Collectors.toList()));


        // Clear and add friends
        friendsObs.clear();

        List<User> acceptedFriends = service.getAcceptedFriendsOfUser(user);

// Filter out the currently connected user from the list of accepted friends
        List<String> acceptedFullNames = acceptedFriends.stream()
                .filter(friend -> !friend.equals(user)) // Assuming equals() method is appropriately overridden in your User class
                .map(friend -> friend.getFirstName() + " " + friend.getLastName())
                .collect(Collectors.toList());

        friendsObs.addAll(acceptedFullNames);

        friendsReqObs.clear();
        // Add received friend requests
        StreamSupport.stream(service.printFriendships().spliterator(), false)
                .filter(f -> f instanceof FriendShip)
                .map(f -> (FriendShip) f)
                .filter(friendShip -> friendShip.getUser2().getId().equals(user.getId()))
                .forEach(friendShip -> friendsReqObs.add(
                        friendShip.getUser1().getFirstName() + " " + friendShip.getUser1().getLastName() +
                                " " + friendShip.getDate() +
                                " " + friendShip.getAcceptance()));


        friendsReqSentObs.clear();
        // Add sent friend requests
        StreamSupport.stream(service.printFriendships().spliterator(), false)
                .filter(f -> f instanceof FriendShip)
                .map(f -> (FriendShip) f)
                .filter(friendShip -> friendShip.getUser1().getId().equals(user.getId()))
                .forEach(friendShip -> friendsReqSentObs.add(
                        friendShip.getUser2().getFirstName() + " " + friendShip.getUser2().getLastName() +
                                " " + friendShip.getDate() +
                                " " + friendShip.getAcceptance()));
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
        usersTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        friendsRequestList.setItems(friendsReqObs);
        friendRequestsSent.setItems(friendsReqSentObs);
        friendsList.setItems(friendsObs);
        messagesFriendList.setItems(listaMesaje);
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
        initApp(this.user);
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
                initApp(this.user);
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
        initApp(this.user);
    }

    public void onPrevious(ActionEvent actionEvent){
        currentPage--;
        initModel();
    }

    public void onNext(ActionEvent actionEvent){
        currentPage++;
        initModel();
    }

    public void enterMessages(ActionEvent event) {
        Alert a = new Alert(Alert.AlertType.NONE);
        a.show();
    }

    public void sendMessage(KeyEvent event) {
        if(event.getCode() != KeyCode.ENTER)
            return;

        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Message", "Message successfully sent.");


    }

    @FXML
    private void afisareConversatii(ActionEvent actionEvent) {
            UUID from = user.getId();
            try {
                User to = usersTable.getSelectionModel().getSelectedItem();
                UUID id_to = null;
                if (to != null) {
                    id_to = to.getId();
                }

                String str = messageService.conversation(from, id_to).stream()
                        .map(tup -> tup.getFrom().getLastName() + " " + tup.getFrom().getFirstName() + ": " +
                                tup.getMessage() + "\nTrimis la: " +
                                tup.getData().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)) + '\n')
                        .collect(Collectors.joining("\n"));

                openSecondaryWindow(str);

            } catch (Exception e) {
                MessageAlert.showErrorMessage(null, "An error occurred: " + e.getMessage());
            }
    }

    @FXML
    private void adaugaMesaj(ActionEvent actionEvent) {
        try {
            // Get the selected users from the table
            List<User> selectedUsers = usersTable.getSelectionModel().getSelectedItems();

            // Check if users are selected
            if (selectedUsers.isEmpty()) {
                throw new Exception("No users selected!");
            }

            List<User> id_toList = selectedUsers.stream().collect(Collectors.toList());

            String mesaj = this.getMesajField.getText();
            if (mesaj.isEmpty()) throw new Exception("Mesaj null!");

            // Assuming MessageService.addMessage expects a List<UUID> for recipients
            messageService.addMessage(user, id_toList, mesaj);

            getMesajField.clear();
            initApp(this.user);
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, "An error occurred: " + e.getMessage());
        }
    }



    private void openSecondaryWindow(String deAfisat) {
        // Creează o nouă fereastră (Stage)
        Stage secondaryStage = new Stage();
        secondaryStage.setTitle("Fereastra Secundară");

        // Creează un obiect Label cu șirul
        javafx.scene.control.Label label = new javafx.scene.control.Label(deAfisat + "\n\n\n");

        // Layout pentru fereastra secundară
        StackPane secondaryLayout = new StackPane();
        secondaryLayout.getChildren().add(label);

        // Setează scena pentru fereastra secundară
        Scene secondaryScene = new Scene(secondaryLayout);
        secondaryStage.setScene(secondaryScene);
        secondaryStage.setWidth(300);

        // Arată fereastra secundară
        secondaryStage.show();
    }

    public void sendRequest(ActionEvent actionEvent) {

        try {
            User selectedUser = usersTable.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                // Assuming user is the currently logged-in user
                User loggedInUser = user;

                // Check if the users are already friends
                if (service.areAlreadyFriends(loggedInUser, selectedUser)) {
                    MessageAlert.showMessage(null, Alert.AlertType.WARNING, "Already Friends", "You are already friends with " + selectedUser.getFirstName() + " " + selectedUser.getLastName());
                } else {
                    // If they are not already friends, proceed to send the friend request
                    String FirstNameU1 = loggedInUser.getFirstName();
                    String LastNameU1 = loggedInUser.getLastName();
                    String FirstNameU2 = selectedUser.getFirstName();
                    String LastNameU2 = selectedUser.getLastName();

                    // Check if there is an existing friend request in PENDING status
                    if (!service.areFriendRequestsPending(loggedInUser, selectedUser)) {
                        // If no pending friend request, send the new friend request
                        service.createFriendRequest(FirstNameU1, LastNameU1, FirstNameU2, LastNameU2);
                        friendsReqSentObs.removeAll(friendsReqSentObs.stream().toList());
                        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Friend Request Sent", "Friend Request successfully sent.");
                        initApp(loggedInUser);
                    } else {
                        MessageAlert.showMessage(null, Alert.AlertType.WARNING, "Friend Request Pending", "You already have a pending friend request with " + selectedUser.getFirstName() + " " + selectedUser.getLastName());
                    }
                }
            }
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, "An error occurred: " + e.getMessage());
        }
    }



        public void acceptFriendRequest(ActionEvent actionEvent) {
        try {
            if (friendsRequestList.getSelectionModel().getSelectedItem() == null)
                return;

            String userInfo = friendsRequestList.getSelectionModel().getSelectedItem().toString();
            String firstnameU2 = userInfo.split(" ")[0];
            String lastnameU2 = userInfo.split(" ")[1];
            String status = userInfo.split(" ")[3];

            if (!status.equals("PENDING"))
                return;
            String FirstNameU1 = user.getFirstName();
            String LastNameU1 = user.getLastName();

            service.removeFriendShip(firstnameU2, lastnameU2, FirstNameU1, LastNameU1);
            friendsObs.removeAll(friendsObs.stream().toList());
            friendsReqObs.removeAll(friendsReqObs.stream().toList());
            service.addFriendShip(firstnameU2, lastnameU2, FirstNameU1, LastNameU1);
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Friend Request Accepted", "Friend Request successfully accepted.");
            initApp(this.user);

        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, "An error occurred: " + e.getMessage());
        }
    }

    public void declineFriendRequest(ActionEvent actionEvent) {
        try {
            if (friendsRequestList.getSelectionModel().getSelectedItem() == null)
                return;

            String userInfo = friendsRequestList.getSelectionModel().getSelectedItem().toString();
            String firstnameU2 = userInfo.split(" ")[0];
            String lastnameU2 = userInfo.split(" ")[1];
            String status = userInfo.split(" ")[3];
            String FirstNameU1 = user.getFirstName();
            String LastNameU1 = user.getLastName();

            if (!status.equals("PENDING"))
                return;

            service.removeFriendShip(firstnameU2, lastnameU2, FirstNameU1, LastNameU1);
            friendsObs.removeAll(friendsObs.stream().toList());
            friendsReqObs.removeAll(friendsReqObs.stream().toList());
            service.declineFriendRequest(firstnameU2, lastnameU2, FirstNameU1, LastNameU1);
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Friend Request Declined", "Friend Request successfully declined.");
            initApp(this.user);

        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, "An error occurred: " + e.getMessage());
        }
    }
    public void cancelFriendRequest(ActionEvent actionEvent) {
        try {
            if (friendRequestsSent.getSelectionModel().getSelectedItem() == null)
                return;

            String userInfo = friendRequestsSent.getSelectionModel().getSelectedItem().toString();
            String firstnameU2 = userInfo.split(" ")[0];
            String lastnameU2 = userInfo.split(" ")[1];
            String status = userInfo.split(" ")[3];

            if (!status.equals("PENDING"))
                return;

            String FirstNameU1 = user.getFirstName();
            String LastNameU1 = user.getLastName();

            service.removeFriendShip(FirstNameU1, LastNameU1, firstnameU2, lastnameU2);
            friendsReqSentObs.removeAll(friendsReqSentObs.stream().toList());

            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Friend Request Canceled", "Friend Request successfully canceled.");
            initApp(this.user);

        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, "An error occurred: " + e.getMessage());
        }
    }

}
