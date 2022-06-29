package com.sahingroup.service;

import com.sahingroup.domain.Apartman;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Apartman}.
 */
public interface ApartmanService {
    /**
     * Save a apartman.
     *
     * @param apartman the entity to save.
     * @return the persisted entity.
     */
    Apartman save(Apartman apartman);

    /**
     * Updates a apartman.
     *
     * @param apartman the entity to update.
     * @return the persisted entity.
     */
    Apartman update(Apartman apartman);

    /**
     * Partially updates a apartman.
     *
     * @param apartman the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Apartman> partialUpdate(Apartman apartman);

    /**
     * Get all the apartmen.
     *
     * @return the list of entities.
     */
    List<Apartman> findAll();

    /**
     * Get the "id" apartman.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Apartman> findOne(String id);

    /**
     * Delete the "id" apartman.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
