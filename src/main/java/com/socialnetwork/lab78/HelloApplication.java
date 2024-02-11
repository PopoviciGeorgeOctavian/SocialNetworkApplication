package com.socialnetwork.lab78;

import com.socialnetwork.lab78.Paging.PagingRepository;
import com.socialnetwork.lab78.controller.LogInController;
import com.socialnetwork.lab78.controller.UserController;
import com.socialnetwork.lab78.domain.FriendShip;
import com.socialnetwork.lab78.domain.Message;
import com.socialnetwork.lab78.domain.User;
import com.socialnetwork.lab78.repository.FriendShipDBRepository;
import com.socialnetwork.lab78.repository.MessageDBRepository;
import com.socialnetwork.lab78.repository.Repository;
import com.socialnetwork.lab78.repository.UserDBRepository;
import com.socialnetwork.lab78.service.MessageService;
import com.socialnetwork.lab78.service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.UUID;

/**
 * The HelloApplication class serves as the entry point for the JavaFX application.
 * It initializes the necessary repositories, services, and controllers for user authentication and interaction.
 */
public class HelloApplication extends Application  {

    /**
     * The start method is called when the JavaFX application is launched.
     *
     * @param stage The primary stage for the application.
     * @throws Exception If an error occurs during the application startup.
     */
    @Override
    public void start(Stage stage) throws Exception {
        // Initialize repositories
        UserDBRepository userRepo = new UserDBRepository("jdbc:postgresql://localhost:5432/socialnetwork", "postgres", "George100");
        Repository<UUID, FriendShip> friendshipRepo = new FriendShipDBRepository("jdbc:postgresql://localhost:5432/socialnetwork", "postgres", "George100");
        Repository<UUID, Message> messageRepository = new MessageDBRepository("jdbc:postgresql://localhost:5432/socialnetwork", "postgres", "George100", userRepo);

        // Initialize services
        Service service = new Service(userRepo, friendshipRepo);
        MessageService messageService = new MessageService(messageRepository, userRepo);

        try {
            // Load the LogIn.fxml view
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/socialnetwork/lab78/views/LogIn.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            // Set up the LogInController with the initialized services
            LogInController controller = fxmlLoader.getController();
            controller.setService(service);
            controller.setMessageService(messageService);

            // Set up the primary stage
            stage.setTitle("App");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The main method is the entry point of the application.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch();
    }
}
