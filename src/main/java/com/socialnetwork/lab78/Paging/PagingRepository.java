package com.socialnetwork.lab78.Paging;

import com.socialnetwork.lab78.domain.Entity;
import com.socialnetwork.lab78.repository.Repository;

public interface  PagingRepository<ID, E extends Entity<ID>> extends Repository<ID,E> {
    Page<E> findAll(Pageable pageable);
}
