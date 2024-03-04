Social Network App!

The Social Network App is a powerful and user-friendly platform designed to connect individuals and foster meaningful interactions. With robust features and secure authentication, this application provides a seamless experience for managing friendships, including friend requests and messaging. It also offers efficient user account management, allowing users to create, update, and delete accounts hassle-free.
Getting Started
Prerequisites

    Java JDK: Download
    PostgreSQL: Download

Build and Run

    Open the project in your preferred Java IDE.
    Configure the database connection in application.properties.
    Run the HelloApplication class to start the application.

Usage

    Navigate to the login screen.
    Log in with existing credentials.
    Manage friends, send/receive friend requests, and communicate through messaging.

Technologies Used

    JavaFX: Graphical user interface.
    ControlsFX: UI controls library.
    FormsFX: Form creation library.
    BootstrapFX: Bootstrap-themed components.
    PostgreSQL: Database for user and friendship data.

Project Structure

    com.socialnetwork.lab78: Main application package.
    com.socialnetwork.lab78.controller: Holds controllers responsible for managing user interface interactions(LogInController, UserController)
    com.socialnetwork.lab78.domain: Defines domain classes representing core entities in the application - User, Friendship, Entity, Message etc.).
    com.socialnetwork.lab78.repository:Contains classes responsible for interacting with the data storage layer { UserDBRepository/FriendShipDBRepository/MessageDBRepository }
    com.socialnetwork.lab78.service: Implements business logic services that orchestrate operations based on user interactions.
    com.socialnetwork.lab78.paging: Contains classes related to paging logic for displaying data in manageable chunks.
    com.socialnetwork.lab78.validators:Holds classes responsible for validating user input and ensuring data integrity.
    com.socialnetwork.lab78.utils.observer: Includes interfaces and classes for implementing the observer pattern.
    com.socialnetwork.lab78.controller: UI controllers.





![Fără titlu](https://github.com/PopoviciGeorgeOctavian/SocialNetworkApplication/assets/116513072/607ef814-ab49-43c6-b554-fd0e419ca9a3)
![Friend Request Sent](https://github.com/PopoviciGeorgeOctavian/SocialNetworkApplication/assets/116513072/8a220ca4-5095-4a81-8ac1-5437dade7853)
![Friend Request Pending](https://github.com/PopoviciGeorgeOctavian/SocialNetworkApplication/assets/116513072/bdcc624b-30d2-4ff4-a2a6-310dfb6243ad)
![Friend Request Sent](https://github.com/PopoviciGeorgeOctavian/SocialNetworkApplication/assets/116513072/eb7b8584-aff6-4153-be3d-98fe61f31b84)
![Conversations](https://github.com/PopoviciGeorgeOctavian/SocialNetworkApplication/assets/116513072/ac69d8e6-cde8-4d89-be58-71bbaa61bd55)
![Messages](https://github.com/PopoviciGeorgeOctavian/SocialNetworkApplication/assets/116513072/34fe8ee2-1fc7-49dc-888b-4131bcea9c04)
![Databases](https://github.com/PopoviciGeorgeOctavian/SocialNetworkApplication/assets/116513072/ee035092-7671-498c-9ad5-dbfc857f3aef)

