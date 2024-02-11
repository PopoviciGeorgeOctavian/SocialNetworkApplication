// PagingRepository interface extending Repository with pagination support
package com.socialnetwork.lab78.Paging;

import com.socialnetwork.lab78.domain.Entity;
import com.socialnetwork.lab78.repository.Repository;

/**
 * Interface for a generic repository with paging capabilities.
 *
 * @param <ID> The type of the entity identifier.
 * @param <E>  The type of the entity.
 */
public interface PagingRepository<ID, E extends Entity<ID>> extends Repository<ID, E> {

    /**
     * Finds all entities with pagination.
     *
     * @param pageable The object representing the pagination information.
     * @return A page of entities.
     */
    Page<E> findAll(Pageable pageable);
}