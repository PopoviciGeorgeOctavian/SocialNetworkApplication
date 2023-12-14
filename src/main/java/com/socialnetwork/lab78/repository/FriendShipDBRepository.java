package com.socialnetwork.lab78.repository;



import com.socialnetwork.lab78.domain.FriendRequest;
import com.socialnetwork.lab78.domain.FriendShip;
import com.socialnetwork.lab78.domain.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class FriendShipDBRepository implements Repository<UUID, FriendShip> {

    private String url;
    private String user;
    private String password;

    public FriendShipDBRepository(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public Optional<FriendShip> findOne(UUID id) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Prietenii WHERE UUID=?");) {
            //statement.setLong(1,id);
            statement.setObject(1, id);
            ResultSet r = statement.executeQuery();
            if (r.next()) {
                UUID idu1 = (UUID) r.getObject("idu1");
                String FirstNameU1 = r.getString("FirstNameU1");
                String LastNameU1 = r.getString("LastNameU1");
                User u1 = new User(idu1, FirstNameU1, LastNameU1);
                UUID idu2 = (UUID) r.getObject("idu2");
                String FirstNameU2 = r.getString("FirstNameU2");
                String LastNameU2 = r.getString("LastNameU2");
                User u2 = new User(idu2, FirstNameU2, LastNameU2);
                FriendShip p1 = new FriendShip(id, u1, u2);
                return Optional.of(p1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<FriendShip> findAll() {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Prietenii")) {
            ArrayList<FriendShip> list = new ArrayList<>();
            ResultSet r = statement.executeQuery();
            while (r.next()) {
                UUID id = (UUID) r.getObject("UUID");
                UUID idu1 = (UUID) r.getObject("idu1");
                String FirstNameU1 = r.getString("FirstNameU1");
                String LastNameU1 = r.getString("LastNameU1");
                User u1 = new User(idu1, FirstNameU1, LastNameU1);
                UUID idu2 = (UUID) r.getObject("idu2");
                String FirstNameU2 = r.getString("FirstNameU2");
                String LastNameU2 = r.getString("LastNameU2");
                User u2 = new User(idu2, FirstNameU2, LastNameU2);
                String status = r.getString("status");
                FriendShip p1 = new FriendShip(id, u1, u2, FriendRequest.valueOf(status));
                list.add(p1);
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<FriendShip> save(FriendShip entity) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO Prietenii(UUID,FirstNameU1,LastNameU1,FirstNameU2,LastNameU2,friendsFrom,idu1,idu2,status) VALUES (?,?,?,?,?,?,?,?,?)");) {
            statement.setObject(1, entity.getId());
            statement.setString(2, entity.getUser1().getFirstName());
            statement.setString(3, entity.getUser1().getLastName());
            statement.setString(4, entity.getUser2().getFirstName());
            statement.setString(5, entity.getUser2().getLastName());
            statement.setTimestamp(6, Timestamp.valueOf(entity.getDate()));
            statement.setObject(7, entity.getUser1().getId());
            statement.setObject(8, entity.getUser2().getId());
            statement.setString(9, entity.getAcceptance().name());
            int affectedRows = statement.executeUpdate();
            return affectedRows != 0 ? Optional.empty() : Optional.of(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<FriendShip> delete(UUID uuid) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM Prietenii WHERE UUID = ?");) {
            var cv = findOne(uuid);
            statement.setObject(1, uuid);
            int affectedRows = statement.executeUpdate();
            return affectedRows == 0 ? Optional.empty() : cv;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<FriendShip> update(FriendShip entity) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("UPDATE Prietenii SET FirstNameU1 = ?, LastNameU1 = ?, FirstNameU2 = ?, LastNameU2 = ?, idu1 = ?, idu2 = ?, friendsfrom =? WHERE UUID = ?");) {
            statement.setString(1, entity.getUser1().getFirstName());
            statement.setString(2, entity.getUser1().getLastName());
            statement.setString(3, entity.getUser2().getFirstName());
            statement.setString(4, entity.getUser2().getLastName());
            statement.setObject(5, entity.getUser1().getId());
            statement.setObject(6, entity.getUser2().getId());
            statement.setTimestamp(7, Timestamp.valueOf(entity.getDate()));
            statement.setObject(8, entity.getId());
            int affectedRows = statement.executeUpdate();
            return affectedRows != 0 ? Optional.empty() : Optional.of(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}