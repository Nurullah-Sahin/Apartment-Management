package com.sahingroup.web.rest;

import com.sahingroup.domain.Daire;
import com.sahingroup.repository.DaireRepository;
import com.sahingroup.service.DaireService;
import com.sahingroup.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.sahingroup.domain.Daire}.
 */
@RestController
@RequestMapping("/api")
public class DaireResource {

    private final Logger log = LoggerFactory.getLogger(DaireResource.class);

    private static final String ENTITY_NAME = "daire";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DaireService daireService;

    private final DaireRepository daireRepository;

    public DaireResource(DaireService daireService, DaireRepository daireRepository) {
        this.daireService = daireService;
        this.daireRepository = daireRepository;
    }

    /**
     * {@code POST  /daires} : Create a new daire.
     *
     * @param daire the daire to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new daire, or with status {@code 400 (Bad Request)} if the daire has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/daires")
    public ResponseEntity<Daire> createDaire(@RequestBody Daire daire) throws Exception {
        log.debug("REST request to save Daire : {}", daire);
        if (daire.getId() != null) {
            throw new BadRequestAlertException("A new daire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Daire result = daireService.save(daire);
        return ResponseEntity
            .created(new URI("/api/daires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /daires/:id} : Updates an existing daire.
     *
     * @param id the id of the daire to save.
     * @param daire the daire to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated daire,
     * or with status {@code 400 (Bad Request)} if the daire is not valid,
     * or with status {@code 500 (Internal Server Error)} if the daire couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/daires/{id}")
    public ResponseEntity<Daire> updateDaire(@PathVariable(value = "id", required = false) final String id, @RequestBody Daire daire)
        throws URISyntaxException {
        log.debug("REST request to update Daire : {}, {}", id, daire);
        if (daire.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, daire.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!daireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Daire result = daireService.update(daire);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, daire.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /daires/:id} : Partial updates given fields of an existing daire, field will ignore if it is null
     *
     * @param id the id of the daire to save.
     * @param daire the daire to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated daire,
     * or with status {@code 400 (Bad Request)} if the daire is not valid,
     * or with status {@code 404 (Not Found)} if the daire is not found,
     * or with status {@code 500 (Internal Server Error)} if the daire couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/daires/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Daire> partialUpdateDaire(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Daire daire
    ) throws URISyntaxException {
        log.debug("REST request to partial update Daire partially : {}, {}", id, daire);
        if (daire.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, daire.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!daireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Daire> result = daireService.partialUpdate(daire);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, daire.getId()));
    }

    /**
     * {@code GET  /daires} : get all the daires.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of daires in body.
     */
    @GetMapping("/daires")
    public List<Daire> getAllDaires(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Daires");
        return daireService.findAll();
    }

    /**
     * {@code GET  /daires/:id} : get the "id" daire.
     *
     * @param id the id of the daire to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the daire, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/daires/{id}")
    public ResponseEntity<Daire> getDaire(@PathVariable String id) {
        log.debug("REST request to get Daire : {}", id);
        Optional<Daire> daire = daireService.findOne(id);
        return ResponseUtil.wrapOrNotFound(daire);
    }

    /**
     * {@code DELETE  /daires/:id} : delete the "id" daire.
     *
     * @param id the id of the daire to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/daires/{id}")
    public ResponseEntity<Void> deleteDaire(@PathVariable String id) {
        log.debug("REST request to delete Daire : {}", id);
        daireService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
