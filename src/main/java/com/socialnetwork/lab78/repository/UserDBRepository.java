package com.socialnetwork.lab78.repository;


import com.socialnetwork.lab78.domain.User;

import java.sql.*;
import java.util.*;

public class UserDBRepository implements Repository<UUID, User> {
    final private String url;
    final private String user;
    final private String password;

    public UserDBRepository(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public Optional<User> findOne(UUID id) {
        try(Connection connection = DriverManager.getConnection(url,user,password);
            PreparedStatement statement  = connection.prepareStatement("SELECT * FROM Utilizatori WHERE UUID=?"))
        {
            //statement.setLong(1,id);
            statement.setObject(1,id);
            ResultSet r = statement.executeQuery();
            if (r.next()){

                String FirstName = r.getString("FirstName");
                String LastName = r.getString("LastName");
                User u1 = new User(id,FirstName, LastName);
                return Optional.of(u1);
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<User> findAll() {
        try(Connection connection = DriverManager.getConnection(url,user,password);
            PreparedStatement statement  = connection.prepareStatement("SELECT * FROM Utilizatori"))
        {
            ArrayList<User> list = new ArrayList<>();
            ResultSet r = statement.executeQuery();
            while (r.next()){
                UUID id = (UUID)r.getObject("UUID");
                String FirstName = r.getString("FirstName");
                String LastName = r.getString("LastName");
                User u1 = new User(id,FirstName, LastName);
                list.add(u1);
            }
            return list;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> save(User entity) {
        try(Connection connection = DriverManager.getConnection(url,user,password);
            PreparedStatement statement  = connection.prepareStatement("INSERT INTO Utilizatori(UUID,FirstName,LastName) VALUES (?,?,?)"))
        {
            statement.setObject(1,entity.getId());
            statement.setString(2,entity.getFirstName());
            statement.setString(3,entity.getLastName());
            //statement.setInt(3,entity.getYear());
            int affectedRows = statement.executeUpdate();
            return affectedRows!=0? Optional.empty():Optional.of(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }    }

    @Override
    public Optional<User> delete(UUID uuid) {
        try(Connection connection = DriverManager.getConnection(url,user,password);
            PreparedStatement statement  = connection.prepareStatement("DELETE FROM Utilizatori WHERE UUID = ?"))
        {
            var cv = findOne(uuid);
            //statement.setLong(1,uuid);
            statement.setObject(1,uuid);
            int affectedRows = statement.executeUpdate();
            return affectedRows==0? Optional.empty():cv;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> update(User entity) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("UPDATE Utilizatori SET FirstName = ?, LastName = ? WHERE UUID = ?")) {
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setObject(3,entity.getId());

            int affectedRows = statement.executeUpdate();
            return affectedRows != 0 ? Optional.of(entity) : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}


