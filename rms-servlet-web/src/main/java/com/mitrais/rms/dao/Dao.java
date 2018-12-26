package com.mitrais.rms.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * This interface provides generic CRUD operation. See <a href="https://en.wikipedia.org/wiki/Create,_read,_update_and_delete>CRUD</a>
 * Its uses generic to provide type-safe operation.
 */
public interface Dao<T, ID>
{
    /**
     * Find single record by id.
     * @param id id
     * @return record wrapped by {@link Optional}
     */
    Optional<T> find(ID id);

    /**
     * Find all records
     * @return list of records
     */
    List<T> findAll();

    /**
     * Persist record to database
     * @param o record
     * @return true if operation success
     */
    boolean save(T o) throws SQLException;

    /**
     * Persist existing record to database
     * @param o record
     * @return true if operation success
     */
    boolean update(T o) throws SQLException;

    /**
     * Remove existing record from database
     * @param o record
     * @return true if operaion success
     */
    boolean delete(T o) throws SQLException;
}
