package com.sahingroup.service;

import com.sahingroup.domain.Mesaj;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Mesaj}.
 */
public interface MesajService {
    /**
     * Save a mesaj.
     *
     * @param mesaj the entity to save.
     * @return the persisted entity.
     */
    Mesaj save(Mesaj mesaj);

    /**
     * Updates a mesaj.
     *
     * @param mesaj the entity to update.
     * @return the persisted entity.
     */
    Mesaj update(Mesaj mesaj);

    /**
     * Partially updates a mesaj.
     *
     * @param mesaj the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Mesaj> partialUpdate(Mesaj mesaj);

    /**
     * Get all the mesajs.
     *
     * @return the list of entities.
     */
    List<Mesaj> findAll();

    /**
     * Get all the mesajs with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Mesaj> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" mesaj.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Mesaj> findOne(String id);

    /**
     * Delete the "id" mesaj.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
