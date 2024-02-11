package com.socialnetwork.lab78.repository;


import com.socialnetwork.lab78.Paging.Page;
import com.socialnetwork.lab78.Paging.Pageable;
import com.socialnetwork.lab78.Paging.PagingRepository;
import com.socialnetwork.lab78.domain.User;

import java.sql.*;
import java.util.*;

/**
 * UserDBRepository is a repository implementation that interacts with a relational database to store and retrieve User entities.
 * It implements the PagingRepository interface for pagination support.
 */
public class UserDBRepository implements PagingRepository<UUID, User> {
    final private String url;
    final private String user;
    final private String password;

    /**
     * Constructs a new UserDBRepository with the specified database connection details.
     *
     * @param url      The URL of the database.
     * @param user     The username for the database connection.
     * @param password The password for the database connection.
     */
    public UserDBRepository(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public Optional<User> findOne(UUID id) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Utilizatori WHERE UUID=?")) {
            statement.setObject(1, id);
            ResultSet r = statement.executeQuery();
            if (r.next()) {
                String firstName = r.getString("FirstName");
                String lastName = r.getString("LastName");
                String email = r.getString("Email");
                String password = r.getString("Password");
                User user = new User(id, firstName, lastName, email, password);
                return Optional.of(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        List<User> userList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement pagePreparedStatement = connection.prepareStatement("SELECT * FROM Utilizatori LIMIT ? OFFSET ?");
             PreparedStatement countPreparedStatement = connection.prepareStatement("SELECT COUNT(*) AS count FROM Utilizatori")) {
            pagePreparedStatement.setInt(1, pageable.getPageSize());
            pagePreparedStatement.setInt(2, pageable.getPageSize() * pageable.getPageNumber());
            try (ResultSet pageResultSet = pagePreparedStatement.executeQuery();
                 ResultSet countResultSet = countPreparedStatement.executeQuery();) {
                while (pageResultSet.next()) {
                    UUID id = (UUID) pageResultSet.getObject("UUID");
                    String firstName = pageResultSet.getString("FirstName");
                    String lastName = pageResultSet.getString("LastName");
                    String email = pageResultSet.getString("Email");
                    String password = pageResultSet.getString("Password");
                    User user = new User(id, firstName, lastName, email, password);
                    userList.add(user);
                }
                int totalCount = 0;
                if (countResultSet.next()) {
                    totalCount = countResultSet.getInt("count");
                }

                return new Page<>(userList, totalCount);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<User> findAll() {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Utilizatori")) {
            ArrayList<User> list = new ArrayList<>();
            ResultSet r = statement.executeQuery();
            while (r.next()) {
                UUID id = (UUID) r.getObject("UUID");
                String firstName = r.getString("FirstName");
                String lastName = r.getString("LastName");
                String email = r.getString("Email");
                String password = r.getString("Password");
                User user = new User(id, firstName, lastName, email, password);
                list.add(user);
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> save(User entity) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO Utilizatori(UUID,FirstName,LastName,Email,Password) VALUES (?,?,?,?,?)")) {
            statement.setObject(1, entity.getId());
            statement.setString(2, entity.getFirstName());
            statement.setString(3, entity.getLastName());
            statement.setString(4, entity.getEmail());
            statement.setString(5, entity.getPassword());
            int affectedRows = statement.executeUpdate();
            return affectedRows != 0 ? Optional.empty() : Optional.of(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> delete(UUID uuid) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM Utilizatori WHERE UUID = ?")) {
            var cv = findOne(uuid);
            statement.setObject(1, uuid);
            int affectedRows = statement.executeUpdate();
            return affectedRows == 0 ? Optional.empty() : cv;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> update(User entity) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("UPDATE Utilizatori SET FirstName = ?, LastName = ?, Email = ? WHERE UUID = ?")) {
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setString(3, entity.getEmail());
            statement.setObject(4, entity.getId());

            int affectedRows = statement.executeUpdate();
            return affectedRows != 0 ? Optional.of(entity) : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}