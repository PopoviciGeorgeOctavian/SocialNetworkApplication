package com.socialnetwork.lab78.repository;


import com.socialnetwork.lab78.domain.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * InMemoryRepository is a generic repository implementation that stores entities in memory using a HashMap.
 *
 * @param <ID> The type of the entity ID.
 * @param <E>  The type of the entity.
 */
public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {
    protected Map<ID, E> entities;

    /**
     * Constructs a new InMemoryRepository with an empty HashMap for storing entities.
     */
    public InMemoryRepository() {
        entities = new HashMap<>();
    }

    @Override
    public Optional<E> findOne(ID id) {
        // Validate and return an Optional containing the entity with the specified ID, if present
        if (id == null)
            throw new IllegalArgumentException("id must be not null");
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public Iterable<E> findAll() {
        // Return all entities stored in the repository
        return entities.values();
    }

    @Override
    public Optional<E> save(E entity) {
        // Validate and save the entity in the repository, returning an Optional containing the saved entity
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null");
        if (entities.get(entity.getId()) != null) {
            return Optional.ofNullable(entity);
        } else {
            entities.put(entity.getId(), entity);
        }
        return Optional.empty();
    }

    @Override
    public Optional<E> delete(ID id) {
        // Delete and return an Optional containing the removed entity with the specified ID
        return Optional.ofNullable(entities.remove(id));
    }

    @Override
    public Optional<E> update(E entity) {
        // Validate and update the entity in the repository, returning an Optional containing the updated entity
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null!");
        entities.put(entity.getId(), entity);
        if (entities.get(entity.getId()) != null) {
            return Optional.empty();
        }
        return Optional.ofNullable(entity);
    }
}