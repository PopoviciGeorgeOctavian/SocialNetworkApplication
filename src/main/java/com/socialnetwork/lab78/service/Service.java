package com.socialnetwork.lab78.service;


import com.socialnetwork.lab78.Paging.Page;
import com.socialnetwork.lab78.Paging.Pageable;
import com.socialnetwork.lab78.Paging.PagingRepository;
import com.socialnetwork.lab78.domain.Entity;
import com.socialnetwork.lab78.domain.FriendRequest;
import com.socialnetwork.lab78.domain.FriendShip;
import com.socialnetwork.lab78.domain.User;
import com.socialnetwork.lab78.exception.ServiceException;
import com.socialnetwork.lab78.repository.Repository;
import com.socialnetwork.lab78.utils.observer.Observable;
import com.socialnetwork.lab78.utils.observer.Observer;
import com.socialnetwork.lab78.utils.observer.UserChangeEvent;
import com.socialnetwork.lab78.validators.ValidationException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.socialnetwork.lab78.domain.FriendRequest.ACCEPTED;

/**
 * Service class provides the main business logic for managing users and friendships in the social network application.
 */
public class Service implements Observable<UserChangeEvent> {

    private PagingRepository<UUID, User> UserRepo;
    private Repository<UUID, FriendShip> FriendShipRepo;
    final private List<Observer<UserChangeEvent>> observers = new ArrayList<>();

    /**
     * Constructs a new Service with the specified repositories for users and friendships.
     *
     * @param UserRepo       The repository for users.
     * @param FriendShipRepo The repository for friendships.
     */
    public Service(PagingRepository<UUID, User> UserRepo, Repository<UUID, FriendShip> FriendShipRepo) {
        this.UserRepo = UserRepo;
        this.FriendShipRepo = FriendShipRepo;
    }

    /**
     * Retrieves a page of users based on the provided pageable information.
     *
     * @param pageable The pageable information.
     * @return A page of users.
     */
    public Page<User> findAll(Pageable pageable) {
        return UserRepo.findAll(pageable);
    }

    /**
     * Adds a new user to the system with the specified details.
     *
     * @param nume     The first name of the user.
     * @param prenume  The last name of the user.
     * @param email    The email of the user.
     * @param password The password of the user.
     */
    public void addUser(String nume, String prenume, String email, String password) {
        try {
            User u = new User(nume, prenume, email, password);
            UserRepo.save(u);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * Deletes a user and their friendships based on the provided first name and last name.
     *
     * @param nume    The first name of the user.
     * @param prenume The last name of the user.
     */
    public void deleteUser(String nume, String prenume) {
        try {
            User u = getUserByNumePrenume(nume, prenume);
            if (u == null) {
                System.err.println("Nu exista nici un user cu acest nume si prenume!");
                return;
            }
            Iterable<FriendShip> friendships = FriendShipRepo.findAll();
            for (FriendShip friendship : friendships) {
                if (friendship.getUser1().equals(u) || friendship.getUser2().equals(u)) {
                    FriendShipRepo.delete(friendship.getId());
                }
            }
            UserRepo.delete(u.getId());
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * Retrieves all users in the system.
     *
     * @return An iterable of all users.
     */
    public Iterable<User> getAllUseri() {
        return UserRepo.findAll();
    }

    /**
     * Retrieves a list of all users.
     *
     * @return A list of all users.
     */
    public List<User> printUsers() {
        ArrayList<User> lista = new ArrayList<User>();
        UserRepo.findAll().forEach(lista::add);
        return lista;
    }

    /**
     * Retrieves all friendships in the system.
     *
     * @return An iterable of all friendships.
     */
    public Iterable<FriendShip> printFriendships() {
        return FriendShipRepo.findAll();
    }

    /**
     * Retrieves a list of all friendships.
     *
     * @return A list of all friendships.
     */
    public List<FriendShip> printPrieteniile() {
        ArrayList<FriendShip> lista = new ArrayList<>();
        FriendShipRepo.findAll().forEach(lista::add);
        return lista;
    }

    /**
     * Adds a new friendship between users with the specified first and last names.
     *
     * @param n1 The first name of the first user.
     * @param p1 The last name of the first user.
     * @param n2 The first name of the second user.
     * @param p2 The last name of the second user.
     */
    public void addFriendShip(String n1, String p1, String n2, String p2) {
        try {
            User u1 = getUserByNumePrenume(n1, p1);
            User u2 = getUserByNumePrenume(n2, p2);
            System.out.println("User1: " + u1 + "User2: " + u2);
            if (u1 == null || u2 == null || u1.equals(u2))
                throw new ValidationException("Acesti useri nu sunt buni");
            var FriendShip = new FriendShip(u1, u2, FriendRequest.ACCEPTED);
            FriendShipRepo.save(FriendShip);
            u1.addFriend(u2);
            u2.addFriend(u1);

        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * Retrieves the friendship of a user.
     *
     * @param u The user.
     * @return The friendship of the user.
     */
    public FriendShip getFriendShipByUser(User u) {
        return printPrieteniile().stream()
                .filter(p -> p.getUser1().equals(u) || p.getUser2().equals(u))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves a user by their first name and last name.
     *
     * @param nume    The first name of the user.
     * @param prenume The last name of the user.
     * @return The user with the specified first name and last name.
     */
    public User getUserByNumePrenume(String nume, String prenume) {
        return printUsers().stream()
                .filter(u -> u.getFirstName().equals(nume) && u.getLastName().equals(prenume))
                .findFirst()
                .orElse(null);
    }

    /**
     * Removes a friendship between users with the specified first and last names.
     *
     * @param n1 The first name of the first user.
     * @param p1 The last name of the first user.
     * @param n2 The first name of the second user.
     * @param p2 The last name of the second user.
     */
    public void removeFriendShip(String n1, String p1, String n2, String p2) {
        User u1 = getUserByNumePrenume(n1, p1);
        User u2 = getUserByNumePrenume(n2, p2);
        Iterable<FriendShip> prt = FriendShipRepo.findAll();
        for (FriendShip p : prt) {
            if (p.getUser1().equals(u1) && p.getUser2().equals(u2)) {
                System.out.println("Found");
                FriendShipRepo.delete(p.getId());
                break;
            }
        }
    }

    /**
     * Updates the details of a user with the specified ID.
     *
     * @param id           The ID of the user to be updated.
     * @param newFirstName The new first name.
     * @param newLastName  The new last name.
     * @param newEmail     The new email.
     * @throws Exception if the user with the given ID is not found.
     */
    public void updateUser(UUID id, String newFirstName, String newLastName, String newEmail) throws Exception {
        Optional<User> oldUserOptional = UserRepo.findOne(id);

        if (oldUserOptional.isPresent()) {
            User oldUser = oldUserOptional.get();

            // Update user in the repository
            User newUser = new User(id, newFirstName, newLastName, newEmail);
            UserRepo.update(newUser);

            // Update friendships referencing the old user
            Iterable<FriendShip> friendships = FriendShipRepo.findAll();
            for (FriendShip friendship : friendships) {
                if (friendship.getUser1().equals(oldUser)) {
                    friendship.setUser1(newUser);
                    friendship.setDate(LocalDateTime.now());
                    FriendShipRepo.update(friendship);
                }
                if (friendship.getUser2().equals(oldUser)) {
                    friendship.setUser2(newUser);
                    friendship.setDate(LocalDateTime.now());
                    FriendShipRepo.update(friendship);
                }
            }
        } else {
            // Handle the case where the user with the given ID is not found
            throw new ServiceException("User not found with ID: " + id);

        }
    }

    /**
     * Performs Depth-First Search (DFS) starting from a given user and marks visited users.
     *
     * @param u   The starting user for DFS.
     * @param set A map to track visited users.
     * @return A list of users traversed during DFS.
     */
    private List<User> DFS(User u, Map<UUID, Boolean> set) {
        List<User> list = new ArrayList<>();
        list.add(u);
        set.put(u.getId(), true);

        for (User f : u.getFriends()) {
            if (!set.containsKey(f.getId())) {
                List<User> l = DFS(f, set);
                list.addAll(l);
            }
        }
        return list;
    }

    /**
     * Counts the number of communities using Depth-First Search (DFS).
     *
     * @return The number of communities in the graph.
     */
    public int numberOfCommunities() {
        Iterable<User> it = UserRepo.findAll();
        Map<UUID, Boolean> set = new HashMap<>();
        int count = 0;
        for (User u : it) {
            if (!set.containsKey(u.getId())) {
                ++count;
                DFS(u, set);
            }
        }
        return count;
    }

    /**
     * Finds the most sociable community using DFS and longest path calculation.
     *
     * @return A list of communities that are most sociable.
     */
    public Iterable<Iterable<User>> mostSociableCommunity() {
        List<Iterable<User>> list = new ArrayList<>();
        Iterable<User> it = UserRepo.findAll();
        Map<UUID, Boolean> set = new HashMap<>();

        int max = -1;
        for (User u : it)
            if (!set.containsKey(u.getId())) {
                List<User> aux = DFS(u, set);
                int l = longestPath(aux);
                if (l > max) {
                    list = new ArrayList<>();
                    list.add(aux);
                    max = l;
                } else if (l == max)
                    list.add(aux);
            }

        return list;
    }

    /**
     * Calculates the longest path in a list of nodes.
     *
     * @param nodes The list of nodes.
     * @return The length of the longest path.
     */
    private int longestPath(List<User> nodes) {
        int max = 0;
        for (User u : nodes) {
            int l = longestPathFromSource(u);
            if (max < l)
                max = l;
        }
        return max;
    }

    /**
     * Calculates the longest path from a source user using Breadth-First Search (BFS).
     *
     * @param source The source user.
     * @return The length of the longest path from the source.
     */
    private int longestPathFromSource(User source) {
        Map<UUID, Boolean> set = new HashMap<>();
        return BFS(source, set);
    }

    /**
     * Performs Breadth-First Search (BFS) and calculates the maximum path length.
     *
     * @param source The source user.
     * @param set    A map to track visited users.
     * @return The maximum path length from the source.
     */
    private int BFS(User source, Map<UUID, Boolean> set) {
        int max = -1;
        for (User f : source.getFriends())
            if (!set.containsKey(f.getId())) {
                set.put(f.getId(), true);
                int l = BFS(f, set);
                if (l > max)
                    max = l;
                set.remove(f.getId());
            }

        return max + 1;
    }

    /**
     * Adds randomly generated users and friendships to the repository.
     */
    public void addEntities() {
        String[] listaNume = {"Popescu", "Ionescu", "Dumitru", "Stancu", "Radu", "Mihai", "Neagu", "Dragomir", "Bălan", "Enescu"};
        String[] listaPrenume = {"Ana", "Ion", "Maria", "Mihai", "Elena", "Victor", "Adriana", "Alexandru", "Andreea", "Gabriel"};

        Random rand = new Random();

        for (int i = 0; i < 10; i++) {
            int indexNume = rand.nextInt(listaNume.length);
            int indexPrenume = rand.nextInt(listaPrenume.length);

            String firstName = listaPrenume[indexPrenume].trim(); // Trim the first name
            String lastName = listaNume[indexNume].trim(); // Trim the last name
            String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@yahoo.com";
            String password = generateRandomPassword();

            User user = new User(firstName, lastName, email, password);
            UserRepo.save(user);
        }
        List<User> users = printUsers(); // Retrieve existing users

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);

            // Add friendships for each user
            for (int j = i + 1; j < users.size(); j++) {
                User friend = users.get(j);

                // Check if they are not already friends before making a new friendship
                if (!areAlreadyFriends(user, friend)) {
                    System.out.println("Added friendship between " + user.getFirstName() + " " + user.getLastName() +
                            " and " + friend.getFirstName() + " " + friend.getLastName());

                    addFriendShip(user.getFirstName(), user.getLastName(), friend.getFirstName(), friend.getLastName());
                }
            }
        }
    }

    /**
     * Generates a random password (placeholder method).
     *
     * @return A randomly generated password.
     */
    private String generateRandomPassword() {
        // Logic to generate a random password (you can customize this based on your requirements)
        return "Pass";
    }

    /**
     * Retrieves users with a minimum number of friendships.
     *
     * @param n The minimum number of friendships.
     * @return A list of users with at least 'n' friendships.
     */
    public List<User> numarMinimPrieteni(int n) {
        Iterable<User> users = UserRepo.findAll();

        List<User> usersWithMinFriends = StreamSupport.stream(users.spliterator(), false)
                .filter(u -> (int) StreamSupport.stream(FriendShipRepo.findAll().spliterator(), false)
                        .filter(f -> f.getUser1().equals(u) || f.getUser2().equals(u))
                        .count() >= n)
                .collect(Collectors.toList());

        return usersWithMinFriends;
    }

    /**
     * Retrieves accepted friends for a given user.
     *
     * @param user The user to find accepted friends for.
     * @return A list of accepted friends for the given user.
     */

    public List<User> getAcceptedFriendsOfUser(User user) {
        // Getting all friendships
        Iterable<FriendShip> friendships = FriendShipRepo.findAll();

        // Filtering friendships to obtain only those ACCEPTED where the given user is involved
        List<User> acceptedFriends = StreamSupport.stream(friendships.spliterator(), false)
                .filter(friendship ->
                        (friendship.getUser1().equals(user) || friendship.getUser2().equals(user))
                                && friendship.getAcceptance() == FriendRequest.ACCEPTED)
                .flatMap(friendship -> Stream.of(friendship.getUser1(), friendship.getUser2()))
                .distinct()
                .collect(Collectors.toList());

        return acceptedFriends;
    }

    /**
     * Retrieves the number of friendships for a given user.
     * @param user The user to count friendships for.
     * @return The number of friendships for the given user.
     */
    public int numarPrieteni(User user) {
        return (int) StreamSupport.stream(FriendShipRepo.findAll().spliterator(), false)
                .filter(f -> f.getUser1().equals(user) || f.getUser2().equals(user))
                .count();
    }

    /**
     * Retrieves friendships for a given user in a specific month
     * @param n The last name of the user.
     * @param p The first name of the user.
     * @param month The month to filter friendships.
     * @return A list of friends for the user in the specified month.
     */
    public List<User> FriendsFromFunction(String n, String p, Integer month) {
        ArrayList<User> users = new ArrayList<>();
        Iterable<FriendShip> prt = FriendShipRepo.findAll();
        User u = getUserByNumePrenume(n, p);
        for (FriendShip prtt : prt) {
            if (prtt.getUser1().equals(u) && prtt.getDate().getMonth().getValue() == month) {
                users.add(prtt.getUser2());
            }
            if (prtt.getUser2().equals(u) && prtt.getDate().getMonth().getValue() == month) {
                users.add(prtt.getUser1());
            }
        }
        return users;
    }


    /**
     * Retrieves users with a first name containing a specific string.
     *
     * @param find The string to search for in user first names.
     * @return A list of users with first names containing the specified string.
     */
    public List<User> stringSearch(String find) {
        ArrayList<User> users = new ArrayList<>();
        Iterable<User> usr = UserRepo.findAll();
        for (User u : usr) {
            if (u.getFirstName().contains(find)) {
                users.add(u);
            }
        }
        return users;
    }


    /**
     * Adds an observer for user change events.
     *
     * @param o The observer to be added.
     */
    @Override
    public void addObserver(Observer<UserChangeEvent> o) {
        observers.add(o);

    }

    /**
     * Removes an observer for user change events.
     *
     * @param o The observer to be removed.
     */
    @Override
    public void removeObserver(Observer<UserChangeEvent> o) {
        observers.remove(o);

    }

    /**
     * Notifies all registered observers about a user change event.
     *
     * @param t The user change event.
     */
    @Override
    public void notify(UserChangeEvent t) {
        observers.forEach(o -> o.update(t));

    }

    /**
     * Accepts a friendship request between two pairs of users.
     *
     * @param n1, p1 The first user's first and last names.
     * @param n2, p2 The second user's first and last names.
     */
    public void acceptFriendship(String n1, String p1, String n2, String p2) {
        removeFriendShip(n1, p1, n2, p2);
        addFriendShip(n1, p1, n2, p2);
    }

    /**
     * Declines a friendship request between two pairs of users.
     *
     * @param n1, p1 The first user's first and last names.
     * @param n2, p2 The second user's first and last names.
     */
    public void declineFriendRequest(String n1, String p1, String n2, String p2) {
        removeFriendShip(n1, p1, n2, p2);

        try {
            User u1 = getUserByNumePrenume(n1, p1);
            User u2 = getUserByNumePrenume(n2, p2);

            // Check if the users are valid and not the same
            if (u1 == null || u2 == null || u1.equals(u2)) {
                throw new ValidationException("Acesti useri nu sunt buni");
            }

            // Check if they are already friends
            if (areAlreadyFriends(u1, u2)) {
                throw new ValidationException("Users are already friends");
            }

            // Create a new friendship with REJECTED status
            var friendShip = new FriendShip(u1, u2, FriendRequest.REJECTED);
            FriendShipRepo.save(friendShip);

        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * Creates a friendship request between two pairs of users.
     *
     * @param n1, p1 The first user's first and last names.
     * @param n2, p2 The second user's first and last names.
     */
    public void createFriendRequest(String n1, String p1, String n2, String p2) {
        try {
            User u1 = getUserByNumePrenume(n1, p1);
            User u2 = getUserByNumePrenume(n2, p2);

            // Check if the users are valid and not the same
            if (u1 == null || u2 == null || u1.equals(u2)) {
                throw new ValidationException("Acesti useri nu sunt buni");
            }

            // Check if they are already friends
            if (areAlreadyFriends(u1, u2)) {
                throw new ValidationException("Users are already friends");
            }

            // Create a new friendship
            var friendShip = new FriendShip(u1, u2, FriendRequest.PENDING);
            FriendShipRepo.save(friendShip);

        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * Checks if two users are already friends based on existing friendships.
     *
     * @param u1 The first user.
     * @param u2 The second user.
     * @return True if users are already friends, false otherwise.
     */
    public boolean areAlreadyFriends(User u1, User u2) {
        // Retrieve existing friendships from the repository
        Iterable<FriendShip> existingFriendships = FriendShipRepo.findAll();

        // Check if there is a friendship with the specified users and status ACCEPTED
        for (FriendShip friendship : existingFriendships) {
            if ((friendship.getUser1().equals(u1) && friendship.getUser2().equals(u2))
                    || (friendship.getUser1().equals(u2) && friendship.getUser2().equals(u1))) {
                if (friendship.getAcceptance() == FriendRequest.ACCEPTED) {
                    return true; // Users are already friends
                }
            }
        }

        return false; // Users are not already friends
    }

    /**
     * Checks if there are pending friend requests between two users.
     *
     * @param u1 The first user.
     * @param u2 The second user.
     * @return True if there are pending friend requests, false otherwise.
     */
    public boolean areFriendRequestsPending(User u1, User u2) {
        // Retrieve existing friend requests from the repository
        Iterable<FriendShip> existingFriendRequests = FriendShipRepo.findAll();

        // Check if there is a friend request with the specified users and status PENDING
        for (FriendShip friendRequest : existingFriendRequests) {
            if ((friendRequest.getUser1().equals(u1) && friendRequest.getUser2().equals(u2))
                    || (friendRequest.getUser1().equals(u2) && friendRequest.getUser2().equals(u1))) {
                if (friendRequest.getAcceptance() == FriendRequest.PENDING) {
                    return true; // There is a pending friend request
                }
            }
        }

        return false; // There are no pending friend requests
    }

    /**
     * Retrieves a user based on their email address.
     *
     * @param email The email address of the user.
     * @return The user corresponding to the given email or null if not found.
     */
    public User getUserByEmail(String email) {
        return printUsers().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }
}
