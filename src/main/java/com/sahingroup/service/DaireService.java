package com.sahingroup.service;

import com.sahingroup.domain.Daire;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Daire}.
 */
public interface DaireService {
    /**
     * Save a daire.
     *
     * @param daire the entity to save.
     * @return the persisted entity.
     */
    Daire save(Daire daire) throws Exception;

    /**
     * Updates a daire.
     *
     * @param daire the entity to update.
     * @return the persisted entity.
     */
    Daire update(Daire daire);

    /**
     * Partially updates a daire.
     *
     * @param daire the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Daire> partialUpdate(Daire daire);

    /**
     * Get all the daires.
     *
     * @return the list of entities.
     */
    List<Daire> findAll();

    /**
     * Get all the daires with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Daire> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" daire.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Daire> findOne(String id);

    /**
     * Delete the "id" daire.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

}
