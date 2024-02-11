package com.socialnetwork.lab78.repository;



import com.socialnetwork.lab78.domain.Message;
import com.socialnetwork.lab78.domain.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * MessageDBRepository is a repository implementation that interacts with a relational database to store and retrieve Message entities.
 */
public class MessageDBRepository implements Repository<UUID, Message> {

    private UserDBRepository userRepo;
    private String url;
    private String user;
    private String password;

    /**
     * Constructs a new MessageDBRepository with the specified database connection details and a UserDBRepository for handling User entities.
     *
     * @param url      The URL of the database.
     * @param user     The username for the database connection.
     * @param password The password for the database connection.
     * @param userRepo The UserDBRepository for handling User entities.
     */
    public MessageDBRepository(String url, String user, String password, UserDBRepository userRepo) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.userRepo = userRepo;
    }

    @Override
    public Optional<Message> findOne(UUID id) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM messages WHERE id=?")) {
            statement.setObject(1, id);
            ResultSet r = statement.executeQuery();
            if (r.next()) {
                UUID id_from = (UUID) r.getObject("from_id");
                String list_to = r.getString("to_id");
                List<String> listToUUIDs = Arrays.asList(list_to.split(","));
                List<User> listUser = listToUUIDs.stream()
                        .map(uuidString -> {
                            try {
                                UUID uuid = UUID.fromString(uuidString.trim());
                                return userRepo.findOne(uuid).orElse(null);
                            } catch (IllegalArgumentException e) {
                                // Handle invalid UUID string
                                return null;
                            }
                        })
                        .filter(user -> user != null)
                        .collect(Collectors.toList());
                UUID id_reply = (UUID) r.getObject("id_reply");
                String mesaj = r.getString("mesaj");
                LocalDateTime date = r.getTimestamp("datamesaj").toLocalDateTime();
                Message m = new Message(id, userRepo.findOne(id_from).get(), listUser, mesaj, date);
                if (id_reply != null) {
                    m.setReply(findOne(id_reply).get());
                }
                m.setId(id);
                return Optional.ofNullable(m);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Message> findAll() {
        ArrayList<Message> messages = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM messages;")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                UUID id = (UUID) resultSet.getObject("id");
                UUID id_from = (UUID) resultSet.getObject("from_id");
                String list_to = resultSet.getString("to_id");
                List<String> listToUUIDs = Arrays.asList(list_to.split(","));
                List<User> listUser = listToUUIDs.stream()
                        .map(uuidString -> {
                            try {
                                UUID uuid = UUID.fromString(uuidString.trim());
                                return userRepo.findOne(uuid).orElse(null);
                            } catch (IllegalArgumentException e) {
                                // Handle invalid UUID string
                                return null;
                            }
                        })
                        .filter(user -> user != null)
                        .collect(Collectors.toList());
                UUID id_reply = (UUID) resultSet.getObject("id_reply");
                String mesaj = resultSet.getString("mesaj");
                LocalDateTime date = resultSet.getTimestamp("datamesaj").toLocalDateTime();
                Message m = new Message(id, userRepo.findOne(id_from).get(), listUser, mesaj, date);
                if (id_reply != null) {
                    m.setReply(findOne(id_reply).get());
                }
                m.setId(id);
                messages.add(m);
            }
            return messages;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Message> save(Message entity) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO messages(id,from_id,to_id,mesaj,datamesaj,id_reply) VALUES (?,?,?,?,?,?);")) {
            statement.setObject(1, entity.getId());
            statement.setObject(2, entity.getFrom().getId());

            String toUUIDs = entity.getTo().stream()
                    .map(user -> user.getId().toString())
                    .collect(Collectors.joining(","));

            statement.setString(3, toUUIDs);
            statement.setString(4, entity.getMessage());

            LocalDateTime dt = entity.getData();
            statement.setTimestamp(5, Timestamp.valueOf(dt));

            // Assuming id_reply is set to null for new messages
            statement.setObject(6, null);

            statement.executeUpdate();

            return Optional.of(entity);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Message> delete(UUID id) {
        return Optional.empty();
    }

    @Override
    public Optional<Message> update(Message entity) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("UPDATE messages SET id_reply = ? WHERE id = ?");) {
            statement.setObject(1, entity.getReply().getId());
            statement.setObject(2, entity.getId());
            statement.executeUpdate();

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}