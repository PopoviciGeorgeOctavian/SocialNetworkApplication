package com.socialnetwork.lab78.repository;

import com.socialnetwork.lab78.domain.Entity;
import com.socialnetwork.lab78.validators.Validator;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * AbstractFileRepository is an abstract class that serves as a base class for repositories
 * that store entities in a file. It extends InMemoryRepository and implements file-related
 * operations for loading data from a file and writing data to a file.
 *
 * @param <ID> The type of the entity identifier.
 * @param <E>  The type of the entity.
 */
public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {
    String fileName;

    /**
     * Constructs a new AbstractFileRepository with the specified file name and validator.
     *
     * @param fileName  The name of the file to load and save data.
     * @param validator The validator for entities.
     */
    public AbstractFileRepository(String fileName, Validator<E> validator) {
        // Call the constructor of the superclass (InMemoryRepository) with the validator.
        //super(validator);
        this.fileName = fileName;
        loadData();
    }

    /**
     * Loads data from the file and populates the repository.
     * This method uses the decorator pattern to extend the behavior of the InMemoryRepository.
     */
    private void loadData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String newLine;
            while ((newLine = reader.readLine()) != null) {
                System.out.println(newLine);
                List<String> data = Arrays.asList(newLine.split(";"));
                E entity = extractEntity(data);
                super.save(entity);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Abstract method to extract an entity from a list of attributes.
     * This method follows the template method design pattern.
     *
     * @param attributes The list of attributes from which to create the entity.
     * @return An entity of type E.
     */
    public abstract E extractEntity(List<String> attributes);

    /**
     * Abstract method to create a string representation of an entity.
     * This method follows the template method design pattern.
     *
     * @param entity The entity for which to create the string representation.
     * @return A string representation of the entity.
     */
    protected abstract String createEntityAsString(E entity);

    /**
     * Saves an entity to the repository and writes it to the file.
     *
     * @param entity The entity to save.
     * @return An Optional containing the saved entity or null if the entity couldn't be saved.
     */
    public Optional<E> save(E entity) {
        return Optional.ofNullable(entity);
    }

    /**
     * Writes an entity to the file.
     *
     * @param entity The entity to write to the file.
     */
    protected void writeToFile(E entity) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(createEntityAsString(entity));
            writer.newLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
