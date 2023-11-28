package com.socialnetwork.lab78.service;





import com.socialnetwork.lab78.domain.FriendRequest;
import com.socialnetwork.lab78.domain.FriendShip;
import com.socialnetwork.lab78.domain.User;
import com.socialnetwork.lab78.exception.ServiceException;
import com.socialnetwork.lab78.repository.Repository;
import com.socialnetwork.lab78.utils.observer.Observable;
import com.socialnetwork.lab78.utils.observer.Observer;
import com.socialnetwork.lab78.utils.observer.UserChangeEvent;
import com.socialnetwork.lab78.validators.ValidationException;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.socialnetwork.lab78.domain.FriendRequest.ACCEPTED;

public class Service implements Observable<UserChangeEvent> {

    //private InMemoryRepository<UUID, User> UserRepo;
    private Repository<UUID, User> UserRepo;
    //private InMemoryRepository<UUID, FriendShip> FriendShipRepo;
    private Repository<UUID, FriendShip> FriendShipRepo;

    //Constructor
    // public Service(InMemoryRepository<UUID, User> UserRepo, InMemoryRepository<UUID, FriendShip> FriendShipRepo) {
    public Service(Repository<UUID, User> UserRepo, Repository<UUID, FriendShip> FriendShipRepo) {
        this.UserRepo = UserRepo;
        this.FriendShipRepo = FriendShipRepo;
    }

    //Adauga user
    public void addUser(String nume, String prenume) {
        try {


            User u = new User(nume, prenume);
            UserRepo.save(u);
        }catch (Exception e) {
            System.err.println(e);
        }
    }

    //Sterge user si prieteniile acestuia
    public void deleteUser(String nume, String prenume) {
        try{
            User u = getUserByNumePrenume(nume, prenume);
            if(u == null) {
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
        }
        catch (Exception e) {
            System.err.println(e);
        }
    }



    public Iterable<User> getAllUseri() {
        return UserRepo.findAll();
    }


    public List<User> printUsers() {
        ArrayList<User> lista = new ArrayList<User>();
        UserRepo.findAll().forEach(lista::add);
        return lista;
    }

    public Iterable<FriendShip> printFriendships() {
        return FriendShipRepo.findAll();
    }

    public List<FriendShip> printPrieteniile() {
        ArrayList<FriendShip> lista = new ArrayList<>();
        FriendShipRepo.findAll().forEach(lista::add);
        return lista;
    }

    //Adauga FriendShip
    public void addFriendShip(String n1, String p1, String n2, String p2) {
        try{
            User u1 = getUserByNumePrenume(n1, p1);
            User u2 = getUserByNumePrenume(n2, p2);
            if(u1 == null || u2 == null || u1.equals(u2))
                throw new ValidationException("Acesti useri nu sunt buni");
            var FriendShip = new FriendShip(u1, u2, FriendRequest.ACCEPTED);
            FriendShipRepo.save(FriendShip);
            u1.addFriend(u2);
            u2.addFriend(u1);

        }catch (Exception e){
            System.err.println(e);
        }
    }

    public FriendShip getFriendShipByUser(User u) {
        return printPrieteniile().stream()
                .filter(p -> p.getUser1().equals(u) || p.getUser2().equals(u))
                .findFirst()
                .orElse(null);
    }

    public User getUserByNumePrenume(String nume, String prenume) {
        return printUsers().stream()
                .filter(u -> u.getFirstName().equals(nume) && u.getLastName().equals(prenume))
                .findFirst()
                .orElse(null);
    }

    //Sterge FriendShip dupa nume prenume useri
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

    public void updateUser(UUID id, String newFirstName, String newLastName) throws Exception {
        Optional<User> oldUserOptional = UserRepo.findOne(id);

        if (oldUserOptional.isPresent()) {
            User oldUser = oldUserOptional.get();

            // Update user in the repository
            User newUser = new User(id, newFirstName, newLastName);
            UserRepo.update(newUser);

            // Update friendships referencing the old user
            Iterable<FriendShip> friendships = FriendShipRepo.findAll();
            for (FriendShip friendship : friendships) {
                if (friendship.getUser1().equals(oldUser)) {
                    friendship.setUser1(newUser);
                    FriendShipRepo.update(friendship);
                }
                if (friendship.getUser2().equals(oldUser)) {
                    friendship.setUser2(newUser);
                    FriendShipRepo.update(friendship);
                }
            }
        } else {
            // Handle the case where the user with the given ID is not found
            throw new ServiceException("User not found with ID: " + id);

        }
    }


    //DFS
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

    //Numara cate comunitati exista
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

    //Cea mai sociabila comunitate
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

    private int longestPath(List<User> nodes) {
        int max = 0;
        for (User u : nodes) {
            int l = longestPathFromSource(u);
            if (max < l)
                max = l;
        }
        return max;
    }

    private int longestPathFromSource(User source) {
        Map<UUID, Boolean> set = new HashMap<>();
        return BFS(source, set);
    }

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

    public void addEntities() {
        String[] listaNume = {"Popescu", "Ionescu", "Dumitru", "Stancu", "Radu", "Mihai", "Neagu", "Dragomir", "Bălan", "Enescu"};
        String[] listaPrenume = {"Ana", "Ion", "Maria", "Mihai", "Elena", "Victor", "Adriana", "Alexandru", "Andreea", "Gabriel"};

        Random rand = new Random();

        int indexNume = rand.nextInt(listaNume.length);
        int indexPrenume = rand.nextInt(listaPrenume.length);
        User u1 = new User(listaNume[indexNume], listaPrenume[indexPrenume]);
        UserRepo.save(u1);

        indexNume = rand.nextInt(listaNume.length);
        indexPrenume = rand.nextInt(listaPrenume.length);
        User u2 = new User(listaNume[indexNume], listaPrenume[indexPrenume]);
        UserRepo.save(u2);

        indexNume = rand.nextInt(listaNume.length);
        indexPrenume = rand.nextInt(listaPrenume.length);
        User u3 = new User(listaNume[indexNume], listaPrenume[indexPrenume]);
        UserRepo.save(u3);

        indexNume = rand.nextInt(listaNume.length);
        indexPrenume = rand.nextInt(listaPrenume.length);
        User u4 = new User(listaNume[indexNume], listaPrenume[indexPrenume]);
        UserRepo.save(u4);

        indexNume = rand.nextInt(listaNume.length);
        indexPrenume = rand.nextInt(listaPrenume.length);
        User u5 = new User(listaNume[indexNume], listaPrenume[indexPrenume]);
        UserRepo.save(u5);

        indexNume = rand.nextInt(listaNume.length);
        indexPrenume = rand.nextInt(listaPrenume.length);
        User u6 = new User(listaNume[indexNume], listaPrenume[indexPrenume]);
        UserRepo.save(u6);

        indexNume = rand.nextInt(listaNume.length);
        indexPrenume = rand.nextInt(listaPrenume.length);
        User u7 = new User(listaNume[indexNume], listaPrenume[indexPrenume]);
        UserRepo.save(u7);

        indexNume = rand.nextInt(listaNume.length);
        indexPrenume = rand.nextInt(listaPrenume.length);
        User u8 = new User(listaNume[indexNume], listaPrenume[indexPrenume]);
        UserRepo.save(u8);

        indexNume = rand.nextInt(listaNume.length);
        indexPrenume = rand.nextInt(listaPrenume.length);
        User u9 = new User(listaNume[indexNume], listaPrenume[indexPrenume]);
        UserRepo.save(u9);

        addFriendShip(u1.getFirstName(), u1.getLastName(), u3.getFirstName(), u3.getLastName());
        addFriendShip(u5.getFirstName(), u5.getLastName(), u3.getFirstName(), u3.getLastName());
        addFriendShip(u1.getFirstName(), u1.getLastName(), u7.getFirstName(), u7.getLastName());
        addFriendShip(u8.getFirstName(), u8.getLastName(), u9.getFirstName(), u9.getLastName());

    }

    // Add this method to the Service class
    public List<User> numarMinimPrieteni(int n) {
        Iterable<User> users = UserRepo.findAll();

        List<User> usersWithMinFriends = StreamSupport.stream(users.spliterator(), false)
                .filter(u -> (int) StreamSupport.stream(FriendShipRepo.findAll().spliterator(), false)
                        .filter(f -> f.getUser1().equals(u) || f.getUser2().equals(u))
                        .count() >= n)
                .collect(Collectors.toList());

        return usersWithMinFriends;
    }
    public int numarPrieteni(User user) {
        return (int) StreamSupport.stream(FriendShipRepo.findAll().spliterator(), false)
                .filter(f -> f.getUser1().equals(user) || f.getUser2().equals(user))
                .count();
    }


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


    private List<Observer<UserChangeEvent>> observers = new ArrayList<>();
    @Override
    public void addObserver(Observer<UserChangeEvent> o) {observers.add(o);

    }

    @Override
    public void removeObserver(Observer<UserChangeEvent> o) {observers.remove(o);

    }

    @Override
    public void notify(UserChangeEvent t) {
        observers.forEach(o->o.update(t));

    }
}
