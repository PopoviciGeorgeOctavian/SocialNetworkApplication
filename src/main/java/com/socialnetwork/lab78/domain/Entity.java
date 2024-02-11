package com.socialnetwork.lab78.domain;


import java.io.Serializable;
import java.util.Objects;

/**
 * An abstract base class representing entities in the social network domain.
 *
 * @param <ID> The type of the entity's identifier.
 */
public class Entity<ID> implements Serializable {
    protected ID id;

    public ID getId(){
        return id;
    }

    public void setId(ID id){
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity<?> entity = (Entity<?>) o;
        return Objects.equals(id, entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Entity{" +
                "id=" + id +
                '}';
    }
}
