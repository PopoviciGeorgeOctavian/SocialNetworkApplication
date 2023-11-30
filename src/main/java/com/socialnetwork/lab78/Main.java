package com.socialnetwork.lab78;



import com.socialnetwork.lab78.Paging.PagingRepository;
import com.socialnetwork.lab78.domain.FriendShip;
import com.socialnetwork.lab78.domain.User;
import com.socialnetwork.lab78.repository.FriendShipDBRepository;
import com.socialnetwork.lab78.repository.Repository;
import com.socialnetwork.lab78.repository.UserDBRepository;
import com.socialnetwork.lab78.service.Service;
import com.socialnetwork.lab78.ui.UI;

import java.util.UUID;

public class Main {
    public static void main(String[] args) {

        PagingRepository<UUID, User> userRepo = new UserDBRepository("jdbc:postgresql://localhost:5432/socialnetwork", "postgres", "George100");
        Repository<UUID, FriendShip> friendshipRepo = new FriendShipDBRepository("jdbc:postgresql://localhost:5432/socialnetwork", "postgres", "George100");
        Service service = new Service(userRepo, friendshipRepo);

        /*
        service.addUser("FName","LName");
        service.addUser("Test","Testt");
        service.addFriendShip("FName","LName","Test","Testt");
        Iterable<User> utz = service.printUserii();
        for(User u:utz){
            System.out.println(u);
        }
        //InMemoryRepository<UUID, User> UserRepo = new InMemoryRepository<>();
       // InMemoryRepository<UUID, FriendShip> FriendShipRepo = new InMemoryRepository<>();
       // Service service = new Service(UserRepo, FriendShipRepo);
       // Service service = new Service(userRepo, FriendShipRepo);
        */
        UI ui = new UI(service);
        ui.run();

        System.out.println("Done");
    }
}