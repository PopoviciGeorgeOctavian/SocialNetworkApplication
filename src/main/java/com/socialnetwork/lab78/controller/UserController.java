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

public class UserController implements Observer<UserChangeEvent> {

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
    private TableColumn<User, String> emailColumn;

    @FXML
    private TextField firstNameField, emailField, lastNameField, getMesajField;

    @FXML
    private Slider pageSizeSlider;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ListView<String> friendRequestsSent;
    @FXML
    private ListView<String> friendsRequestList;
    @FXML
    private ListView<String> friendsList;

    @FXML
    private ListView<String> messagesFriendList;

    @FXML
    private Button addButton, deleteButton, nextButton, previousButton, sendRequestButton;

    @FXML
    public Button btnAdMesaj, btnafisareConversatii, acceptButton, declineButton, cancelButton;

    private final ObservableList<User> users = FXCollections.observableArrayList();

    private final ObservableList<String> listaMesaje = FXCollections.observableArrayList();

    private final ObservableList<String> friendsObs = FXCollections.observableArrayList();

    private final ObservableList<String> friendsReqObs = FXCollections.observableArrayList();

    private final ObservableList<String> friendsReqSentObs = FXCollections.observableArrayList();


    @FXML
    public void setService(Service serviceUsers) {
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

    /**
     * Initializes the application with user-specific data, including messages, friends, and friend requests.
     *
     * @param user The user for whom the application is initialized.
     */
    public void initApp(User user) {
        // Set the current user
        this.user = user;

        // Set the username label with the user's full name
        username.setText(user.getFirstName() + " " + user.getLastName());

        // Clear and populate the list of messages
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

        // Clear received friend requests
        friendsReqObs.clear();

        // Add received friend requests to the observable list
        StreamSupport.stream(service.printFriendships().spliterator(), false)
                .filter(f -> f instanceof FriendShip)
                .map(f -> (FriendShip) f)
                .filter(friendShip -> friendShip.getUser2().getId().equals(user.getId()))
                .forEach(friendShip -> friendsReqObs.add(
                        friendShip.getUser1().getFirstName() + " " + friendShip.getUser1().getLastName() +
                                " " + friendShip.getDate() +
                                " " + friendShip.getAcceptance()));

        // Clear sent friend requests
        friendsReqSentObs.clear();

        // Add sent friend requests to the observable list
        StreamSupport.stream(service.printFriendships().spliterator(), false)
                .filter(f -> f instanceof FriendShip)
                .map(f -> (FriendShip) f)
                .filter(friendShip -> friendShip.getUser1().getId().equals(user.getId()))
                .forEach(friendShip -> friendsReqSentObs.add(
                        friendShip.getUser2().getFirstName() + " " + friendShip.getUser2().getLastName() +
                                " " + friendShip.getDate() +
                                " " + friendShip.getAcceptance()));
    }

    /**
     * Initializes the data model, updating the page with user-specific data.
     */
    private void initModel() {
        // Obține valoarea selectată pe Slider pentru dimensiunea paginii
        int customPageSize = (int) pageSizeSlider.getValue();

        Page<User> page = service.findAll(new Pageable(currentPage, customPageSize));

        int maxPage = (int) Math.ceil((double) page.getTotalElementCount() / customPageSize);
        if (maxPage == -1) {
            maxPage = 0;
        }
        if (currentPage > maxPage) {
            currentPage = maxPage;
            page = service.findAll(new Pageable(currentPage, customPageSize));
        }

        users.setAll(StreamSupport.stream(page.getElementsOnPage().spliterator(), false).collect(Collectors.toList()));
        totalNumberOfElements = page.getTotalElementCount();

        previousButton.setDisable(currentPage == 0);
        nextButton.setDisable((currentPage + 1) * customPageSize >= totalNumberOfElements);
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

    /**
     * Initializes the UI components and sets up event listeners.
     */
    @FXML
    private void initialize() {
        usersTable.setItems(users);
        usersTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        friendsRequestList.setItems(friendsReqObs);
        friendRequestsSent.setItems(friendsReqSentObs);
        friendsList.setItems(friendsObs);
        messagesFriendList.setItems(listaMesaje);
        pageSizeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Aici poți reacționa la schimbarea valorii slider-ului
            // și apela metoda initModel pentru a reactualiza pagina
            initModel();
        });
    }


    @FXML
    private void addUser(ActionEvent actionEvent) {
        try {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();

            // Validate inputs
            if (firstName.isEmpty() || lastName.isEmpty()) {
                MessageAlert.showErrorMessage(null, "First name and last name cannot be empty.");
                return;

            }
            if (email.isEmpty()) {
                MessageAlert.showErrorMessage(null, "Email cannot be empty.");
                return;
            }

            if (password.isEmpty()) {
                MessageAlert.showErrorMessage(null, "Password cannot be empty.");
                return;
            }
            service.addUser(firstName, lastName, email, password);
            clearForm();
            usersTable.refresh();
            initModel();
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "User Added", "User successfully added.");
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, "An error occurred: " + e.getMessage());
        }
        usersTable.refresh();

    }


    /**
     * Clears the input fields in the user form.
     */
    private void clearForm() {
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        passwordField.clear();
    }

    /**
     * Event handler for the "Delete User" button.
     * Deletes the selected user from the system.
     */
    @FXML
    private void deleteUser(ActionEvent actionEvent) {
        try {
            User selectedUser = usersTable.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                String firstName = selectedUser.getFirstName();
                String lastName = selectedUser.getLastName();
                service.deleteUser(firstName, lastName);
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

    /**
     * Event handler for the "Update User" button.
     * Updates the information of the selected user based on the input fields.
     */
    @FXML
    private void updateUser(ActionEvent actionEvent) {
        try {
            User selectedUser = usersTable.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                String newFirstName = firstNameField.getText();
                String newLastName = lastNameField.getText();
                String newEmail = emailField.getText();
                UUID id = selectedUser.getId();
                service.updateUser(id, newFirstName, newLastName, newEmail);
                clearForm();
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

    /**
     * Event handler for the "Generate Users" button.
     * Clears all existing data and adds new entities to the system.
     */
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

    /**
     * Updates the UI and data model when a user change event occurs.
     *
     * @param t The user change event.
     */
    @Override
    public void update(UserChangeEvent t) {
        initModel();
        initApp(this.user);
    }

    /**
     * Event handler for the "Previous" button in pagination.
     * Moves to the previous page and updates the data model.
     */
    public void onPrevious(ActionEvent actionEvent) {
        currentPage--;
        initModel();
    }

    /**
     * Event handler for the "Next" button in pagination.
     * Moves to the next page and updates the data model.
     */
    public void onNext(ActionEvent actionEvent) {
        currentPage++;
        initModel();
    }

    /**
     * Event handler for the "Enter Messages" button.
     * Opens an alert dialog for messages.
     */
    public void enterMessages(ActionEvent event) {
        Alert a = new Alert(Alert.AlertType.NONE);
        a.show();
    }

    /**
     * Event handler for sending messages.
     * Sends a message when the Enter key is pressed.
     * Shows an information alert after sending.
     */
    public void sendMessage(KeyEvent event) {
        if (event.getCode() != KeyCode.ENTER)
            return;

        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Message", "Message successfully sent.");


    }

    /**
     * Event handler for the "Show Conversations" button.
     * Shows the conversation history between the currently logged-in user and the selected user.
     * Displays the conversation in a secondary window.
     */
    @FXML
    private void afisareConversatii(ActionEvent actionEvent) {
        UUID from = user.getId();
        try {
            User to = usersTable.getSelectionModel().getSelectedItem();
            UUID id_to = null;
            if (to != null) {
                id_to = to.getId();
            }
            System.out.println("From: " + from);
            System.out.println("To: " + id_to);

            String str = messageService.conversation(from, id_to).stream()
                    .map(tup -> tup.getFrom().getLastName() + " " + tup.getFrom().getFirstName() + ": " +
                            tup.getMessage() + "\nTrimis la: " +
                            tup.getData().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)) + '\n')
                    .collect(Collectors.joining("\n"));

            openSecondaryWindow(str);

        } catch (Exception e) {
            e.printStackTrace();
            MessageAlert.showErrorMessage(null, "An error occurred: " + e.getMessage());
        }
    }

    /**
     * Event handler for the "Add Message" button.
     * Adds a message to selected users based on the input message field.
     * Shows an information alert after adding the message.
     */
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
            System.out.println("Selected users: " + selectedUsers);
            System.out.println("Mesaj: " + mesaj);
            messageService.addMessage(user, id_toList, mesaj);

            getMesajField.clear();
            initApp(this.user);
        } catch (Exception e) {
            e.printStackTrace();
            MessageAlert.showErrorMessage(null, "An error occurred: " + e.getMessage());
        }
    }


    /**
     * Opens a secondary window to display the provided text.
     *
     * @param deAfisat The text to be displayed in the secondary window.
     */
    private void openSecondaryWindow(String deAfisat) {
        // Create a new stage for the secondary window
        Stage secondaryStage = new Stage();
        secondaryStage.setTitle("Fereastra Secundară");

        // Create a label with the provided text
        javafx.scene.control.Label label = new javafx.scene.control.Label(deAfisat + "\n\n\n");

        // Layout for the secondary window
        StackPane secondaryLayout = new StackPane();
        secondaryLayout.getChildren().add(label);

        // Set the scene for the secondary window
        Scene secondaryScene = new Scene(secondaryLayout);
        secondaryStage.setScene(secondaryScene);
        secondaryStage.setWidth(300);

        // Show the secondary window
        secondaryStage.show();
    }

    /**
     * Event handler for sending friend requests.
     * Sends a friend request to the selected user and handles various scenarios.
     * Shows information or warning alerts based on the outcome.
     */
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


    /**
     * Event handler for accepting friend requests.
     * Accepts the selected friend request and updates the UI accordingly.
     * Shows an information alert after accepting the friend request.
     */
    public void acceptFriendRequest(ActionEvent actionEvent) {
        try {
            if (friendsRequestList.getSelectionModel().getSelectedItem() == null)
                return;

            // Get information about the selected friend request
            String userInfo = friendsRequestList.getSelectionModel().getSelectedItem().toString();
            String firstnameU2 = userInfo.split(" ")[0];
            String lastnameU2 = userInfo.split(" ")[1];
            String status = userInfo.split(" ")[3];

            // Check if the friend request is in PENDING status
            if (!status.equals("PENDING"))
                return;
            String FirstNameU1 = user.getFirstName();
            String LastNameU1 = user.getLastName();

            // Remove the existing friendship and add the new friendship
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

    /**
     * Event handler for declining friend requests.
     * Declines the selected friend request and updates the UI accordingly.
     * Shows an information alert after declining the friend request.
     */
    public void declineFriendRequest(ActionEvent actionEvent) {
        try {
            if (friendsRequestList.getSelectionModel().getSelectedItem() == null)
                return;

            // Get information about the selected friend request
            String userInfo = friendsRequestList.getSelectionModel().getSelectedItem().toString();
            String firstnameU2 = userInfo.split(" ")[0];
            String lastnameU2 = userInfo.split(" ")[1];
            String status = userInfo.split(" ")[3];
            String FirstNameU1 = user.getFirstName();
            String LastNameU1 = user.getLastName();

            // Check if the friend request is in PENDING status
            if (!status.equals("PENDING"))
                return;

            // Remove the existing friendship and decline the friend request
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

    /**
     * Event handler for canceling friend requests.
     * Cancels the selected friend request and updates the UI accordingly.
     * Shows an information alert after canceling the friend request.
     */
    public void cancelFriendRequest(ActionEvent actionEvent) {
        try {
            if (friendRequestsSent.getSelectionModel().getSelectedItem() == null)
                return;

            String userInfo = friendRequestsSent.getSelectionModel().getSelectedItem().toString();
            String firstnameU2 = userInfo.split(" ")[0];
            String lastnameU2 = userInfo.split(" ")[1];
            String status = userInfo.split(" ")[3];

            // Check if the friend request is in PENDING status
            if (!status.equals("PENDING"))
                return;

            // Remove the existing friendship and cancel the friend request
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
